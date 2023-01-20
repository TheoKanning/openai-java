package com.theokanning.openai;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp Interceptor that adds an authorization organizationId header
 */
public class OrganizationInterceptor implements Interceptor {

    private final String organizationId;

    OrganizationInterceptor(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
      if (organizationId ==  null) {
        return chain.proceed(chain.request());
      }
        Request request = chain.request()
            .newBuilder()
            .header("OpenAI-Organization", organizationId)
            .build();
        return chain.proceed(request);
    }
}
