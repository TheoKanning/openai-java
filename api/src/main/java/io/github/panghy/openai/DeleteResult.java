package io.github.panghy.openai;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * A response when deleting an object
 */
@Data
@Builder
@Jacksonized
public class DeleteResult {
    /**
     * The id of the object.
     */
    String id;

    /**
     * The type of object deleted, for example "file" or "model"
     */
    String object;

    /**
     * True if successfully deleted
     */
    boolean deleted;
}
