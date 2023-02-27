package com.theokanning.openai.service.event;


import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public interface ServerSentEvent {

    /**
     * @return the original request that initiated the Server Sent Event.
     */
    Request request();

    /**
     * Defines a timeout to close the connection when now events are received. This is useful to avoid hanging connections.
     * Note if this is set a heartbeat mechanism should be implemented in server side to avoid closing connections when no events.
     *
     * @param timeout timeout to set
     * @param unit    the {@link TimeUnit} of the timeout to set
     */
    void setTimeout(long timeout, TimeUnit unit);

    /**
     * Force the Server Sent event channel to close. This will cancel any pending request or close the established channel.
     */
    void close();

    interface Listener {

        /**
         * Notify when the connection is open an established. From this point on, new message could be received.
         *
         * @param sse      the instance of {@link ServerSentEvent}
         * @param response the response from the server after establishing the connection
         */
        void onOpen(ServerSentEvent sse, Response response);

        /**
         * Called every time a message is received.
         *
         * @param sse     the instance of {@link ServerSentEvent}
         * @param id      id sent by the server to identify the message
         * @param event   event type of this message
         * @param message message payload
         */
        void onMessage(ServerSentEvent sse, String id, String event, String message);

        /**
         * Called every time a comment is received.
         *
         * @param sse     the instance of {@link ServerSentEvent}
         * @param comment the content of the comment
         */
        void onComment(ServerSentEvent sse, String comment);

        /**
         * The stream can define the retry time sending a message with "retry: milliseconds"
         * If this event is received this method will be called with the sent value
         *
         * @param sse          the instance of {@link ServerSentEvent}
         * @param milliseconds new retry time in milliseconds
         * @return true if this retry time should be used, false otherwise
         */
        boolean onRetryTime(ServerSentEvent sse, long milliseconds);

        /**
         * Notify when the connection failed either because it could not be establish or the connection broke.
         * The Server Sent Event protocol defines that should be able to reestablish a connection using retry mechanism.
         * In some cases depending on the error the connection should not be retry.
         *
         * Implement this method to define this behavior.
         *
         * @param sse       the instance of {@link ServerSentEvent}
         * @param throwable the instance of the error that caused the failure
         * @param response  the response of the server that caused the failure, it might be null.
         * @return true if the connection should be retried after the defined retry time, false to avoid the retry, this will close the SSE.
         */
        boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response);

        /**
         * Notify that the connection was closed.
         *
         * @param sse the instance of {@link ServerSentEvent}
         */
        void onClosed(ServerSentEvent sse);

        /**
         * Notifies client before retrying to connect. At this point listener may decide to return
         * {@code originalRequest} to repeat last request or another one to alternate
         *
         * @param sse             the instance of {@link ServerSentEvent}
         * @param originalRequest the request to be retried
         * @return call to be executed or {@code null} to cancel retry and close the SSE channel
         */
        Request onPreRetry(ServerSentEvent sse, Request originalRequest);
    }
}
