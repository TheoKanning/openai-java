package io.github.panghy.openai.service;

import io.github.panghy.openai.DeleteResult;
import io.github.panghy.openai.file.File;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileTest {
    static String filePath = "src/test/resources/fine-tuning-data.jsonl";

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);
    static String fileId;

    @Test
    @Order(1)
    void uploadFile() throws Exception {
        File file = service.uploadFile("fine-tune", filePath);
        fileId = file.getId();

        assertEquals("fine-tune", file.getPurpose());
        assertEquals(filePath, file.getFilename());

        // wait for file to be processed
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    @Order(2)
    void listFiles() {
        List<File> files = service.listFiles();

        assertTrue(files.stream().anyMatch(file -> file.getId().equals(fileId)));
    }

    @Test
    @Order(3)
    void retrieveFile() {
        File file = service.retrieveFile(fileId);

        assertEquals(filePath, file.getFilename());
    }

    @Test
    @Order(4)
    void retrieveFileContent() throws IOException {
        String fileBytesToString = service.retrieveFileContent(fileId).string();
        String contents = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        assertEquals(contents, fileBytesToString);
    }

    @Test
    @Order(5)
    void deleteFile() {
        DeleteResult result = service.deleteFile(fileId);
        assertTrue(result.isDeleted());
    }
}
