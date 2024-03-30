package io.github.panghy.openai.fine_tuning;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Fine-tuning job
 * https://platform.openai.com/docs/api-reference/fine-tuning/object
 */
@Data
@Builder
@Jacksonized
public class FineTuningJob {
    /**
     * The object identifier, which can be referenced in the API endpoints.
     */
    String id;

    /**
     * The object type, which is always "fine_tuning.job".
     */
    String object;

    /**
     * The unix timestamp for when the fine-tuning job was created.
     */
    @JsonProperty("created_at")
    Long createdAt;

    /**
     * The unix timestamp for when the fine-tuning job was finished.
     */
    @JsonProperty("finished_at")
    Long finishedAt;

    /**
     * The base model that is being fine-tuned.
     */
    String model;

    /**
     * The name of the fine-tuned model that is being created.
     * Can be null if no fine-tuned model is created yet.
     */
    @JsonProperty("fine_tuned_model")
    String fineTunedModel;

    /**
     * The organization that owns the fine-tuning job.
     */
    @JsonProperty("organization_id")
    String organizationId;

    /**
     * The current status of the fine-tuning job.
     * Can be either created, pending, running, succeeded, failed, or cancelled.
     */
    String status;

    /**
     * The hyperparameters used for the fine-tuning job.
     * See the fine-tuning guide for more details.
     */
    Hyperparameters hyperparameters;

    /**
     * The file ID used for training.
     */
    @JsonProperty("training_file")
    String trainingFile;

    /**
     * The file ID used for validation.
     * Can be null if validation is not used.
     */
    @JsonProperty("validation_file")
    String validationFile;

    /**
     * The compiled results files for the fine-tuning job.
     */
    @JsonProperty("result_files")
    List<String> resultFiles;

    /**
     * The total number of billable tokens processed by this fine-tuning job.
     */
    @JsonProperty("trained_tokens")
    Integer trainedTokens;
}