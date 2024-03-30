package io.github.panghy.openai.fine_tuning;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * An object representing an event in the lifecycle of a fine-tuning job
 *
 * https://platform.openai.com/docs/api-reference/fine-tuning/list-events
 */
@Data
@Builder
@Jacksonized
public class FineTuningEvent {
    /**
     * The type of object returned, should be "fine-tuneing.job.event".
     */
    String object;

    /**
     * The ID of the fine-tuning event.
     */
    String id;

    /**
     * The creation time in epoch seconds.
     */
    @JsonProperty("created_at")
    Long createdAt;

    /**
     * The log level of this message.
     */
    String level;

    /**
     * The event message.
     */
    String message;

    /**
     * The type of event, i.e. "message"
     */
    String type;
}
