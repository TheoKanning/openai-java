package example;

import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

public class OpenAiApiStreamExample {
	public static void main(String... args) {
		String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);

		System.out.println("\nCreating completion...");
		CompletionRequest completionRequest = CompletionRequest.builder()
				  .model("ada")
				  .prompt("Somebody once told me the world is gonna roll me")
				  .echo(true)
				  .user("testing")
				  .n(3)
				  .build();
        
        /* 
            Note: when using blockingForEach the calling Thread waits until the loop finishes.
            Use forEach instaed of blockignForEach if you don't want the calling Thread to wait.
        */

        //  stream raw bytes 
        service
            .streamCompletionBytes(completionRequest)
            .doOnError( e -> {
                e.printStackTrace();
            })
            .blockingForEach( bytes -> {
                System.out.print(new String(bytes));
            });

        //  stream CompletionChunks
        service
            .streamCompletion(completionRequest)
            .doOnError( e -> {
                e.printStackTrace();
            })
            .blockingForEach(System.out::println);


        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
        messages.add(systemMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(5)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        //  stream ChatCompletionChunks
        service
            .streamChatCompletion(chatCompletionRequest)
            .doOnError( e -> {
                e.printStackTrace();
            })
            .blockingForEach(System.out::println);

        /*
         * shutdown the OkHttp ExecutorService to 
         * exit immediately after the loops have finished
         */
        service.shutdownExecutor();
    }
}
