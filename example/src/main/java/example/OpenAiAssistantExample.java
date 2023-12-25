package example;

import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.assistants.Assistant;
import com.theokanning.openai.assistants.AssistantRequest;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.CreateThreadAndRunRequest;
import com.theokanning.openai.runs.Run;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

final class OpenAiAssistantExample {
  private static void waitForRunToComplete(OpenAiService service, String runId, String threadId)
      throws InterruptedException {
    Run run;
    do {
      run = service.retrieveRun(threadId, runId);
      // Or do something better to wait for the status to change.
      TimeUnit.SECONDS.sleep(1);
    } while (!Objects.equals(run.getStatus(), "completed"));
  }

  private static void print(OpenAiResponse<Message> messageOpenAiResponse) {
    for (Message message : messageOpenAiResponse.getData()) {
      System.out.printf("[%s] %s%n", message.getRole(), message.getContent().get(0));
    }
  }

  public static void main(String... args) throws InterruptedException {
    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));
    System.out.println("\nCreate a new assistant....");
    Assistant assistant =
        service.createAssistant(
            AssistantRequest.builder()
                .name("Daniel Beta Alpha")
                .model("gpt-4-1106-preview")
                .instructions("A math expert")
                .build());
    String assistantId = assistant.getId();

    System.out.println(
        "\nAttach a new thread to the assistant, and run it to create the first message from the\n"
            + " assistant...");
    CreateThreadAndRunRequest createThreadAndRunRequest =
        CreateThreadAndRunRequest.builder().assistantId(assistantId).build();

    Run run = service.createThreadAndRun(createThreadAndRunRequest);
    System.out.println(
        "\nKeep the thread id which is a persistent id for the conversation between the user and the\n"
            + " assistant...");
    String threadId = run.getThreadId();

    System.out.println("\nWait for gpt to process the new thread...");
    waitForRunToComplete(service, run.getId(), threadId);

    System.out.println("\nGet the latest messages list, the one message should be from the assistant...");
    OpenAiResponse<Message> messageOpenAiResponse = service.listMessages(threadId);
    print(messageOpenAiResponse);

    System.out.println("\nAdd a new message to the thread...");
    service.createMessage(
        threadId, MessageRequest.builder().content("Can you solve 1+1?").role("user").build());

    System.out.println("\nCreate a new run for gpt to response to the message...");
    run = service.createRun(threadId, RunCreateRequest.builder().assistantId(assistantId).build());

    System.out.println("\nWait again to the run to complete...");
    waitForRunToComplete(service, run.getId(), threadId);

    System.out.println("\nObserve the latest messages after the second run...");
    messageOpenAiResponse = service.listMessages(threadId);
    print(messageOpenAiResponse);
  }
}
