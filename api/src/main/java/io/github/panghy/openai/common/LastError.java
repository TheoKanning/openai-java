package io.github.panghy.openai.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: vacuity
 * @create: 2023-11-16 22:27
 **/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LastError {
    
    private String code;
    
    private String message;
}
