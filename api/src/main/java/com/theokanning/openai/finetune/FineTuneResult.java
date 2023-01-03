package com.theokanning.openai.finetune;

import com.theokanning.openai.file.File;
import lombok.Data;

import java.util.List;

/**
 * An object describing a fine-tuned model. Returned by multiple fine-tune requests.
 *
 * https://beta.openai.com/docs/api-reference/fine-tunes
 */
@Data
public class FineTuneResult {
    /**
     * The ID of the fine-tuning job.
     */
    String id;

    /**
     * The type of object returned, should be "fine-tune".
     */
    String object;

    /**
     * The name of the base model.
     */
    String model;

    /**
     * The creation time in epoch seconds.
     */
    Long createdAt;

    /**
     * List of events in this job's lifecycle. Null when getting a list of fine-tune jobs.
     */
    List<FineTuneEvent> events;

    /**
     * The ID of the fine-tuned model, null if tuning job is not finished.
     * This is the id used to call the model.
     */
    String fineTunedModel;

    /**
     * The specified hyper-parameters for the tuning job.
     */
    HyperParameters hyperparams;

    /**
     * The ID of the organization this model belongs to.
     */
    String organizationId;

    /**
     * Result files for this fine-tune job.
     */
    List<File> resultFiles;

    /**
     * The status os the fine-tune job. "pending", "succeeded", or "cancelled"
     */
    String status;

    /**
     * Training files for this fine-tune job.
     */
    List<File> trainingFiles;

    /**
     * The last update time in epoch seconds.
     */
    Long updatedAt;

    /**
     * Validation files for this fine-tune job.
     */
    List<File> validationFiles;
}
