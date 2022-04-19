package com.theokanning.openai.file;

import lombok.Data;

/**
 * A file uploaded to OpenAi
 *
 * https://beta.openai.com/docs/api-reference/files
 */
@Data
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
    Long createdAt;

    /**
     * The name of the file.
     */
    String filename;

    /**
     * Description of the file's purpose.
     */
    String purpose;
}
