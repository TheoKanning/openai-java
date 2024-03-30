package io.github.panghy.openai.service;

import io.github.panghy.openai.OpenAiResponse;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.fine_tuning.FineTuningEvent;
import io.github.panghy.openai.fine_tuning.FineTuningJob;
import io.github.panghy.openai.fine_tuning.FineTuningJobRequest;
import io.github.panghy.openai.fine_tuning.Hyperparameters;
import io.github.panghy.openai.service.OpenAiService;
import io.reactivex.Single;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FineTuningTest {
  OpenAiApi mockApi;
  OpenAiService service;

  @BeforeEach
  void setUp() {
    mockApi = mock(OpenAiApi.class);
    service = new OpenAiService(mockApi);
  }

  static String fineTuningJobId;

  @Test
  @Order(1)
  void createFineTuningJob() {
    Hyperparameters hyperparameters = Hyperparameters.builder()
        .nEpochs(4)
        .build();
    FineTuningJobRequest request = FineTuningJobRequest.builder()
        .trainingFile("test-file-id")
        .model("gpt-3.5-turbo")
        .hyperparameters(hyperparameters)
        .build();

    when(mockApi.createFineTuningJob(request)).thenReturn(just(FineTuningJob.builder()
        .id("fine-tuning-job-id")
        .model("gpt-3.5-turbo")
        .status("running")
        .build()));
    FineTuningJob fineTuningJob = service.createFineTuningJob(request);
    fineTuningJobId = fineTuningJob.getId();

    assertNotNull(fineTuningJob);
  }

  @Test
  @Order(2)
  void listFineTuningJobs() {
    when(mockApi.listFineTuningJobs()).thenReturn(just(OpenAiResponse.<FineTuningJob>builder().data(
            List.of(FineTuningJob.builder()
                .id("fine-tuning-job-id")
                .model("gpt-3.5-turbo")
                .status("running")
                .build()))
        .build()));
    List<FineTuningJob> fineTuningJobs = service.listFineTuningJobs();
    verify(mockApi, times(1)).listFineTuningJobs();

    assertTrue(fineTuningJobs.stream().anyMatch(fineTuningJob -> fineTuningJob.getId().equals(fineTuningJobId)));
  }

  @Test
  @Order(2)
  void listFineTuningEvents() {
    when(mockApi.listFineTuningJobEvents(fineTuningJobId)).
        thenReturn(just(OpenAiResponse.<FineTuningEvent>builder().data(
                List.of(FineTuningEvent.builder()
                    .id("event-id")
                    .object("fine_tuning.event")
                    .createdAt(1630000000L)
                    .message("Fine-tuning job started")
                    .build()))
            .build()));
    List<FineTuningEvent> events = service.listFineTuningJobEvents(fineTuningJobId);
    verify(mockApi, times(1)).listFineTuningJobEvents(fineTuningJobId);

    assertFalse(events.isEmpty());
  }

  @Test
  @Order(2)
  void retrieveFineTuningJob() {
    when(mockApi.retrieveFineTuningJob(fineTuningJobId)).thenReturn(just(FineTuningJob.builder()
        .id("fine-tuning-job-id")
        .model("gpt-3.5-turbo")
        .status("running")
        .build()));
    FineTuningJob fineTune = service.retrieveFineTuningJob(fineTuningJobId);
    verify(mockApi, times(1)).retrieveFineTuningJob(fineTuningJobId);

    assertTrue(fineTune.getModel().startsWith("gpt-3.5-turbo"));
  }

  @Test
  @Order(3)
  void cancelFineTuningJob() throws Exception {
    when(mockApi.cancelFineTuningJob(fineTuningJobId)).thenReturn(just(FineTuningJob.builder()
        .id("fine-tuning-job-id")
        .model("gpt-3.5-turbo")
        .status("cancelled")
        .build()));
    FineTuningJob fineTuningJob = service.cancelFineTuningJob(fineTuningJobId);
    verify(mockApi, times(1)).cancelFineTuningJob(fineTuningJobId);

    assertEquals("cancelled", fineTuningJob.getStatus());
  }
}
