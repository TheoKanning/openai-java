package com.theokanning.openai.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * An object containing the flags for each moderation category
 *
 * https://beta.openai.com/docs/api-reference/moderations/create
 */
@Data
public class ModerationCategories {

    public boolean hate;

    @JsonProperty("hate/threatening")
    public boolean hateThreatening;

    public boolean harassment;

    @JsonProperty("harassment/threatening")
    public boolean harassment/threatening;

    @JsonProperty("self-harm")
    public boolean selfHarm;

    @JsonProperty("self-harm/intent")
    public boolean selfHarmIntent;

    @JsonProperty("self-harm/instructions")
    public boolean selfHarmInstructions;

    public boolean sexual;

    @JsonProperty("sexual/minors")
    public boolean sexualMinors;

    public boolean violence;

    @JsonProperty("violence/graphic")
    public boolean violenceGraphic;
}
