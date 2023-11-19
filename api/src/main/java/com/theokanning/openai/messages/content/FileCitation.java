package com.theokanning.openai.messages.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A citation within the message that points to a specific quote from a specific File associated with the
 * assistant or the message. Generated when the assistant uses the "retrieval" tool to search files.
 * <p>
 * https://platform.openai.com/docs/api-reference/messages/object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileCitation {

    /**
     * The ID of the specific File the citation is from.
     */
    @JsonProperty("file_id")
    String fileId;

    /**
     * The specific quote in the file.
     */
    String quote;
}
