package io.github.panghy.openai.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * An object containing the scores for each moderation category
 *
 * https://beta.openai.com/docs/api-reference/moderations/create
 */
@Data
@Builder
@Jacksonized
public class ModerationCategoryScores {

    public double hate;

    @JsonProperty("hate/threatening")
    public double hateThreatening;

    @JsonProperty("self-harm")
    public double selfHarm;

    public double sexual;

    @JsonProperty("sexual/minors")
    public double sexualMinors;

    public double violence;

    @JsonProperty("violence/graphic")
    public double violenceGraphic;
}
