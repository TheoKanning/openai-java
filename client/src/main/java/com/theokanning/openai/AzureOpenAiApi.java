package com.theokanning.openai;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.file.File;
import com.theokanning.openai.finetune.FineTuneRequest;
import com.theokanning.openai.finetune.FineTuneResult;
import com.theokanning.openai.model.Model;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface AzureOpenAiApi{
    @GET("/openai/models")
    Single<OpenAiResponse<Model>> listModels(@Query("api-version") String apiVersion);

    @GET("/openai/models/{model_id}")
    Single<Model> getModel(@Path("model_id") String modelId,
                           @Query("api-version") String apiVersion);

    @POST("/openai/deployments/{deployment_id}/completions")
    Single<CompletionResult> createCompletion(@Body CompletionRequest request,
                                              @Path("deployment_id") String deploymentId,
                                              @Query("api-version") String apiVersion);

    @Streaming
    @POST("/openai/deployments/{deployment_id}/completions")
    Call<ResponseBody> createCompletionStream(@Body CompletionRequest request,
                                              @Path("deployment_id") String deploymentId,
                                              @Query("api-version") String apiVersion);

    @POST("/openai/deployments/{deployment_id}/chat/completions")
    Single<ChatCompletionResult> createChatCompletion(@Body ChatCompletionRequest request,
                                                      @Path("deployment_id") String deploymentId,
                                                      @Query("api-version") String apiVersion);

    @Streaming
    @POST("/openai/deployments/{deployment_id}/chat/completions")
    Call<ResponseBody> createChatCompletionStream(@Body ChatCompletionRequest request,
                                                  @Path("deployment_id") String deploymentId,
                                                  @Query("api-version") String apiVersion);

    @GET("/openai/files")
    Single<OpenAiResponse<File>> listFiles(@Query("api-version") String apiVersion);

    @Multipart
    @POST("/openai/files")
    Single<File> uploadFile(@Part("purpose") RequestBody purpose,
                            @Part MultipartBody.Part file,
                            @Query("api-version") String apiVersion);

    @GET("/openai/files/{file_id}")
    Single<File> retrieveFile(@Path("file_id") String fileId,
                              @Query("api-version") String apiVersion);

    @POST("/openai/fine-tunes")
    Single<FineTuneResult> createFineTune(@Body FineTuneRequest request,
                                          @Query("api-version") String apiVersion);

    @GET("/openai/fine-tunes/{fine_tune_id}")
    Single<FineTuneResult> retrieveFineTune(@Path("fine_tune_id") String fineTuneId,
                                            @Query("api-version") String apiVersion);

    @POST("/openai/deployments/{deployment_id}/embeddings")
    Single<EmbeddingResult> createEmbeddings(@Body EmbeddingRequest request,
                                             @Path("deployment_id") String deploymentId,
                                             @Query("api-version") String apiVersion);
}
