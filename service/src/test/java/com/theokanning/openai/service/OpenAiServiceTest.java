package com.theokanning.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.CompletionResult;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.junit.jupiter.api.Assertions.*;

public class OpenAiServiceTest {

    @Test
    void assertTokenNotNull() {
        String token = null;
        assertThrows(NullPointerException.class, () -> new OpenAiService(token));
    }

    @Test
    void executeHappyPath() {
        CompletionResult expected = new CompletionResult();
        Single<CompletionResult> single = Single.just(expected);

        CompletionResult actual = OpenAiService.execute(single);
        assertEquals(expected, actual);
    }

    @Test
    void executeParseHttpError() throws JsonProcessingException{
        String errorBody = "{\"error\":{\"message\":\"Invalid auth token\",\"type\":\"type\",\"param\":\"param\",\"code\":\"code\"}}";
        HttpException httpException = createException(errorBody, 401);
        Single<CompletionResult> single = Single.error(httpException);

        OpenAiHttpException exception = assertThrows(OpenAiHttpException.class, () -> OpenAiService.execute(single));

        assertEquals("Invalid auth token", exception.getMessage());
        assertEquals("type", exception.type);
        assertEquals("param", exception.param);
        assertEquals("code", exception.code);
        assertEquals(401, exception.statusCode);
    }

    @Test
    void executeParseUnknownProperties() {
        // error body contains one unknown property and no message
        String errorBody = "{\"error\":{\"unknown\":\"Invalid auth token\",\"type\":\"type\",\"param\":\"param\",\"code\":\"code\"}}";
        HttpException httpException = createException(errorBody, 401);
        Single<CompletionResult> single = Single.error(httpException);

        OpenAiHttpException exception = assertThrows(OpenAiHttpException.class, () -> OpenAiService.execute(single));
        assertNull(exception.getMessage());
        assertEquals("type", exception.type);
        assertEquals("param", exception.param);
        assertEquals("code", exception.code);
        assertEquals(401, exception.statusCode);
    }

    @Test
    void executeNullErrorBodyThrowOriginalError() {
        // exception with a successful response creates an error without an error body
        HttpException httpException = new HttpException(Response.success(new CompletionResult()));
        Single<CompletionResult> single = Single.error(httpException);

        HttpException exception = assertThrows(HttpException.class, () -> OpenAiService.execute(single));
    }

    private HttpException createException(String errorBody, int code) {
        ResponseBody body = ResponseBody.create(MediaType.get("application/json"), errorBody);
        Response<Void> response = Response.error(code, body);
        return new HttpException(response);
    }
}
