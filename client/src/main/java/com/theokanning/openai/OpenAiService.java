package com.theokanning.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.theokanning.openai.classification.ClassificationRequest;
import com.theokanning.openai.classification.ClassificationResult;
import com.theokanning.openai.file.File;
import com.theokanning.openai.finetune.FineTuneRequest;
import com.theokanning.openai.finetune.FineTuneEvent;
import com.theokanning.openai.finetune.FineTuneResult;
import com.theokanning.openai.search.SearchRequest;
import okhttp3.*;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.engine.Engine;
import com.theokanning.openai.search.SearchResult;
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

    public List<SearchResult> search(String engineId, SearchRequest request) {
        return api.search(engineId, request).blockingGet().data;
    }

    public ClassificationResult createClassification(ClassificationRequest request) {
        return api.createClassification(request).blockingGet();
    }

    public List<File> listFiles() {
        return api.listFiles().blockingGet().data;
    }

    public File uploadFile(String purpose, String filepath) {
        java.io.File file = new java.io.File(filepath);
        RequestBody purposeBody = RequestBody.create(okhttp3.MultipartBody.FORM, purpose);
        RequestBody fileBody = RequestBody.create(MediaType.parse("text"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filepath, fileBody);

        return api.uploadFile(purposeBody, body).blockingGet();
    }

    public DeleteResult deleteFile(String fileId) {
        return api.deleteFile(fileId).blockingGet();
    }

    public File retrieveFile(String fileId) {
        return api.retrieveFile(fileId).blockingGet();
    }

    public FineTuneResult createFineTune(FineTuneRequest request) {
        return api.createFineTune(request).blockingGet();
    }

    public CompletionResult createFineTuneCompletion(CompletionRequest request) {
        return api.createFineTuneCompletion(request).blockingGet();
    }

    public List<FineTuneResult> listFineTunes() {
        return api.listFineTunes().blockingGet().data;
    }

    public FineTuneResult retrieveFineTune(String fineTuneId) {
        return api.retrieveFineTune(fineTuneId).blockingGet();
    }

    public FineTuneResult cancelFineTune(String fineTuneId) {
        return api.cancelFineTune(fineTuneId).blockingGet();
    }

    public List<FineTuneEvent> listFineTuneEvents(String fineTuneId) {
        return api.listFineTuneEvents(fineTuneId).blockingGet().data;
    }

    public DeleteResult deleteFineTune(String fineTuneId) {
        return api.deleteFineTune(fineTuneId).blockingGet();
    }
}
