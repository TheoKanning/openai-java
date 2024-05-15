package com.theokanning.openai.batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: acone.wu
 * @date: 2024/5/15 16:12
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BatchListRequest {

    /**
     * A cursor for use in pagination.
     * <p>
     * `after` is an object ID that defines your place in the list. For instance, if
     * you make a list request and receive 100 objects, ending with obj_foo, your
     * subsequent call can include after=obj_foo in order to fetch the next page of the
     * list.
     */
    private String after;

    /**
     * A limit on the number of objects to be returned.
     * <p>
     * Limit can range between 1 and 100, and the default is 20.
     */
    private Integer limit;
}
