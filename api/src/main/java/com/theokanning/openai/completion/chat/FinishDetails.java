package com.theokanning.openai.completion.chat;

import lombok.Data;

/**
 * finish_details
 *
 * @author cong
 * @since 2023/12/3
 */
@Data
public class FinishDetails {

    /**
     * The reason why GPT stopped generating, for example "stop", "max_tokens".
     */
    private String type;

    /**
     * For example "<|fim_suffix|>"
     */
    private String stop;
}
