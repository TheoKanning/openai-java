package com.theokanning.openai.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: vacuity
 * @create: 2023-11-16 22:33
 **/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunImage {

    @JsonProperty("file_id")
    private String fileId;
}
