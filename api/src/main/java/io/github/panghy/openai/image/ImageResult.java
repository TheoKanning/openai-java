package io.github.panghy.openai.image;

import lombok.Data;

import java.util.List;

/**
 * An object with a list of image results.
 *
 * https://beta.openai.com/docs/api-reference/images
 */
@Data
public class ImageResult {

    /**
     * The creation time in epoch seconds.
     */
    Long created;

    /**
     * List of image results.
     */
    List<Image> data;
}
