package example;

import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.assistants.Assistant;
import com.theokanning.openai.assistants.AssistantRequest;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.messages.MessageContent;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.CreateThreadAndRunRequest;
import com.theokanning.openai.runs.Run;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/** Example code to use the assistant feature:
 * 1. Create a new assistant
 * 2. Attach a thread
 * 3. Wait for messages to process
 * 4. Attach a new user message
 * 5. Wait for messages to process
 */
final class OpenAiAssistantExample {
  private static void waitForRunToComplete(OpenAiService service, String runId, String threadId)
      throws InterruptedException {
    Run run;
    do {
      run = service.retrieveRun(threadId, runId);
      // Or do something better to wait for the status to change. As for EOY 2023 it takes a few
      // seconds.
      TimeUnit.SECONDS.sleep(1);
    } while (!Objects.equals(run.getStatus(), "completed"));
  }

  private static void printMessages(OpenAiResponse<Message> messages) {
    for (Message message : messages.getData()) {
      StringBuilder sb = new StringBuilder();
      for(MessageContent content : message.getContent()) {
        sb.append(content);
      }

      System.out.printf("[%s] %s%n", message.getRole(), sb);
    }
  }

  public static void main(String... args) throws InterruptedException {
    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

    System.out.println("\nCreating a new assistant....");
    Assistant assistant =
        service.createAssistant(
            AssistantRequest.builder()
                .name("Daniel Beta Alpha")
                .model("gpt-4-1106-preview")
                .instructions("A math expert")
                .build());
    String assistantId = assistant.getId();

    System.out.println(
        "\nAttaching a new thread to the assistant, and run it to create the first message from the\n"
            + " assistant...");
    CreateThreadAndRunRequest createThreadAndRunRequest =
        CreateThreadAndRunRequest.builder().assistantId(assistantId).build();
    Run run = service.createThreadAndRun(createThreadAndRunRequest);

    System.out.println(
        "\nKeeping the thread id which is a persistent id for the conversation between the user and the\n"
            + " assistant...");
    String threadId = run.getThreadId();

    System.out.println("\nWaiting for GPT to process the new thread...");
    waitForRunToComplete(service, run.getId(), threadId);

    System.out.println(
        "\nGetting the latest messages list, should contain a single message from the assistant...");
    OpenAiResponse<Message> messages = service.listMessages(threadId);
    printMessages(messages);

    System.out.println("\nAdding a new user message to the thread...");
    service.createMessage(
        threadId, MessageRequest.builder().content("Can you solve 1+1?").role("user").build());

    System.out.println("\nCreating a new run for GPT to response to the message...");
    run = service.createRun(threadId, RunCreateRequest.builder().assistantId(assistantId).build());

    System.out.println("\nWaiting again to the run to complete...");
    waitForRunToComplete(service, run.getId(), threadId);

    System.out.println("\nObserving the latest messages after the second run...");
    messages = service.listMessages(threadId);
    printMessages(messages);

    service.shutdownExecutor();
  }
}
