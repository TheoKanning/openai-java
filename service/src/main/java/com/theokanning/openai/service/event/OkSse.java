package com.theokanning.openai.service.event;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * OkSse is a plugin for OkHttp library that extends its functionality to create a Server Sent Event client.
 * <p>
 * The usage of this class follows the same logic as any OkHttp request.
 * <p>
 * <p>Use {@code new OkSse()} to create a new instance with a new instance of {@link OkHttpClient} with default settings
 * <pre>   {@code
 *
 *   // The singleton HTTP client.
 *   public final OkSse okSseClient = new OkSse();
 * }</pre>
 * <p>
 * <p>Use {@code new OkSse(okHttpClient)} to create a new instance that shares the instance of {@link OkHttpClient}.
 * This would be the prefered way, since the resources of the OkHttpClient will be reused for the SSE
 * <pre>   {@code
 *
 *   // The singleton HTTP client.
 *   public final OkSse okSseClient = new OkSse(okHttpClient);
 * }</pre>
 * <p>
 * To create a new {@link ServerSentEvent} call {@link OkSse#newServerSentEvent(Request, ServerSentEvent.Listener)}
 * giving the desired {@link Request}. Note that must be a GET request.
 * <p>
 * OkSse will make sure to build the proper parameters needed for SSE conneciton and return the instance.
 */
public class OkSse {

    private final OkHttpClient client;

    /**
     * Create a OkSse using a new instance of {@link OkHttpClient} with the default settings.
     */
    public OkSse() {
        this(new OkHttpClient.Builder().readTimeout(0, TimeUnit.SECONDS).retryOnConnectionFailure(true).build());
    }

    /**
     * Creates a new OkSse using the shared {@link OkHttpClient}
     *
     * @param client
     */
    public OkSse(OkHttpClient client) {
        this.client = client.newBuilder().protocols(Collections.singletonList(Protocol.HTTP_1_1)).build();
    }

    /**
     * Get the {@link OkHttpClient} used to create this instance.
     *
     * @return the instance of the {@link OkHttpClient}
     */
    public OkHttpClient getClient() {
        return client;
    }

    /**
     * Create a new instance of {@link ServerSentEvent} that will handle the connection and communication with
     * the SSE Server.
     *
     * @param request  the OkHttp {@link Request} with the valid information to create the connection with the server.
     * @param listener the {@link ServerSentEvent.Listener} to attach to this SSE.
     * @return a new instance of {@link ServerSentEvent} that will automatically start the connection.
     */
    public ServerSentEvent newServerSentEvent(Request request, ServerSentEvent.Listener listener) {
        RealServerSentEvent sse = new RealServerSentEvent(request, listener);
        sse.connect(client);
        return sse;
    }
}
