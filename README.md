![Maven Central](https://img.shields.io/maven-central/v/com.theokanning.openai-gpt3-java/client?color=blue)
# OpenAI-Java
Java libraries for using OpenAI's GPT-3 api.

Includes the following artifacts:
- `api` : request/response POJOs for the GPT-3 engine, completion, and search APIs.
- `client` : a basic retrofit client for the GPT-3 endpoints, includes the `api` module

as well as an example project using the client.

## Supported APIs
- [Engines](https://beta.openai.com/docs/api-reference/engines)
- [Completions](https://beta.openai.com/docs/api-reference/completions)
- [Edits](https://beta.openai.com/docs/api-reference/edits)
- [Embeddings](https://beta.openai.com/docs/api-reference/embeddings)
- [Files](https://beta.openai.com/docs/api-reference/files)
- [Fine-tunes](https://beta.openai.com/docs/api-reference/fine-tunes)
- [Moderations](https://beta.openai.com/docs/api-reference/moderations)

#### Deprecated by OpenAI but still working as of 8/19/22
- [Searches](https://beta.openai.com/docs/api-reference/searches)
- [Classifications](https://beta.openai.com/docs/api-reference/classifications)
- [Answers](https://beta.openai.com/docs/api-reference/answers)

## Usage

### Importing into a gradle project
`implementation 'com.theokanning.openai-gpt3-java:api:<version>'`  
or   
`implementation 'com.theokanning.openai-gpt3-java:client:<version>'`

### Using OpenAiService
If you're looking for the fastest solution, import the `client` and use [OpenAiService](client/src/main/java/com/theokanning/openai/OpenAiService.java).
```
OpenAiService service = new OpenAiService(your_token)
CompletionRequest completionRequest = CompletionRequest.builder()
        .prompt("Somebody once told me the world is gonna roll me")
        .echo(true)
        .build();
service.createCompletion("ada", completionRequest).getChoices().forEach(System.out::println);
```

### Using OpenAiApi Retrofit client
If you're using retrofit, you can import the `client` module and use the [OpenAiApi](client/src/main/java/com/theokanning/openai/OpenAiApi.java).  
You'll have to add your auth token as a header (see [AuthenticationInterceptor](client/src/main/java/com/theokanning/openai/AuthenticationInterceptor.java))
and set your converter factory to use snake case and only include non-null fields.

### Using data classes only
If you want to make your own client, just import the POJOs from the `api` module.  
Your client will need to use snake case to work with the OpenAI API.

## Running the example project
All the [example](example/src/main/java/example/OpenAiApiExample.java) project requires is your OpenAI api token
```
export OPENAI_TOKEN="sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
./gradlew example:run
```

## License
Published under the MIT License
