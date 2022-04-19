package com.theokanning.openai.finetune;

import lombok.Data;

/**
 * Fine-tuning job hyperparameters
 *
 * https://beta.openai.com/docs/api-reference/fine-tunes
 */
@Data
public class HyperParameters {

    /**
     * The batch size to use for training.
     */
    String batchSize;

    /**
     * The learning rate multiplier to use for training.
     */
    Double learningRateMultiplier;

    /**
     * The number of epochs to train the model for.
     */
    Integer nEpochs;

    /**
     * The weight to use for loss on the prompt tokens.
     */
    Double promptLossWeight;
}
