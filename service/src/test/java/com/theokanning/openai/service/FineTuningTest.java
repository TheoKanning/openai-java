package com.theokanning.openai.service;

import com.theokanning.openai.fine_tuning.FineTuningEvent;
import com.theokanning.openai.fine_tuning.FineTuningJob;
import com.theokanning.openai.fine_tuning.FineTuningJobRequest;
import com.theokanning.openai.fine_tuning.Hyperparameters;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FineTuningTest {
    static OpenAiService service;
    static String fileId;
    static String fineTuningJobId;


    @BeforeAll
   static void setup() throws Exception {
        String token = System.getenv("OPENAI_TOKEN");
        service = new OpenAiService(token);
        fileId = service.uploadFile("fine-tune", "src/test/resources/chat-fine-tuning-data.jsonl").getId();

        // wait for file to be processed
        TimeUnit.SECONDS.sleep(10);
    }

    @AfterAll
    static void teardown() {
        try {
            service.deleteFile(fileId);
        } catch (Exception e) {
            // ignore
        }
    }

    @Test
    @Order(1)
    void createFineTuningJob() {
        Hyperparameters hyperparameters = Hyperparameters.builder()
                .nEpochs(4)
                .build();
        FineTuningJobRequest request = FineTuningJobRequest.builder()
                .trainingFile(fileId)
                .model("gpt-3.5-turbo")
                .hyperparameters(hyperparameters)
                .build();

        FineTuningJob fineTuningJob = service.createFineTuningJob(request);
        fineTuningJobId = fineTuningJob.getId();

        assertNotNull(fineTuningJob);
    }

    @Test
    @Order(2)
    void listFineTuningJobs() {
        List<FineTuningJob> fineTuningJobs = service.listFineTuningJobs();

        assertTrue(fineTuningJobs.stream().anyMatch(fineTuningJob -> fineTuningJob.getId().equals(fineTuningJobId)));
    }

    @Test
    @Order(2)
    void listFineTuningEvents() {
        List<FineTuningEvent> events = service.listFineTuningJobEvents(fineTuningJobId);

        assertFalse(events.isEmpty());
    }

    @Test
    @Order(2)
    void retrieveFineTuningJob() {
        FineTuningJob fineTune = service.retrieveFineTuningJob(fineTuningJobId);

        assertTrue(fineTune.getModel().startsWith("gpt-3.5-turbo"));
    }

    @Test
    @Order(2)
    void cancelFineTuningJob() {
        FineTuningJob fineTuningJob = service.cancelFineTuningJob(fineTuningJobId);

        assertEquals("cancelled", fineTuningJob.getStatus());
    }
}
