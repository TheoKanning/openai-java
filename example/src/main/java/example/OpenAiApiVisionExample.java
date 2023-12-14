package example;

import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.utils.VisionUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class OpenAiApiVisionExample {
    public static void main(String... args) {
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

        System.out.println("Streaming chat completion...");
        final List<ChatMessage> messages = new ArrayList<>();
        List<ChatMessageContent> content = new ArrayList<>();
        content.add(new ChatMessageContent("What’s in this image?"));
        content.add(new ChatMessageContent(new ImageUrl(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg")));
        messages.add(new ChatMessage<>(ChatMessageRole.USER.value(), content));

        // use VisionUtil to convert image prompt to OpenAI format
        System.out.println("Converting image to OpenAI format...");
        ChatMessage<List<ChatMessageContent>> visionChatMessage = VisionUtil.convertForVision(
            new ChatMessage<>(ChatMessageRole.USER.value(),
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg "
                    + "What are in these images? Is there any difference between them?"));
        messages.add(visionChatMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
            .builder()
            .model("gpt-4-vision-preview")
            .messages(messages)
            .maxTokens(300)
            .build();

        service.streamChatCompletion(chatCompletionRequest).blockingForEach(System.out::println);
        service.shutdownExecutor();
    }
}
