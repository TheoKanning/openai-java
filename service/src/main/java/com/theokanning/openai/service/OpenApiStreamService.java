package com.theokanning.openai.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.theokanning.openai.IStream;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import okhttp3.*;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSourceListener;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class OpenApiStreamService {
    private static final String BASE_URL = "https://api.openai.com/";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final ObjectMapper errorMapper = defaultObjectMapper();

    private final OkHttpClient client;

    /**
     * Creates a new OpenAiService that wraps OpenAiApi
     *
     * @param token OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     */
    public OpenApiStreamService(final String token) {
        this(token, DEFAULT_TIMEOUT);
    }

    /**
     * Creates a new OpenAiService that wraps OpenAiApi
     *
     * @param token   OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     * @param timeout http read timeout, Duration.ZERO means no timeout
     */
    public OpenApiStreamService(final String token, final Duration timeout) {
        this(defaultClient(token, timeout));
    }

    public OpenApiStreamService(OkHttpClient client) {
        this.client = client;
    }


    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper;
    }

    public static OkHttpClient defaultClient(String token, Duration timeout) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(token))
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    public void streamRequest(IStream streamRequest, String path, EventSourceListener listener) {
        try {
            streamRequest.setStream(true);
            String requestBody = errorMapper.writeValueAsString(streamRequest);
            Request request = new Request.Builder()
                    .url(BASE_URL + path)
                    .header("Accept-Encoding", "")
                    .header("Accept", "text/event-stream")
                    .header("Cache-Control", "no-cache")
                    .post(RequestBody.create(MediaType.get("application/json"), requestBody))
                    .build();
            RealEventSource realEventSource = new RealEventSource(request, listener);
            realEventSource.connect(client);
        } catch (JsonProcessingException e) {
            e.getMessage();
        }
    }


}
