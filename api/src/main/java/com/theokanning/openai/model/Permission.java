package com.theokanning.openai.model;

import lombok.Data;

/**
 * GPT-3 model permissions
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

    public boolean allowCreateEngine;

    public boolean allowSampling;

    public boolean allowLogProbs;

    public boolean allowSearchIndices;

    public boolean allowView;

    public boolean allowFineTuning;

    public String organization;

    public String group;

    public boolean isBlocking;

}
