package example;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.event.ServerSentEvent;
import okhttp3.Request;
import okhttp3.Response;


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
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

        ServerSentEvent.Listener listener = new ServerSentEvent.Listener(){

            @Override
            public void onOpen(ServerSentEvent sse, Response response) {
                System.out.println("response:" + response.message());
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                System.out.println("event:" + event + ",message:" + message + ",id:" + id);
            }

            @Override
            public void onComment(ServerSentEvent sse, String comment) {
                System.out.println("comment:" + comment);

            }

            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                System.out.println("milliseconds:" + milliseconds);
                return false;
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
                System.out.println("throwable:" + throwable.getMessage() + throwable);
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
        ServerSentEvent sse = service.createCompletionStream(completionRequest, listener);

        System.out.println("\nCreating Image...");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt("A cow breakdancing with a turtle")
                .build();

        System.out.println("\nImage is located at:");
        System.out.println(service.createImage(request).getData().get(0).getUrl());
    }
}
