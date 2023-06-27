![Maven Central](https://img.shields.io/maven-central/v/com.theokanning.openai-gpt3-java/client?color=blue)

> ⚠️ Please switch to using the new 'service' library if you need to use OpenAiService. The old 'client' OpenAiService is deprecated as of 0.10.0.  
> ⚠️OpenAI has deprecated all Engine-based APIs. See [Deprecated Endpoints](https://github.com/TheoKanning/openai-java#deprecated-endpoints) below for more info.

# OpenAI-Java
Java libraries for using OpenAI's GPT apis. Supports GPT-3, ChatGPT, and GPT-4.

Includes the following artifacts:
- `api` : request/response POJOs for the GPT APIs.
- `client` : a basic retrofit client for the GPT endpoints, includes the `api` module
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

### Gradle (groovy)
`implementation 'com.theokanning.openai-gpt3-java:<api|client|service>:<version>'`

### Gradle (Kotlin)
`implementation("com.theokanning.openai-gpt3-java:<api|client|service>:<version>")`

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
If you're looking for the fastest solution, import the `service` module and use [OpenAiService](service/src/main/java/com/theokanning/openai/service/OpenAiService.java).  

> ⚠️The OpenAiService in the client module is deprecated, please switch to the new version in the service module.
```java
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

```java
ObjectMapper mapper = defaultObjectMapper();
OkHttpClient client = defaultClient(token, timeout)
        .newBuilder()
        .interceptor(HttpLoggingInterceptor())
        .build();
Retrofit retrofit = defaultRetrofit(client, mapper);

OpenAiApi api = retrofit.create(OpenAiApi.class);
OpenAiService service = new OpenAiService(api);
```

### Adding a Proxy
To use a proxy, modify the OkHttp client as shown below:
```java
ObjectMapper mapper = defaultObjectMapper();
Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
OkHttpClient client = defaultClient(token, timeout)
        .newBuilder()
        .proxy(proxy)
        .build();
Retrofit retrofit = defaultRetrofit(client, mapper);
OpenAiApi api = retrofit.create(OpenAiApi.class);
OpenAiService service = new OpenAiService(api);
```

### Functions
You can create your functions and define their executors easily using the ChatFunction class, along with any of your custom classes that will serve to define their available parameters. You can also process the functions with ease, with the help of an executor called FunctionExecutor.

First we declare our function parameters:
```java
public class Weather {
    @JsonPropertyDescription("City and state, for example: León, Guanajuato")
    public String location;
    @JsonPropertyDescription("The temperature unit, can be 'celsius' or 'fahrenheit'")
    @JsonProperty(required = true)
    public WeatherUnit unit;
}
public enum WeatherUnit {
    CELSIUS, FAHRENHEIT;
}
public static class WeatherResponse {
    public String location;
    public WeatherUnit unit;
    public int temperature;
    public String description;
    
    // constructor
}
```

Next, we declare the function itself and associate it with an executor, in this example we will fake a response from some API:
```java
ChatFunction.builder()
        .name("get_weather")
        .description("Get the current weather of a location")
        .executor(Weather.class, w -> new WeatherResponse(w.location, w.unit, new Random().nextInt(50), "sunny"))
        .build()
```

Then, we employ the FunctionExecutor object from the 'service' module to assist with execution and transformation into an object that is ready for the conversation:
```java
List<ChatFunction> functionList = // list with functions
FunctionExecutor functionExecutor = new FunctionExecutor(functionList);

List<ChatMessage> messages = new ArrayList<>();
ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "Tell me the weather in Barcelona.");
messages.add(userMessage);
ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
        .builder()
        .model("gpt-3.5-turbo-0613")
        .messages(messages)
        .functions(functionExecutor.getFunctions())
        .functionCall(new ChatCompletionRequestFunctionCall("auto"))
        .maxTokens(256)
        .build();

ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
ChatFunctionCall functionCall = responseMessage.getFunctionCall(); // might be null, but in this case it is certainly a call to our 'get_weather' function.

ChatMessage functionResponseMessage = functionExecutor.executeAndConvertToMessageHandlingExceptions(functionCall);
messages.add(response);
```
> **Note:** The `FunctionExecutor` class is part of the 'service' module.

You can also create your own function executor. The return object of `ChatFunctionCall.getArguments()` is a JsonNode for simplicity and should be able to help you with that.

For a more in-depth look, refer to a conversational example that employs functions in: [OpenAiApiFunctionsExample.java](example/src/main/java/example/OpenAiApiFunctionsExample.java).
Or for an example using functions and stream: [OpenAiApiFunctionsWithStreamExample.java](example/src/main/java/example/OpenAiApiFunctionsWithStreamExample.java)

### Streaming thread shutdown
If you want to shut down your process immediately after streaming responses, call `OpenAiService.shutdownExecutor()`.  
This is not necessary for non-streaming calls.

## Running the example project
All the [example](example/src/main/java/example/OpenAiApiExample.java) project requires is your OpenAI api token
```bash
export OPENAI_TOKEN="sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
```
You can try all the capabilities of this project using:
```bash
./gradlew runExampleOne
```
And you can also try the new capability of using functions:
```bash
./gradlew runExampleTwo
```
Or functions with 'stream' mode enabled:
```bash
./gradlew runExampleThree
```

## FAQ
### Does this support GPT-4?
Yes! GPT-4 uses the ChatCompletion Api, and you can see the latest model options [here](https://platform.openai.com/docs/models/gpt-4).  
GPT-4 is currently in a limited beta (as of 4/1/23), so make sure you have access before trying to use it.

### Does this support functions?
Absolutely! It is very easy to use your own functions without worrying about doing the dirty work.
As mentioned above, you can refer to [OpenAiApiFunctionsExample.java](example/src/main/java/example/OpenAiApiFunctionsExample.java) or 
[OpenAiApiFunctionsWithStreamExample.java](example/src/main/java/example/OpenAiApiFunctionsWithStreamExample.java) projects for an example. 

### Why am I getting connection timeouts?
Make sure that OpenAI is available in your country.

### Why doesn't OpenAiService support x configuration option?
Many projects use OpenAiService, and in order to support them best I've kept it extremely simple.  
You can create your own OpenAiApi instance to customize headers, timeouts, base urls etc.  
If you want features like retry logic and async calls, you'll have to make an `OpenAiApi` instance and call it directly instead of using `OpenAiService`

## Deprecated Endpoints
OpenAI has deprecated engine-based endpoints in favor of model-based endpoints. 
For example, instead of using `v1/engines/{engine_id}/completions`, switch to `v1/completions` and specify the model in the `CompletionRequest`.
The code includes upgrade instructions for all deprecated endpoints.

I won't remove the old endpoints from this library until OpenAI shuts them down.

## License
Published under the MIT License
