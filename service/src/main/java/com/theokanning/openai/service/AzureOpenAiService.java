package com.theokanning.openai.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.theokanning.openai.*;
import com.theokanning.openai.completion.CompletionChunk;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.file.File;
import com.theokanning.openai.finetune.FineTuneRequest;
import com.theokanning.openai.finetune.FineTuneResult;
import com.theokanning.openai.model.Model;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class AzureOpenAiService {
    private static final ObjectMapper mapper = defaultObjectMapper();

    private final AzureOpenAiApi api;

    private final String deployment;

    private final ExecutorService executorService;

    /**
     * Creates a new AzureOpenAiService that wraps AzureOpenAiApi
     *
     * @param token   Azure OpenAI key
     * @param baseUrl Azure OpenAI endpoint
     * @param deployment Azure OpenAI deployment
     * @param timeout http read timeout, Duration.ZERO means no timeout
     */
    public AzureOpenAiService(
            final String token,
            final String baseUrl,
            final String deployment,
            final Duration timeout) {
        this.deployment = deployment;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Cache-Control", "no-cache")
                            .header("api-key", token)
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.api = retrofit.create(AzureOpenAiApi.class);
        this.executorService = client.dispatcher().executorService();
    }

    /**
     * @param apiVersion Azure OpenAI api version
     */
    public List<Model> listModels(String apiVersion) {
        return execute(api.listModels(apiVersion)).data;
    }

    public Model getModel(String modelId, String apiVersion) {
        return execute(api.getModel(modelId, apiVersion));
    }

    public CompletionResult createCompletion(CompletionRequest request, String apiVersion) {
        return execute(api.createCompletion(request, deployment, apiVersion));
    }

    public Flowable<CompletionChunk> streamCompletion(CompletionRequest request, String apiVersion) {
        request.setStream(true);

        return stream(api.createCompletionStream(request, deployment, apiVersion), CompletionChunk.class);
    }

    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request, String apiVersion) {
        return execute(api.createChatCompletion(request, deployment, apiVersion));
    }

    public Flowable<ChatCompletionChunk> streamChatCompletion(ChatCompletionRequest request, String apiVersion) {
        request.setStream(true);

        return stream(api.createChatCompletionStream(request, deployment, apiVersion), ChatCompletionChunk.class);
    }

    public EmbeddingResult createEmbeddings(EmbeddingRequest request, String apiVersion) {
        return execute(api.createEmbeddings(request, deployment, apiVersion));
    }

    public List<File> listFiles(String apiVersion) {
        return execute(api.listFiles(apiVersion)).data;
    }

    public File uploadFile(String purpose, String filepath, String apiVersion) {
        java.io.File file = new java.io.File(filepath);
        RequestBody purposeBody = RequestBody.create(okhttp3.MultipartBody.FORM, purpose);
        RequestBody fileBody = RequestBody.create(MediaType.parse("text"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filepath, fileBody);

        return execute(api.uploadFile(purposeBody, body, apiVersion));
    }

    public File retrieveFile(String fileId, String apiVersion) {
        return execute(api.retrieveFile(fileId, apiVersion));
    }

    public FineTuneResult createFineTune(FineTuneRequest request, String apiVersion) {
        return execute(api.createFineTune(request, apiVersion));
    }

    public FineTuneResult retrieveFineTune(String fineTuneId, String apiVersion) {
        return execute(api.retrieveFineTune(fineTuneId, apiVersion));
    }

    /**
     * Calls the Open AI api, returns the response, and parses error messages if the request fails
     */
    public static <T> T execute(Single<T> apiCall) {
        try {
            return apiCall.blockingGet();
        } catch (HttpException e) {
            e.printStackTrace();
            try {
                if (e.response() == null || e.response().errorBody() == null) {
                    throw e;
                }
                String errorBody = e.response().errorBody().string();

                OpenAiError error = mapper.readValue(errorBody, OpenAiError.class);
                throw new OpenAiHttpException(error, e, e.code());
            } catch (IOException ex) {
                // couldn't parse OpenAI error
                throw e;
            }
        }
    }

    /**
     * Calls the Open AI api and returns a Flowable of SSE for streaming
     * omitting the last message.
     *
     * @param apiCall The api call
     */
    public static Flowable<SSE> stream(retrofit2.Call<ResponseBody> apiCall) {
        return stream(apiCall, false);
    }

    /**
     * Calls the Open AI api and returns a Flowable of SSE for streaming.
     *
     * @param apiCall  The api call
     * @param emitDone If true the last message ([DONE]) is emitted
     */
    public static Flowable<SSE> stream(retrofit2.Call<ResponseBody> apiCall, boolean emitDone) {
        return Flowable.create(emitter -> apiCall.enqueue(new ResponseBodyCallback(emitter, emitDone)), BackpressureStrategy.BUFFER);
    }

    /**
     * Calls the Open AI api and returns a Flowable of type T for streaming
     * omitting the last message.
     *
     * @param apiCall The api call
     * @param cl      Class of type T to return
     */
    public static <T> Flowable<T> stream(Call<ResponseBody> apiCall, Class<T> cl) {
        return stream(apiCall).map(sse -> mapper.readValue(sse.getData(), cl));
    }

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper;
    }

    /**
     * Shuts down the OkHttp ExecutorService.
     * The default behaviour of OkHttp's ExecutorService (ConnectionPool)
     * is to shut down after an idle timeout of 60s.
     * Call this method to shut down the ExecutorService immediately.
     */
    public void shutdownExecutor() {
        Objects.requireNonNull(this.executorService, "executorService must be set in order to shut down");
        this.executorService.shutdown();
    }
}
