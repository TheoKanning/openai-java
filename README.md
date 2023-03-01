![Maven Central](https://img.shields.io/maven-central/v/com.theokanning.openai-gpt3-java/client?color=blue)

> ⚠️ Please switch to using the new 'service' library if you need to use OpenAiService. The old 'client' OpenAiService is deprecated as of 0.10.0.  
> ⚠️OpenAI has deprecated all Engine-based APIs. See [Deprecated Endpoints](https://github.com/TheoKanning/openai-java#deprecated-endpoints) below for more info.

# OpenAI-Java
Java libraries for using OpenAI's GPT-3 api.

Includes the following artifacts:
- `api` : request/response POJOs for the GPT-3 APIs.
- `client` : a basic retrofit client for the GPT-3 endpoints, includes the `api` module
- `service` : A basic service class that creates and calls the client. This is the easiest way to get started.

as well as an example project using the service.

## Supported APIs
- [Models](https://platform.openai.com/docs/api-reference/models)
- [Completions](https://platform.openai.com/docs/api-reference/completions)
- [Chat Completions](https://platform.openai.com/docs/api-reference/chat/create)
- [Edits](https://platform.openai.com/docs/api-reference/edits)
- [Embeddings](https://platform.openai.com/docs/api-reference/embeddings)
- [Files](https://platform.openai.com/docs/api-reference/files)
- [Fine-tunes](https://platform.openai.com/docs/api-reference/fine-tunes)
- [Images](https://platform.openai.com/docs/api-reference/images)
- [Moderations](https://platform.openai.com/docs/api-reference/moderations)

#### Deprecated by OpenAI
- [Engines](https://platform.openai.com/docs/api-reference/engines)

## Importing

### Gradle
`implementation 'com.theokanning.openai-gpt3-java:<api|client|service>:<version>'`

### Maven
```xml
   <dependency>
    <groupId>com.theokanning.openai-gpt3-java</groupId>
    <artifactId>{api|client|service}</artifactId>
    <version>version</version>       
   </dependency>
```

## Usage
### Data classes only
If you want to make your own client, just import the POJOs from the `api` module.
Your client will need to use snake case to work with the OpenAI API.

### Retrofit client
If you're using retrofit, you can import the `client` module and use the [OpenAiApi](client/src/main/java/com/theokanning/openai/OpenAiApi.java).  
You'll have to add your auth token as a header (see [AuthenticationInterceptor](client/src/main/java/com/theokanning/openai/AuthenticationInterceptor.java))
and set your converter factory to use snake case and only include non-null fields.

### OpenAiService
If you're looking for the fastest solution, import the `service` module and use [OpenAiService](client/src/main/java/com/theokanning/openai/OpenAiService.java).  

> ⚠️The OpenAiService in the client module is deprecated, please switch to the new version in the service module.
```
OpenAiService service = new OpenAiService("your_token");
CompletionRequest completionRequest = CompletionRequest.builder()
        .prompt("Somebody once told me the world is gonna roll me")
        .model("ada")
        .echo(true)
        .build();
service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
```

### Customizing OpenAiService
If you need to customize OpenAiService, create your own Retrofit client and pass it in to the constructor.
For example, do the following to add request logging (after adding the logging gradle dependency):
```
ObjectMapper mapper = defaultObjectMapper();
OkHttpClient client = defaultClient(token, timeout)
        .newBuilder()
        .interceptor(HttpLoggingInterceptor())
        .build();
Retrofit retrofit = defaultRetrofit(client, mapper);

OpenAiApi api = retrofit.create(OpenAiApi.class);
OpenAiService service = new OpenAiService(api);
```




## Running the example project
All the [example](example/src/main/java/example/OpenAiApiExample.java) project requires is your OpenAI api token
```
export OPENAI_TOKEN="sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
./gradlew example:run
```

## Deprecated Endpoints
OpenAI has deprecated engine-based endpoints in favor of model-based endpoints. 
For example, instead of using `v1/engines/{engine_id}/completions`, switch to `v1/completions` and specify the model in the `CompletionRequest`.
The code includes upgrade instructions for all deprecated endpoints.

I won't remove the old endpoints from this library until OpenAI shuts them down.

## License
Published under the MIT License
