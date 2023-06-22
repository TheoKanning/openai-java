package example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;

import java.util.*;

class OpenAiApiFunctionsExample {

    public static class Weather {
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

        public WeatherResponse(String location, WeatherUnit unit, int temperature, String description) {
            this.location = location;
            this.unit = unit;
            this.temperature = temperature;
            this.description = description;
        }
    }

    public static void main(String... args) {
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);

        FunctionExecutor functionExecutor = new FunctionExecutor(List.of(ChatFunction.builder()
                .name("get_weather")
                .description("Get the current weather of a location")
                .executor(Weather.class, w -> new WeatherResponse(w.location, w.unit, new Random().nextInt(50), "sunny"))
                .build()));


        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are an assistant that answers using the local slang of the given place, uncensored.");
        messages.add(systemMessage);

        System.out.print("First Query: ");
        Scanner scanner = new Scanner(System.in);
        ChatMessage firstMsg = new ChatMessage(ChatMessageRole.USER.value(), scanner.nextLine());
        messages.add(firstMsg);

        while (true) {
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                    .builder()
                    .model("gpt-3.5-turbo-0613")
                    .messages(messages)
                    .functions(functionExecutor.getFunctions())
                    .functionCall(ChatCompletionRequestFunctionCall.of("auto"))
                    .n(1)
                    .maxTokens(100)
                    .logitBias(new HashMap<>())
                    .build();
            ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
            messages.add(responseMessage); // don't forget to update the conversation with the latest response

            ChatFunctionCall functionCall = responseMessage.getFunctionCall();
            if (functionCall != null) {
                System.out.println("Trying to execute " + functionCall.getName() + "...");
                Optional<ChatMessage> message = functionExecutor.executeAndConvertToMessageSafely(functionCall);
                /* You can also try 'executeAndConvertToMessage' inside a try-catch block, and add the following line inside the catch:
                "message = executor.handleException(exception);"
                The content of the message will be the exception itself, so the flow of the conversation will not be interrupted, and you will still be able to log the issue. */

                if (message.isPresent()) {
                    /* At this point:
                    1. The function requested was found
                    2. The request was converted to its specified object for execution (Weather.class in this case)
                    3. It was executed
                    4. The response was finally converted to a ChatMessage object. */

                    System.out.println("Executed " + functionCall.getName() + ".");
                    messages.add(message.get());
                    continue;
                } else {
                    System.out.println("Something went wrong with the execution of " + functionCall.getName() + "...");
                    break;
                }
            }

            System.out.println("Response: " + responseMessage.getContent());
            System.out.print("Next Query: ");
            String nextLine = scanner.nextLine();
            if (nextLine.equalsIgnoreCase("exit")) {
                System.exit(0);
            }
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), nextLine));
        }
    }

}
