package com.theokanning.openai.messages.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A URL for the file that's generated when the assistant used the code_interpreter tool to generate a file.
 * <p>
 * https://platform.openai.com/docs/api-reference/messages/object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilePath {

    /**
     * The ID of the file that was generated
     */
    @JsonProperty("file_id")
    String fileId;
}
