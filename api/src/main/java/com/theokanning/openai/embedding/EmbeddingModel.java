package com.theokanning.openai.embedding;

/**
 * Enum to represent the available OpenAI embedding models
 */
public enum EmbeddingModel {

  TEXT_EMBEDDING_ADA_002("text-embedding-ada-002", 2),
  TEXT_EMBEDDING_V3_SMALL("text-embedding-3-small", 3),
  TEXT_EMBEDDING_V3_LARGE("text-embedding-3-large", 3),
  ;

  private final String modelName;
  private final Integer generation;

  /**
   * Constructor.
   *
   * @param modelName the value of the enum
   */
  EmbeddingModel(String modelName, Integer generation) {
    this.modelName = modelName;
    this.generation = generation;
  }

  /**
   * Method to return enum from a string value.
   *
   * @param value the value to convert to an enum
   * @return the enum value
   */
  public static EmbeddingModel fromValue(String value) {
    for (EmbeddingModel b : EmbeddingModel.values()) {
      if (b.modelName.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  /**
   * Method to return the value of the enum.
   *
   * @return the value of the enum
   */
  public Integer getGeneration() {
    return generation;
  }

  /**
   * Method to return the value of the enum.
   *
   * @return the value of the enum
   */
  @Override
  public String toString() {
    return modelName;
  }
}
