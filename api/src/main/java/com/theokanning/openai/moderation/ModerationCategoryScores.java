package com.theokanning.openai.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * An object containing the scores for each moderation category
 *
 * https://beta.openai.com/docs/api-reference/moderations/create
 */
@Data
public class ModerationCategoryScores {

    double hate;

    @JsonProperty("hate/threatening")
    double hateThreatening;

    @JsonProperty("self-harm")
    double selfHarm;

    double sexual;

    @JsonProperty("sexual/minors")
    double sexualMinors;

    double violence;

    @JsonProperty("violence/graphic")
    double violenceGraphic;
}
