package example;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.engine.Engine;

class OpenAiApiExample {
    public static void main(String... args) {
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);

        System.out.println("\nGetting available engines...");
        service.getEngines().forEach(System.out::println);

        System.out.println("\nGetting ada engine...");
        Engine ada = service.getEngine("ada");
        System.out.println(ada);

        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .user("testing")
                .build();
        service.createCompletion("ada", completionRequest).getChoices().forEach(System.out::println);
    }
}
