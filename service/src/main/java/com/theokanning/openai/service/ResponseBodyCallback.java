package com.theokanning.openai.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiError;
import com.theokanning.openai.OpenAiHttpException;

import io.reactivex.FlowableEmitter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Callback to parse Server Sent Events (SSE) from raw InputStream and
 * emit the events with io.reactivex.FlowableEmitter to allow streaming of
 * SSE.
 */
public class ResponseBodyCallback implements Callback<ResponseBody> {
    private static final ObjectMapper mapper = OpenAiService.defaultObjectMapper();

    private FlowableEmitter<SSE> emitter;
    private boolean emitDone;

    public ResponseBodyCallback(FlowableEmitter<SSE> emitter, boolean emitDone) {
        this.emitter = emitter;
        this.emitDone = emitDone;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        BufferedReader reader = null;

        try {
            if (!response.isSuccessful()) {
                handleUnsuccessfulResponse(response);
                return;
            }

            InputStream in = response.body().byteStream();
            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            parseSSE(reader);

            emitter.onComplete();

        } catch (Throwable t) {
            onFailure(call, t);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    private void handleUnsuccessfulResponse(Response<ResponseBody> response) throws IOException {
        HttpException e = new HttpException(response);
        ResponseBody errorBody = response.errorBody();

        if (errorBody == null) {
            throw e;
        } else {
            OpenAiError error = mapper.readValue(
                    errorBody.string(),
                    OpenAiError.class
            );
            throw new OpenAiHttpException(error, e, e.code());
        }
    }

    private void parseSSE(BufferedReader reader) throws IOException {
        String line;
        SSE sse = null;

        try {
            while (!emitter.isCancelled() && (line = reader.readLine()) != null) {
                if (line.startsWith("data:")) {
                    String data = line.substring(5).trim();
                    sse = new SSE(data);
                } else if (line.equals("") && sse != null) {
                    handleSSELine(sse);
                    sse = null;
                } else {
                    throw new SSEFormatException("Invalid sse format! " + line);
                }
            }
        } catch (SSEFormatException e) {
            throw new IOException("Error parsing SSE", e);
        }
    }


    private void handleSSELine(SSE sse) {
        if (sse.isDone()) {
            if (emitDone) {
                emitter.onNext(sse);
            }
            return;
        }

        emitter.onNext(sse);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        emitter.onError(t);
    }
}