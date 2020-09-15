package example;

import openai.OpenAiService;
import openai.engine.Engine;

class OpenAiApiExample {
    public static void main(String... args) {
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);

        System.out.println("Getting available engines");
        service.getEngines().forEach((Engine e) -> System.out.println(e.id));
    }
}