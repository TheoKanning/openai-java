package com.theokanning.openai.service.event;

import okhttp3.*;
import okio.BufferedSource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

class RealServerSentEvent implements ServerSentEvent {

    private final Listener listener;
    private final Request originalRequest;

    private OkHttpClient client;
    private Call call;
    private RealServerSentEvent.Reader sseReader;

    private long reconnectTime = TimeUnit.SECONDS.toMillis(3);
    private long readTimeoutMillis = 0;
    private String lastEventId;

    RealServerSentEvent(Request request, Listener listener) {
        this.originalRequest = request;
        this.listener = listener;
    }

    void connect(OkHttpClient client) {
        this.client = client;
        prepareCall(originalRequest);
        enqueue();
    }

    private void prepareCall(Request request) {
        if (client == null) {
            throw new AssertionError("Client is null");
        }
        Request.Builder requestBuilder = request.newBuilder()
                .header("Accept-Encoding", "")
                .header("Accept", "text/event-stream")
                .header("Cache-Control", "no-cache");

        if (lastEventId != null) {
            requestBuilder.header("Last-Event-Id", lastEventId);
        }

        call = client.newCall(requestBuilder.build());
    }

    private void enqueue() {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                notifyFailure(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    openSse(response);
                } else {
                    notifyFailure(new IOException(response.message()), response);
                }
            }
        });
    }

    private void openSse(Response response) {
        sseReader = new RealServerSentEvent.Reader(response.body().source());
        sseReader.setTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS);
        listener.onOpen(this, response);

        //noinspection StatementWithEmptyBody
        while (call != null && !call.isCanceled() && sseReader.read()) {
        }
    }

    private void notifyFailure(Throwable throwable, Response response) {
        if (!retry(throwable, response)) {
            listener.onClosed(this);
            close();
        }
    }

    private boolean retry(Throwable throwable, Response response) {
        if (!Thread.currentThread().isInterrupted() && !call.isCanceled() && listener.onRetryError(this, throwable, response)) {
            Request request = listener.onPreRetry(this, originalRequest);
            if (request == null) {
                return false;
            }
            prepareCall(request);
            try {
                Thread.sleep(reconnectTime);
            } catch (InterruptedException ignored) {
                return false;
            }
            if (!Thread.currentThread().isInterrupted() && !call.isCanceled()) {
                enqueue();
                return true;
            }
        }
        return false;
    }

    @Override
    public Request request() {
        return originalRequest;
    }

    @Override
    public void setTimeout(long timeout, TimeUnit unit) {
        if (sseReader != null) {
            sseReader.setTimeout(timeout, unit);
        }
        readTimeoutMillis = unit.toMillis(timeout);
    }

    @Override
    public void close() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    /**
     * Internal reader for the SSE channel. This will wait for data being send and will parse it according to the
     * SSE standard.
     *
     * @see RealServerSentEvent.Reader#read()
     */
    private class Reader {

        private static final char COLON_DIVIDER = ':';
        private static final String UTF8_BOM = "\uFEFF";

        private static final String DATA = "data";
        private static final String ID = "id";
        private static final String EVENT = "event";
        private static final String RETRY = "retry";
        private static final String DEFAULT_EVENT = "message";
        private static final String EMPTY_STRING = "";

        private final Pattern DIGITS_ONLY = Pattern.compile("^[\\d]+$");

        private final BufferedSource source;

        // Intentionally done to reuse StringBuilder for memory optimization
        @SuppressWarnings("PMD.AvoidStringBufferField")
        private StringBuilder data = new StringBuilder();
        private String eventName = DEFAULT_EVENT;

        Reader(BufferedSource source) {
            this.source = source;
        }

        /**
         * Blocking call that will try to read a line from the source
         *
         * @return true if the read was successfully, false if an error was thrown
         */
        boolean read() {
            try {
                String line = source.readUtf8LineStrict();
                processLine(line);
            }
            catch (IOException e) {
                notifyFailure(e, null);
                return false;
            }
            return true;
        }

        /**
         * Sets a reading timeout, so the read operation will get unblock if this timeout is reached.
         *
         * @param timeout timeout to set
         * @param unit unit of the timeout to set
         */
        void setTimeout(long timeout, TimeUnit unit) {
            if (source != null) {
                source.timeout().timeout(timeout, unit);
            }
        }

        private void processLine(String line) {
            //log.info("Sse read line: " + line);
            if (line == null || line.isEmpty()) { // If the line is empty (a blank line). Dispatch the event.
                dispatchEvent();
                return;
            }

            int colonIndex = line.indexOf(COLON_DIVIDER);
            if (colonIndex == 0) { // If line starts with COLON dispatch a comment
                listener.onComment(RealServerSentEvent.this, line.substring(1).trim());
            } else if (colonIndex != -1) { // Collect the characters on the line after the first U+003A COLON character (:), and let value be that string.
                String field = line.substring(0, colonIndex);
                String value = EMPTY_STRING;
                int valueIndex = colonIndex + 1;
                if (valueIndex < line.length()) {
                    if (line.charAt(valueIndex) == ' ') { // If value starts with a single U+0020 SPACE character, remove it from value.
                        valueIndex++;
                    }
                    value = line.substring(valueIndex);
                }
                processField(field, value);
            } else {
                processField(line, EMPTY_STRING);
            }
        }

        private void dispatchEvent() {
            if (data.length() == 0) {
                return;
            }
            String dataString = data.toString();
            if (dataString.endsWith("\n")) {
                dataString = dataString.substring(0, dataString.length() - 1);
            }
            if("[DONE]".equals(dataString)){
                RealServerSentEvent.this.close();
            }
            listener.onMessage(RealServerSentEvent.this, lastEventId, eventName, dataString);
            data.setLength(0);
            eventName = DEFAULT_EVENT;
        }

        private void processField(String field, String value) {
            if (DATA.equals(field)) {
                data.append(value).append('\n');
            } else if (ID.equals(field)) {
                lastEventId = value;
            } else if (EVENT.equals(field)) {
                eventName = value;
            } else if (RETRY.equals(field) && DIGITS_ONLY.matcher(value).matches()) {
                long timeout = Long.parseLong(value);
                if (listener.onRetryTime(RealServerSentEvent.this, timeout)) {
                    reconnectTime = timeout;
                }
            }
        }
    }
}
