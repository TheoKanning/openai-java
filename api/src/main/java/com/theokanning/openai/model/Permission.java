package com.theokanning.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Model permissions
 * I couldn't find documentation for the specific permissions, and I've elected to leave them undocumented rather than
 * write something incorrect.
 *
 * https://beta.openai.com/docs/api-reference/models
 */
@Data
public class Permission {
    /**
     * An identifier for this model permission
     */
    public String id;

    /**
     * The type of object returned, should be "model_permission"
     */
    public String object;

    /**
     * The creation time in epoch seconds.
     */
    public long created;

    @JsonProperty("allow_create_engine")
    public boolean allowCreateEngine;

    @JsonProperty("allow_sampling")
    public boolean allowSampling;

    @JsonProperty("allow_log_probs")
    public boolean allowLogProbs;

    @JsonProperty("allow_search_indices")
    public boolean allowSearchIndices;

    @JsonProperty("allow_view")
    public boolean allowView;

    @JsonProperty("allow_fine_tuning")
    public boolean allowFineTuning;

    public String organization;

    public String group;

    @JsonProperty("is_blocking")
    public boolean isBlocking;

}
