package openai;

import io.reactivex.Single;
import openai.completion.CompletionRequest;
import openai.completion.CompletionResult;
import openai.engine.Engine;
import openai.search.SearchRequest;
import openai.search.SearchResult;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OpenAiApi {

    @GET("v1/engines")
    Single<OpenAiResponse<Engine>> getEngines();

    @GET("/v1/engines/{engine_id}")
    Single<Engine> getEngine(@Path("engine_id") String engineId);

    @POST("/v1/engines/{engine_id}/completions")
    Single<CompletionResult> createCompletion(@Path("engine_id") String engineId, @Body CompletionRequest request);

    @POST("/v1/engines/{engine_id}/search")
    Single<OpenAiResponse<SearchResult>> search(@Path("engine_id") String engineId, @Body SearchRequest request);
}
