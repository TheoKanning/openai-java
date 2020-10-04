package openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import openai.completion.CompletionRequest;
import openai.completion.CompletionResult;
import openai.engine.Engine;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class OpenAiService {

    OpenAiApi api;

    public OpenAiService(String token) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(token))
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        api = retrofit.create(OpenAiApi.class);
    }

    public List<Engine> getEngines() {
        return api.getEngines().blockingGet().data;
    }

    public Engine getEngine(String engineId) {
        return api.getEngine(engineId).blockingGet();
    }

    public CompletionResult createCompletion(String engineId, CompletionRequest request) {
        return api.createCompletion(engineId, request).blockingGet();
    }
}
