package com.theokanning.openai.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSpeechRequest {

    /**
     * The name of the model to use.
     */
    @NonNull
    String model;

    /**
     * The text to generate audio for. The maximum length is 4096 characters.
     */
    @NonNull
    String input;

    /**
     * The voice to use when generating the audio.
     */
    @NonNull
    String voice;

    /**
     * The format to audio in. Supported formats are mp3, opus, aac, and flac. Defaults to mp3.
     */
    @JsonProperty("response_format")
    String responseFormat;

    /**
     * The speed of the generated audio. Select a value from 0.25 to 4.0. Defaults to 1.0.
     */
    Double speed;
}
