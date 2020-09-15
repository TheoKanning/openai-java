package openai;

import io.reactivex.Single;
import openai.engine.Engine;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OpenAiApi {

    @GET("v1/engines")
    Single<OpenAiResponse<Engine>> getEngines();

    @GET("/v1/engines/{engine_id}")
    Single<Engine> getEngine(@Path("engine_id") String engineId);
}
