package io.github.panghy.openai.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * A file uploaded to OpenAi
 *
 * https://beta.openai.com/docs/api-reference/files
 */
@Data
@Builder
@Jacksonized
public class File {

    /**
     * The unique id of this file.
     */
    String id;

    /**
     * The type of object returned, should be "file".
     */
    String object;

    /**
     * File size in bytes.
     */
    Long bytes;

    /**
     * The creation time in epoch seconds.
     */
    @JsonProperty("created_at")
    Long createdAt;

    /**
     * The name of the file.
     */
    String filename;

    /**
     * Description of the file's purpose.
     */
    String purpose;

    /**
     * The current status of the file, which can be either uploaded, processed, pending, error, deleting or deleted.
     */
    String status;

    /**
     * Additional details about the status of the file.
     * If the file is in the error state, this will include a message describing the error.
     */
    @JsonProperty("status_details")
    String statusDetails;
}
