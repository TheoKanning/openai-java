package com.theokanning.openai.audio;

import lombok.Data;

import java.util.List;

/**
 * An object with the text transcription
 *
 * https://platform.openai.com/docs/api-reference/audio/create
 */
@Data
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
