package io.github.panghy.openai;

/**
 * OkHttp Interceptor that adds an authorization token header
 * 
 * @deprecated Use {@link io.github.panghy.openai.client.AuthenticationInterceptor}
 */
@Deprecated
public class AuthenticationInterceptor extends io.github.panghy.openai.client.AuthenticationInterceptor {

    AuthenticationInterceptor(String token) {
        super(token);
    }

}
