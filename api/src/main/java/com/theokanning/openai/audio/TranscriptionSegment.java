package com.theokanning.openai.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * An object represents transcription segment
 *
 * https://platform.openai.com/docs/api-reference/audio/create
 */
@Data
public class TranscriptionSegment {

    Integer id;
    Integer seek;
    Double start;
    Double end;
    String text;
    List<Integer> tokens;
    Double temperature;
    @JsonProperty("avg_logprob")
    Double averageLogProb;
    @JsonProperty("compression_ratio")
    Double compressionRatio;
    @JsonProperty("no_speech_prob")
    Double noSpeechProb;
    @JsonProperty("transient")
    Boolean transientFlag;

}
