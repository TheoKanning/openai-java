package example;

import openai.OpenAiService;
import openai.completion.CompletionRequest;
import openai.engine.Engine;

class OpenAiApiExample {
    public static void main(String... args) {
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);

        System.out.println("Getting available engines...");
        service.getEngines().forEach(System.out::println);

        System.out.println("Getting ada engine...");
        Engine ada = service.getEngine("ada");
        System.out.println(ada);

        System.out.println("Creating completion...");
        CompletionRequest completionRequest = new CompletionRequest();
        completionRequest.setPrompt("Somebody once told me the world is gonna roll me");
        completionRequest.setEcho(true);
        service.createCompletion("ada", completionRequest).getChoices().forEach(System.out::println);

    }
}