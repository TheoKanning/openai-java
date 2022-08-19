package com.theokanning.openai.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.completion.CompletionChoice;
import lombok.Data;

import java.util.List;

/**
 * An object containing the flags for each moderation category
 *
 * https://beta.openai.com/docs/api-reference/moderations/create
 */
@Data
public class ModerationCategories {

    boolean hate;

    @JsonProperty("hate/threatening")
    boolean hateThreatening;

    @JsonProperty("self-harm")
    boolean selfHarm;

    boolean sexual;

    @JsonProperty("sexual/minors")
    boolean sexualMinors;

    boolean violence;

    @JsonProperty("violence/graphic")
    boolean violenceGraphic;
}
