package io.github.panghy.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Common options when getting a list of objects
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListSearchParameters {
    /**
     * A limit on the number of objects to be returned.
     * Limit can range between 1 and 100, and the default is 20
     */

    Integer limit;

    /**
     * Sort order by the 'created_at' timestamp of the objects.
     * 'asc' for ascending order and 'desc' for descending order.
     */
    Order order;

    /**
     * A cursor for use in pagination. after is an object ID that defines your place in the list.
     * For instance, if you make a list request and receive 100 objects, ending with obj_foo,
     * your subsequent call can include after=obj_foo in order to fetch the next page of the list
     */
    String after;

    /**
     * A cursor for use in pagination. before is an object ID that defines your place in the list.
     * For instance, if you make a list request and receive 100 objects, ending with obj_foo,
     * your subsequent call can include before=obj_foo in order to fetch the previous page of the list.
     */
    String before;

    public enum Order {
        @JsonProperty("asc")
        ASCENDING,

        @JsonProperty("desc")
        DESCENDING
    }
}
