package example;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.service.event.ServerSentEvent;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
class OpenAiApiExample {

    public static void main(String... args) throws Exception {
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
        var result = service.createCompletion(completionRequest);
        result.getChoices().forEach(System.out::println);

        ServerSentEvent.Listener listener = new ServerSentEvent.Listener(){

            @Override
            public void onOpen(ServerSentEvent sse, Response response) {
                log.info("response:{}", response.message());
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                log.info("event:{},message:{},id:{}", event, message,id);
            }

            @Override
            public void onComment(ServerSentEvent sse, String comment) {
                log.info("comment:{}", comment);

            }

            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                log.warn("milliseconds:{}",milliseconds);
                return false;
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
                log.error("throwable:", throwable);
                log.warn("throwable:{}", response.message());
                return false;
            }

            @Override
            public void onClosed(ServerSentEvent sse) {

            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                return null;
            }
        };
        completionRequest.setN(1);
        ServerSentEvent sse = service.createCompletionStream(completionRequest, listener);

        System.out.println("\nCreating Image...");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt("A cow breakdancing with a turtle")
                .build();

        System.out.println("\nImage is located at:");
        System.out.println(service.createImage(request).getData().get(0).getUrl());
    }
}
