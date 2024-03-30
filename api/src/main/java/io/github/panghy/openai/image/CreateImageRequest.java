package io.github.panghy.openai.image;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

/**
 * A request for OpenAi to create an image based on a prompt
 * All fields except prompt are optional
 *
 * https://beta.openai.com/docs/api-reference/images/create
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateImageRequest {

    /**
     * A text description of the desired image(s). The maximum length is 1000 characters for dall-e-2 and 4000 characters for dall-e-3.
     */
    @NonNull
    String prompt;

    /**
     * The model to use for image generation. Defaults to "dall-e-2".
     */
    String model;

    /**
     * The number of images to generate. Must be between 1 and 10. For dall-e-3, only n=1 is supported. Defaults to 1.
     */
    Integer n;

    /**
     * The quality of the image that will be generated. "hd" creates images with finer details and greater consistency across the image. This param is only supported for dall-e-3. Defaults to "standard".
     */
    String quality;

    /**
     * The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024 for dall-e-2. Must be one of 1024x1024, 1792x1024, or 1024x1792 for dall-e-3 models. Defaults to 1024x1024.
     */
    String size;

    /**
     * The format in which the generated images are returned. Must be one of url or b64_json. Defaults to url.
     */
    @JsonProperty("response_format")
    String responseFormat;

    /**
     * The style of the generated images. Must be one of vivid or natural. Vivid causes the model to lean towards generating hyper-real and dramatic images. Natural causes the model to produce more natural, less hyper-real looking images. This param is only supported for dall-e-3. Defaults to vivid.
     */
    String style;

    /**
     * A unique identifier representing your end-user, which will help OpenAI to monitor and detect abuse.
     */
    String user;
}
