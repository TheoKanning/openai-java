![version](https://img.shields.io/bintray/v/theokanning/openai-gpt3-java/api?color=00bb00&style=flat-square)
# OpenAI-Java
Java libraries for using OpenAI's GPT-3 api.

Includes the following artifacts:
- `api` : request/response POJOs for the GPT-3 engine, completion, and search APIs.
- `client` : a basic retrofit client for the GPT-3 endpoints, includes the `api` module

as well as an example project using the client.

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
