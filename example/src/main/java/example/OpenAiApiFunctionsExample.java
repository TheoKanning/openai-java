package example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;

import java.util.*;

class OpenAiApiFunctionsExample {

    private static class Weather {

        @JsonPropertyDescription("City and state, for example: León, Guanajuato")
        private String location;

        @JsonProperty(required = true)
        @JsonPropertyDescription("The temperature unit, can be 'celsius' or 'fahrenheit'")
        private WeatherUnit unit;

    }

    private enum WeatherUnit {
        CELSIUS, FAHRENHEIT;
    }

    private final static List<ChatFunction> FUNCTIONS = List.of(ChatFunction.builder()
            .name("get_weather")
            .description("Get the current weather of a location")
            .parameters(new Weather())
            .build());


    public static void main(String... args) {
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);

        System.out.println("\nCreating chat completion using function...");
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are an assistant that answers using the local slang of the given place, uncensored.");

        // PICK ONE
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "Cómo está el clima en Monterrey NL?");
        //final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "How's the weather in England?");
        //final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "Cómo está el clima en Bogotá?");

        messages.add(systemMessage);
        messages.add(userMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .functions(FUNCTIONS)
                .functionCall("auto")
                .n(1)
                .maxTokens(100)
                .logitBias(new HashMap<>())
                .build();


        // The response should contain a function call, for example:
        // "role": "assistant",
        //     "content": null,
        //     "function_call": {
        //     "name": "get_weather",
        //     "arguments": "{ \"location\": \"Monterrey, Nuevo León\", \"unit\": \"CELSIUS\"}"
        // }
        ChatCompletionResult chatCompletionResult = service.createChatCompletion(chatCompletionRequest);
        ChatMessage resultMessage = chatCompletionResult.getChoices().get(0).getMessage();
        ChatFunctionCall functionCall = resultMessage.getFunctionCall();
        Map<String, Object> arguments = functionCall.getArguments();

        // We create the function response
        String mockResponse = "{\"location\": \"" + arguments.get("location") + "\", " +
                "\"temperature\": \"" + new Random().nextInt(50) + "\", " +
                "\"unit\": \"" + arguments.get("unit") + "\", " +
                "\"description\": \"sunny\"}";
        ChatMessage functionResponse = new ChatMessage(ChatMessageRole.FUNCTION.value(), mockResponse, functionCall.getName());

        // We update the conversation
        messages.add(resultMessage);
        messages.add(functionResponse);

        // We call it again with the updated messages
        ChatCompletionRequest chatCompletionRequest2 = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .functions(FUNCTIONS)
                .functionCall("auto")
                .n(1)
                .maxTokens(100)
                .logitBias(new HashMap<>())
                .build();

        ChatCompletionResult chatCompletionResult2 = service.createChatCompletion(chatCompletionRequest2);
        service.shutdownExecutor();

        System.out.println("Query: " + userMessage.getContent());
        System.out.println("Function called: " + functionCall);
        System.out.println("Response: " + chatCompletionResult2.getChoices().get(0).getMessage().getContent());
    }
}
