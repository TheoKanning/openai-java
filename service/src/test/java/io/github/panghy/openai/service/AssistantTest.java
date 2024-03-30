package io.github.panghy.openai.service;

import io.github.panghy.openai.DeleteResult;
import io.github.panghy.openai.ListSearchParameters;
import io.github.panghy.openai.OpenAiResponse;
import io.github.panghy.openai.assistants.*;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.file.File;
import io.github.panghy.openai.utils.TikTokensUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssistantTest {

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    static String assistantId;
    static String fileId;

    @Test
    @Order(1)
    void createAssistant() {
        AssistantRequest assistantRequest = AssistantRequest.builder().
            model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName()).
            name("Math Tutor").
            instructions("You are a personal Math Tutor.").
            tools(Collections.singletonList(new Tool(AssistantToolsEnum.CODE_INTERPRETER, null))).
            build();
        when(mockApi.createAssistant(assistantRequest)).thenReturn(just(Assistant.builder().
            id("assistant-id").
            name("Math Tutor").
            model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName()).
            tools(Collections.singletonList(new Tool(AssistantToolsEnum.CODE_INTERPRETER, null))).
            build()));
        Assistant assistant = service.createAssistant(assistantRequest);
        verify(mockApi, times(1)).createAssistant(assistantRequest);

        assistantId = assistant.getId();

        assertEquals(assistant.getName(), "Math Tutor");
        assertEquals(assistant.getTools().get(0).getType(), AssistantToolsEnum.CODE_INTERPRETER);
    }

    @Test
    @Order(2)
    void retrieveAssistant() {
        when(mockApi.retrieveAssistant(assistantId)).thenReturn(just(Assistant.builder().
            id(assistantId).
            name("Math Tutor").
            model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName()).
            tools(Collections.singletonList(new Tool(AssistantToolsEnum.CODE_INTERPRETER, null))).
            build()));
        Assistant assistant = service.retrieveAssistant(assistantId);
        verify(mockApi, times(1)).retrieveAssistant(assistantId);

        assertEquals(assistant.getName(), "Math Tutor");
    }

    @Test
    @Order(3)
    void modifyAssistant() {
        String modifiedName = "Science Tutor";
        ModifyAssistantRequest modifyRequest = ModifyAssistantRequest.builder().name(modifiedName).build();

        when(mockApi.modifyAssistant(assistantId, modifyRequest)).thenReturn(just(Assistant.builder().
            id(assistantId).
            name(modifiedName).
            model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName()).
            tools(Collections.singletonList(new Tool(AssistantToolsEnum.CODE_INTERPRETER, null))).
            build()));
        Assistant modifiedAssistant = service.modifyAssistant(assistantId, modifyRequest);
        verify(mockApi, times(1)).modifyAssistant(assistantId, modifyRequest);
        assertEquals(modifiedName, modifiedAssistant.getName());
    }

    @Test
    @Order(4)
    void listAssistants() {
        when(mockApi.listAssistants(Map.of())).
            thenReturn(just(OpenAiResponse.<Assistant>builder().
                data(List.of(Assistant.builder().
                    id(assistantId).
                    name("Science Tutor").
                    model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName()).
                    tools(Collections.singletonList(new Tool(AssistantToolsEnum.CODE_INTERPRETER, null))).
                    build())).
                build()));
        OpenAiResponse<Assistant> assistants = service.listAssistants(ListSearchParameters.builder().build());

        assertNotNull(assistants);
        assertFalse(assistants.getData().isEmpty());
    }

    @Test
    @Order(5)
    void createAssistantFile() {
        String filePath = "src/test/resources/assistants-data.html";

        when(mockApi.uploadFile(any(), any())).thenReturn(just(File.builder().id("file-id").build()));
        File uploadedFile = service.uploadFile("assistants", filePath);
        verify(mockApi, times(1)).uploadFile(any(), any());

        when(mockApi.createAssistantFile(assistantId, new AssistantFileRequest(uploadedFile.getId()))).
            thenReturn(just(AssistantFile.builder().id(uploadedFile.getId()).assistantId(assistantId).build()));
        AssistantFile assistantFile = service.createAssistantFile(assistantId,
            new AssistantFileRequest(uploadedFile.getId()));

        fileId = assistantFile.getId();
        assertNotNull(assistantFile);
        assertEquals(uploadedFile.getId(), assistantFile.getId());
        assertEquals(assistantId, assistantFile.getAssistantId());
    }

    @Test
    @Order(6)
    void retrieveAssistantFile() {
        when(mockApi.retrieveAssistantFile(assistantId, fileId)).
            thenReturn(just(AssistantFile.builder().id(fileId).assistantId(assistantId).build()));
        AssistantFile file = service.retrieveAssistantFile(assistantId, fileId);
        verify(mockApi, times(1)).retrieveAssistantFile(assistantId, fileId);

        assertEquals(file.getId(), fileId);
    }

    @Test
    @Order(7)
    void listAssistantFiles() {
        when(mockApi.listAssistantFiles(assistantId, Map.of())).
            thenReturn(just(OpenAiResponse.<AssistantFile>builder().
                data(List.of(AssistantFile.builder().
                    id(fileId).
                    object("assistant.file").
                    assistantId(assistantId).build())).
                build()));
        List<AssistantFile> files = service.listAssistantFiles(assistantId, new ListSearchParameters()).data;
        verify(mockApi, times(1)).listAssistantFiles(assistantId, Map.of());

        assertFalse(files.isEmpty());
        assertEquals(files.get(0).getId(), fileId);
        assertEquals(files.get(0).getObject(), "assistant.file");
    }

    @Test
    @Order(8)
    void deleteAssistantFile() {
        when(mockApi.deleteAssistantFile(assistantId, fileId)).thenReturn(just(
            DeleteResult.builder().id(fileId).deleted(true).build()));
        DeleteResult deletedFile = service.deleteAssistantFile(assistantId, fileId);
        verify(mockApi, times(1)).deleteAssistantFile(assistantId, fileId);

        assertEquals(deletedFile.getId(), fileId);
        assertTrue(deletedFile.isDeleted());
    }

    @Test
    @Order(9)
    void deleteAssistant() {
        when(mockApi.deleteAssistant(assistantId)).thenReturn(just(
            DeleteResult.builder().id(assistantId).deleted(true).build()));
        DeleteResult deletedAssistant = service.deleteAssistant(assistantId);
        verify(mockApi, times(1)).deleteAssistant(assistantId);

        assertEquals(assistantId, deletedAssistant.getId());
        assertTrue(deletedAssistant.isDeleted());
    }
}
