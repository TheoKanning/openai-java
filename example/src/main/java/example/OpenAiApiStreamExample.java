package example;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by julian0zzx on 2023/3/14.
 */
public class OpenAiApiStreamExample {

    public static void main(String... args) {
        chatCompletionStream();
        completionStream();
    }

    public static void chatCompletionStream() {
        String token = System.getenv("OPENAI_TOKEN");

        System.out.println("\nCreating stream completion...");
        OpenApiStreamService streamService = new OpenApiStreamService(token);
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("user", "Somebody once told me the world is gonna roll me"));
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .maxTokens(1000)
                .stream(true)
                .n(1)
                .build();

        StreamHandler<ChatCompletionResult> listener = new ChatCompletionStreamHandler() {
            @Override
            public void innerHandleEvent(ChatCompletionResult result) {
                System.out.println("######" + result.getChoices().get(0).getDelta().getContent());
            }
        };

        streamService.createChatStreamCompletion(chatCompletionRequest, listener);
    }


    public static void completionStream() {
        String token = System.getenv("OPENAI_TOKEN");

        System.out.println("\nCreating stream completion...");
        OpenApiStreamService streamService = new OpenApiStreamService(token);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("Somebody once told me the world is gonna roll me")
                .maxTokens(1000)
                .stream(true)
                .n(1)
                .user("testing")
                .logitBias(new HashMap<>())
                .logprobs(5)
                .build();

        StreamHandler<CompletionResult> listener = new CompletionStreamHandler() {
            @Override
            public void innerHandleEvent(CompletionResult result) {
                System.out.println("######" + result.getChoices().get(0).getText());
            }
        };

        streamService.createStreamCompletion(completionRequest, listener);
    }

}
