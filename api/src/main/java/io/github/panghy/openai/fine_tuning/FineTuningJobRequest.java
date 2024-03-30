package io.github.panghy.openai.fine_tuning;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


/**
 * Request to create a fine tuning job
 * https://platform.openai.com/docs/api-reference/fine-tuning/create
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FineTuningJobRequest {

    /**
     * The ID of an uploaded file that contains training data.
     */
    @NonNull
    @JsonProperty("training_file")
    String trainingFile;

    /**
     * The ID of an uploaded file that contains validation data.
     * Optional.
     */
    @JsonProperty("validation_file")
    String validationFile;

    /**
     * The name of the model to fine-tune.
     */
    @NonNull
    String model;

    /**
     * The hyperparameters used for the fine-tuning job.
     */
    Hyperparameters hyperparameters;

    /**
     * A string of up to 40 characters that will be added to your fine-tuned model name.
     */
    String suffix;
}
