package example;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

class OpenAiApiExample {
    public static void main(String... args) {
        OpenAiApiExample openAiApiExample = new OpenAiApiExample();
        openAiApiExample.OpenAiServiceWithOnlyToken();
        openAiApiExample.OpenAiServiceWithTokenAndUrlAndTimeOut();
    }

    public void OpenAiServiceWithOnlyToken(){
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);
        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .user("testing")
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
    }

    public void OpenAiServiceWithTokenAndUrlAndTimeOut(){
        String token = System.getenv("OPENAI_TOKEN");
        final String BASE_URL = "https://api.openai.com/";
        OpenAiService service = new OpenAiService(token, BASE_URL,  Duration.ofSeconds(10));
        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .user("testing")
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
    }

}
