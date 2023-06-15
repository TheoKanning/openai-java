
# 🚀 Publishing to Local Maven Repository (⚠ Unofficial Version with Function Support)

This repository is a fork of the original [com.theokanning.openai-gpt3-java](https://github.com/TheoKanning/openai-java)
. While the changes in this fork are awaiting to be pulled into the official repository, you can use this unofficial
version by installing it to your local Maven repository.

This allows you to use the modified version "0.12.0-20240614" while retaining the same dependency group and artifact
names, i.e., the group is still "com.theokanning.openai-gpt3-java" and the artifacts are "api", "client", and "service".

### Installation Instructions

#### For Linux/macOS

1. After cloning or downloading this repository, navigate to its root directory.
2. Make sure that the `publishToLocal.sh` script is executable. You can set the execute permission by running the
   following command:

```sh
chmod +x publishToLocal.sh
```

3. Now, execute the script:

```sh
./publishToLocal.sh
```

#### For Windows

1. After cloning or downloading this repository, navigate to its root directory.
2. Execute the batch script by running the following command:

```batch
publishToLocal.bat
```

### Dependency Configuration

Once the unofficial version is installed to your local Maven repository, you can use it in your project by adding the
following dependencies in your build file (e.g., `pom.xml` for Maven, `build.gradle` for Gradle):

For Maven:

```xml

<dependency>
    <groupId>com.theokanning.openai-gpt3-java</groupId>
    <artifactId>api|client|service</artifactId>
    <version>0.12.0-20240614</version>
</dependency>
```

For Gradle:

```gradle
implementation 'com.theokanning.openai-gpt3-java:api|client|service:0.12.0-20240614'
```

This will allow you to use the modified version of the library with the changes made in this fork.

--------------------------------------------------

![Maven Central](https://img.shields.io/maven-central/v/com.theokanning.openai-gpt3-java/client?color=blue)

> ⚠️ Please switch to using the new 'service' library if you need to use OpenAiService. The old 'client' OpenAiService
> is deprecated as of 0.10.0.  
> ⚠️OpenAI has deprecated all Engine-based APIs.
> See [Deprecated Endpoints](https://github.com/TheoKanning/openai-java#deprecated-endpoints) below for more info.

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

### Streaming thread shutdown
If you want to shut down your process immediately after streaming responses, call `OpenAiService.shutdown()`.  
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

## FAQ
### Does this support GPT-4?
Yes! GPT-4 uses the ChatCompletion Api, and you can see the latest model options [here](https://platform.openai.com/docs/models/gpt-4).  
GPT-4 is currently in a limited beta (as of 4/1/23), so make sure you have access before trying to use it.

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
