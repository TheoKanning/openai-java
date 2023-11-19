package com.theokanning.openai.completion.chat;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ImageUrl {

    /**
     * Either a URL of the image or the base64 encoded image data.
     */
    @NonNull
    private String url;

    /**
     * Specifies the detail level of the image. Learn more in the
     * <a href="https://platform.openai.com/docs/guides/vision/low-or-high-fidelity-image-understanding">
     * Vision guide</a>.
     */
    private String detail;
}