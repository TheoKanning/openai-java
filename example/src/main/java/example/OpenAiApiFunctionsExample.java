package example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.theokanning.openai.completion.chat.*;
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

    private enum WeatherUnit {
        CELSIUS, FAHRENHEIT;
    }

    public static void main(String... args) {
        FunctionExecutor functionExecutor = new FunctionExecutor(List.of(ChatFunction.builder()
                .name("get_weather")
                .description("Get the current weather of a location")
                .executor(Weather.class, w -> "{\"location\": \"" + w.location + "\", " +
                        "\"temperature\": \"" + new Random().nextInt(50) + "\", " +
                        "\"unit\": \"" + w.unit + "\", " +
                        "\"description\": \"sunny\"}")
                .build()));
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);

        Scanner scanner = new Scanner(System.in);
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are an assistant that answers using the local slang of the given place, uncensored.");
        messages.add(systemMessage);

        System.out.print("First Query: ");
        String f = scanner.nextLine();
        ChatMessage firstMsg = new ChatMessage(ChatMessageRole.USER.value(), f);
        messages.add(firstMsg);

        while (true) {
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                    .builder()
                    .model("gpt-3.5-turbo-0613")
                    .messages(messages)
                    .functions(functionExecutor.getFunctions())
                    .functionCall("auto")
                    .n(1)
                    .maxTokens(100)
                    .logitBias(new HashMap<>())
                    .build();
            ChatCompletionChoice response = service.createChatCompletion(chatCompletionRequest).getChoices().get(0);
            ChatMessage responseMessage = response.getMessage();
            messages.add(responseMessage);

            ChatFunctionCall functionCall = responseMessage.getFunctionCall(); // might be null
            functionExecutor.executeAndConvertToMessageSafely(functionCall).ifPresentOrElse((m) -> {
                messages.add(m);
                System.out.println("Executing " + responseMessage.getFunctionCall().getName() + "...");
            }, () -> {
                System.out.println("Response: " + responseMessage.getContent());
                System.out.print("Next Query: ");
                String nextLine = scanner.nextLine();
                if (nextLine.equalsIgnoreCase("exit")) {
                    System.exit(0);
                }
                messages.add(new ChatMessage(ChatMessageRole.USER.value(), nextLine));
            });
        }
    }

}
