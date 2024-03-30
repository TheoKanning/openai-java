package io.github.panghy.openai.image;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * An object with a list of image results.
 *
 * https://beta.openai.com/docs/api-reference/images
 */
@Data
@Builder
@Jacksonized
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
