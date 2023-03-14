package com.theokanning.openai.service;

import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by julian0zzx on 2023/3/14.
 */
public abstract class StreamHandler<T> extends EventSourceListener {

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        eventSource.cancel();
        super.onClosed(eventSource);
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
        T result = handleEvent(eventSource, data);
        super.onEvent(eventSource, id, type, data);
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        eventSource.cancel();
        super.onFailure(eventSource, t, response);
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        super.onOpen(eventSource, response);
    }

    public abstract T handleEvent(EventSource eventSource, String data);

    public abstract void innerHandleEvent(T result);

}
