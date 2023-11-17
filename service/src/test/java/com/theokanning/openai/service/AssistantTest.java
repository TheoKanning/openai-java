package com.theokanning.openai.service;

import com.theokanning.openai.DeleteResult;
import com.theokanning.openai.ListSearchParameters;
import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.assistants.*;
import com.theokanning.openai.file.File;
import com.theokanning.openai.utils.TikTokensUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AssistantTest {
    public static final String MATH_TUTOR = "Math Tutor";
    public static final String ASSISTANT_INSTRUCTION = "You are a personal Math Tutor.";

    static String token = System.getenv("OPENAI_TOKEN");;

    static OpenAiService service = new OpenAiService(token);

    @Test
    void retrieveAssistant() {
        Assistant createAssistantResponse = createAndValidateAssistant();

        Assistant retrieveAssistantResponse = service.retrieveAssistant(createAssistantResponse.getId());
        validateAssistantResponse(retrieveAssistantResponse);
    }

    @Test
    void modifyAssistant() {
        Assistant createAssistantResponse = createAndValidateAssistant();

        String modifiedName = MATH_TUTOR + "Modified";
        ModifyAssistantRequest modifyRequest = ModifyAssistantRequest.builder()
                .name(modifiedName)
                .build();

        Assistant modifiedAssistantResponse = service.modifyAssistant(createAssistantResponse.getId(), modifyRequest);
        assertNotNull(modifiedAssistantResponse);
        assertEquals(modifiedName, modifiedAssistantResponse.getName());
    }

    @Test
    void deleteAssistant() {
        Assistant createAssistantResponse = createAndValidateAssistant();

        DeleteResult deletedAssistant = service.deleteAssistant(createAssistantResponse.getId());

        assertNotNull(deletedAssistant);
        assertEquals(createAssistantResponse.getId(), deletedAssistant.getId());
        assertTrue(deletedAssistant.isDeleted());
    }

    @Test
    void listAssistants() {
        OpenAiResponse<Assistant> assistants = service.listAssistants(ListSearchParameters.builder().build());

        assertNotNull(assistants);
        assertFalse(assistants.getData().isEmpty());
    }

    @Test
    void createAssistantFile() {
        File uploadedFile = uploadAssistantFile();

        Assistant assistant = createAndValidateAssistant();

        AssistantFile assistantFile = service.createAssistantFile(assistant.getId(), new AssistantFileRequest(uploadedFile.getId()));

        assertNotNull(assistantFile);
        assertEquals(uploadedFile.getId(), assistantFile.getId());
        assertEquals(assistant.getId(), assistantFile.getAssistantId());
    }

    @Test
    void retrieveAssistantFile() {
        //TODO
        //There is a bug with uploading assistant files https://community.openai.com/t/possible-bug-with-agent-creation-php-file-upload/484490/5
        //So this would have to be done later
    }

    @Test
    void deleteAssistantFile() {
        //TODO
        //There is a bug with uploading assistant files https://community.openai.com/t/possible-bug-with-agent-creation-php-file-upload/484490/5
        //So this would have to be done later
    }

    @Test
    void listAssistantFiles() {
        //TODO
        //There is a bug with uploading assistant files https://community.openai.com/t/possible-bug-with-agent-creation-php-file-upload/484490/5
        //So this would have to be done later
    }

    @AfterAll
    static void clean() {
        //Clean up all data created during this test
        ListSearchParameters queryFilter = ListSearchParameters.builder()
                .limit(100)
                .build();
        OpenAiResponse<Assistant> assistantListAssistant = service.listAssistants(queryFilter);
        assistantListAssistant.getData().forEach(assistant ->{
            service.deleteAssistant(assistant.getId());
        });
    }

    private static File uploadAssistantFile() {
        String filePath = "src/test/resources/assistants-data.html";
        return service.uploadFile("assistants", filePath);
    }

    private static Assistant createAndValidateAssistant() {
        AssistantRequest assistantRequest = assistantStub();
        Assistant createAssistantResponse = service.createAssistant(assistantRequest);
        validateAssistantResponse(createAssistantResponse);

        return createAssistantResponse;
    }

    private static AssistantRequest assistantStub() {
        return AssistantRequest.builder()
                .model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName())
                .name(MATH_TUTOR)
                .instructions(ASSISTANT_INSTRUCTION)
                .tools(Collections.singletonList(new Tool(AssistantToolsEnum.CODE_INTERPRETER, null)))
                .build();
    }

    private static void validateAssistantResponse(Assistant assistantResponse) {
        assertNotNull(assistantResponse);
        assertNotNull(assistantResponse.getId());
        assertNotNull(assistantResponse.getCreatedAt());
        assertNotNull(assistantResponse.getObject());
        assertEquals(assistantResponse.getTools().get(0).getType(),  AssistantToolsEnum.CODE_INTERPRETER);
        assertEquals(MATH_TUTOR, assistantResponse.getName());
    }
}
