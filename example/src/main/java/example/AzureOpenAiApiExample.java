package example;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.model.Model;
import com.theokanning.openai.service.AzureOpenAiService;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class AzureOpenAiApiExample {
    public static void main(String... args) {
        String token = "";
        String deployment = "";
        String baseUrl = "";
        final String apiVersion20221201 = "2022-12-01";
        final String apiVersion20230315 = "2023-03-15-preview";
        AzureOpenAiService service = new AzureOpenAiService(token, baseUrl, deployment, Duration.ZERO);

        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                //.echo(false) DO NOT use echo!
                .model("gpt-3.5-turbo")
                .prompt("Somebody once told me the world is gonna roll me")
                .user("testing")
                .n(3)
                .build();
        service.createCompletion(completionRequest, apiVersion20221201).getChoices().forEach(System.out::println);
        service.shutdownExecutor();
    }
}
