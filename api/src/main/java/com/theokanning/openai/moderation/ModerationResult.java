package com.theokanning.openai.moderation;

import lombok.Data;

import java.util.List;

/**
 * An object containing a response from the moderation api
 *
 * https://beta.openai.com/docs/api-reference/moderations/create
 */
@Data
public class ModerationResult {
    /**
     * A unique id assigned to this moderation.
     */
    String id;

    /**
     * The GPT-3 model used.
     */
    String model;

    /**
     * A list of moderation scores.
     */
    List<Moderation> results;
}
