# How to Contribute

## How to Add a New API

### Add POJOs to API library
I usually have ChatGPT write them for me by copying and pasting from teh OpenAI API reference ([example chat](https://chat.openai.com/share/af48ef11-0354-40b2-a8e2-3bf8e93a94a3)), but double check everything because Chat always makes mistakes, especially around adding `@JsonProperty` annotations.

- Make all java variables camel case, and use `@JsonProperty` for fields that OpenAI returns as snake case
- Include comments for each variable, I take these directly from the OpenAI website
- Include `@Data` on every response class, and `@Builder @NoArgsConstructor @AllArgsConstructor @Data` on every request
- Include basic class-level documentation and a link to the OpenAI reference page, [example](api/src/main/java/com/theokanning/openai/threads/Thread.java)
- Add a JSON test for every new java object, this ensures that your definition and variable name overrides are correct.
  - Copy the sample response from OpenAI into an api test [fixture](api/src/test/resources/fixtures)
  - Add any missing fields to the JSON file (OpenAI doesn't always include everything)
  - Add the class name to the test cases here [JSON test](api/src/test/java/com/theokanning/openai/JsonTest.java)

### Add to [OpenAiApi](client/src/main/java/com/theokanning/openai/client/OpenAiApi.java)
This is usually straightforward, use [OpenAiResponse](api/src/main/java/com/theokanning/openai/OpenAiResponse.java) for endpoints that return lists.

### Add to [OpenAiService](service/src/main/java/com/theokanning/openai/OpenAiService.java)

### Add an Integration Test
Since 99% of the work of this library is done on OpenAI's servers, the objective of these tests is to call each endpoint at least once.  
Specify every available parameter to make sure that OpenAI accepts everything, but don't create extra test cases unless a parameter drastically affects the results.  
For example, [CompletionTest](service/src/test/java/com/theokanning/openai/service/CompletionTest.java) has one test for normal completions, and one for streaming.

If your test relies on creating and retrieving external resources, [FineTuningTest](service/src/test/java/com/theokanning/openai/service/FineTuningTest.java) is a good example of how to share resources between tests and clean up afterwards.