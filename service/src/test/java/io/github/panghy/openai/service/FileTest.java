package io.github.panghy.openai.service;

import io.github.panghy.openai.DeleteResult;
import io.github.panghy.openai.OpenAiResponse;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.file.File;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileTest {
    static String filePath = "src/test/resources/fine-tuning-data.jsonl";

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    static String fileId;

    @Test
    @Order(1)
    void uploadFile() throws Exception {
        when(mockApi.uploadFile(any(), any())).thenReturn(just(File.builder()
            .id("file-id")
            .purpose("fine-tune")
            .filename(filePath)
            .build()));
        File file = service.uploadFile("fine-tune", filePath);
        verify(mockApi, times(1)).uploadFile(any(), any());
        fileId = file.getId();

        assertEquals("fine-tune", file.getPurpose());
        assertEquals(filePath, file.getFilename());
    }

    @Test
    @Order(2)
    void listFiles() {
        when(mockApi.listFiles()).thenReturn(just(OpenAiResponse.<File>builder()
            .data(List.of(File.builder()
                .id(fileId)
                .purpose("fine-tune")
                .filename(filePath)
                .build()))
            .build()));
        List<File> files = service.listFiles();
        verify(mockApi, times(1)).listFiles();

        assertTrue(files.stream().anyMatch(file -> file.getId().equals(fileId)));
    }

    @Test
    @Order(3)
    void retrieveFile() {
        when(mockApi.retrieveFile(fileId)).thenReturn(just(File.builder()
            .id(fileId)
            .purpose("fine-tune")
            .filename(filePath)
            .build()));
        File file = service.retrieveFile(fileId);
        verify(mockApi, times(1)).retrieveFile(fileId);

        assertEquals(filePath, file.getFilename());
    }

    @Test
    @Order(4)
    void retrieveFileContent() throws IOException {
        when(mockApi.retrieveFileContent(fileId)).thenReturn(just(ResponseBody.create(null,
            Files.readAllBytes(Paths.get(filePath)))));
        String fileBytesToString = service.retrieveFileContent(fileId).string();
        verify(mockApi, times(1)).retrieveFileContent(fileId);
        String contents = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        assertEquals(contents, fileBytesToString);
    }

    @Test
    @Order(5)
    void deleteFile() {
        when(mockApi.deleteFile(fileId)).thenReturn(just(DeleteResult.builder().deleted(true).build()));
        DeleteResult result = service.deleteFile(fileId);
        verify(mockApi, times(1)).deleteFile(fileId);
        assertTrue(result.isDeleted());
    }
}
