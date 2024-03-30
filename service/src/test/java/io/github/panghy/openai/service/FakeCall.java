package io.github.panghy.openai.service;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class FakeCall<T> implements Call<T> {

  private final T single;

  public FakeCall(T single) {
    this.single = single;
  }

  @Override
  public Response<T> execute() throws IOException {
    return Response.success(single);
  }

  @Override
  public void enqueue(Callback<T> callback) {
    callback.onResponse(this, Response.success(single));
  }

  @Override
  public boolean isExecuted() {
    return true;
  }

  @Override
  public void cancel() {

  }

  @Override
  public boolean isCanceled() {
    return false;
  }

  @Override
  public Call<T> clone() {
    return this;
  }

  @Override
  public Request request() {
    return null;
  }

  @Override
  public Timeout timeout() {
    return Timeout.NONE;
  }
}
