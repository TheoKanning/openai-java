package com.theokanning.openai.service;

import com.theokanning.openai.DeleteResult;
import com.theokanning.openai.batch.*;
import com.theokanning.openai.file.File;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author: acone.wu
 * @date: 2024/5/15 15:50
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BatchTest {

    static String filePath = "src/test/resources/batch-chat-data.jsonl";

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);
    static String fileId;

    static Batch batch;

    @Test
    @Order(1)
    void uploadFile() throws Exception {
        File file = service.uploadFile("batch", filePath);
        fileId = file.getId();

        assertEquals("batch", file.getPurpose());
        assertEquals(filePath, file.getFilename());

        // wait for file to be processed
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    @Order(2)
    void createBatch() {
        CreateBatchRequest request = CreateBatchRequest.builder()
                .inputFileId(fileId)
                .endpoint("/v1/chat/completions")
                .compWindow("24h")
                .build();
        batch = service.createBatch(request);

        assertEquals("/v1/chat/completions", batch.getEndpoint());
        assertEquals("24h", batch.getCompletionWindow());
        assertEquals(Status.VALIDATING, batch.getStatus());
    }

    @Test
    @Order(3)
    void retrieveBatch() {
        Batch detail = service.retrieveBatch(batch.getId());

        assertEquals("/v1/chat/completions", detail.getEndpoint());
        assertEquals("24h", detail.getCompletionWindow());
    }

    @Test
    @Order(4)
    void listBatches() {
        BatchListRequest request = BatchListRequest.builder().build();
        BatchListResult batchListResult = service.listBatch(request);

        assertTrue(batchListResult.getData().stream().anyMatch(b -> batch.getId().equals(b.getId())));
    }

    @Test
    @Order(4)
    void cancelBatch() {
        Batch cancelled = service.cancel(batch.getId());

        assertEquals(Status.CANCELLING, cancelled.getStatus());
    }

    @Test
    @Order(5)
    void deleteFile() {
        DeleteResult deleteResult = service.deleteFile(fileId);
        assertTrue(deleteResult.isDeleted());
    }
}
