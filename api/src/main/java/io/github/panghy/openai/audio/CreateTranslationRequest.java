package io.github.panghy.openai.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * A request for OpenAi to create English translation based on an audio file
 * All fields except model are optional
 *
 * https://platform.openai.com/docs/api-reference/audio/create
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTranslationRequest {

    /**
     * The name of the model to use.
     */
    @NonNull
    String model;

    /**
     * An optional text to guide the model's style or continue a previous audio segment. The prompt should be in English.
     */
    String prompt;

    /**
     * The format of the translated output, in one of these options: json or verbose_json
     */
    @JsonProperty("response_format")
    String responseFormat;

    /**
     * The sampling temperature, between 0 and 1.
     * Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic.
     * If set to 0, the model will use log probability to automatically increase the temperature until certain thresholds are hit.
     */
    Double temperature;
}
