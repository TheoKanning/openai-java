package io.github.panghy.openai.messages.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An annotation for a text Message
 * <p>
 * https://platform.openai.com/docs/api-reference/messages/object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Annotation {
    /**
     * The type of annotation, either file_citation or file_path
     */
    String type;

    /**
     * The text in the message content that needs to be replaced
     */
    String text;

    /**
     * File citation details, only present when type == file_citation
     */
    @JsonProperty("file_citation")
    FileCitation fileCitation;

    /**
     * File path details, only present when type == file_path
     */
    @JsonProperty("file_path")
    FilePath filePath;

    @JsonProperty("start_index")
    int startIndex;

    @JsonProperty("end_index")
    int endIndex;
}
