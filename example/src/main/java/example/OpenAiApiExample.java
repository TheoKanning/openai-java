package example;

import openai.OpenAiService;
import openai.completion.CompletionRequest;
import openai.engine.Engine;
import openai.search.SearchRequest;

import java.util.Arrays;

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
        CompletionRequest completionRequest = new CompletionRequest();
        completionRequest.setPrompt("Somebody once told me the world is gonna roll me");
        completionRequest.setEcho(true);
        service.createCompletion("ada", completionRequest).getChoices().forEach(System.out::println);

        System.out.println("\nSearching documents...");
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setDocuments(Arrays.asList("Water", "Earth", "Electricity", "Fire"));
        searchRequest.setQuery("Pikachu");
        service.search("ada", searchRequest).forEach(System.out::println);
    }
}