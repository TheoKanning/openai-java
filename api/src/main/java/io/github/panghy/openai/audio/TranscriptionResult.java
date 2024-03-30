package io.github.panghy.openai.audio;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * An object with the text transcription
 *
 * https://platform.openai.com/docs/api-reference/audio/create
 */
@Data
@Builder
@Jacksonized
public class TranscriptionResult {

    /**
     * The text transcription.
     */
    String text;

    /**
     * Task name
     * @apiNote verbose_json response format only
     */
    String task;

    /**
     * Speech language
     * @apiNote verbose_json response format only
     */
    String language;

    /**
     * Speech duration
     * @apiNote verbose_json response format only
     */
    Double duration;

    /**
     * List of segments
     * @apiNote verbose_json response format only
     */
    List<TranscriptionSegment> segments;

}
