package openai;

import io.reactivex.Single;
import openai.engine.Engine;
import retrofit2.http.GET;

public interface OpenAiApi {

    @GET("v1/engines")
    Single<OpenAiResponse<Engine>> getEngines();
}
