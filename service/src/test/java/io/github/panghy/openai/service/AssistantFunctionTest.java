package io.github.panghy.openai.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.github.panghy.openai.OpenAiResponse;
import io.github.panghy.openai.assistants.*;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.completion.chat.ChatCompletionRequest;
import io.github.panghy.openai.completion.chat.ChatFunction;
import io.github.panghy.openai.completion.chat.ChatFunctionCall;
import io.github.panghy.openai.messages.Message;
import io.github.panghy.openai.messages.MessageRequest;
import io.github.panghy.openai.runs.*;
import io.github.panghy.openai.threads.Thread;
import io.github.panghy.openai.threads.ThreadRequest;
import io.github.panghy.openai.utils.TikTokensUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.reactivex.Single.just;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AssistantFunctionTest {
    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    @Test
    void createRetrieveRun() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.addMixIn(ChatFunction.class, ChatFunctionMixIn.class);
        mapper.addMixIn(ChatCompletionRequest.class, ChatCompletionRequestMixIn.class);
        mapper.addMixIn(ChatFunctionCall.class, ChatFunctionCallMixIn.class);
        
        String funcDef = "{\n" +
                "  \"type\": \"object\",\n" +
                "  \"properties\": {\n" +
                "    \"location\": {\n" +
                "      \"type\": \"string\",\n" +
                "      \"description\": \"The city and state, e.g. San Francisco, CA\"\n" +
                "    },\n" +
                "    \"unit\": {\n" +
                "      \"type\": \"string\",\n" +
                "      \"enum\": [\"celsius\", \"fahrenheit\"]\n" +
                "    }\n" +
                "  },\n" +
                "  \"required\": [\"location\"]\n" +
                "}";
        Map<String, Object> funcParameters = mapper.readValue(funcDef, new TypeReference<>() {
        });
        AssistantFunction function = AssistantFunction.builder()
                .name("weather_reporter")
                .description("Get the current weather of a location")
                .parameters(funcParameters)
                .build();

        List<Tool> toolList = new ArrayList<>();
        Tool funcTool = new Tool(AssistantToolsEnum.FUNCTION, function);
        toolList.add(funcTool);
        
        
        AssistantRequest assistantRequest = AssistantRequest.builder()
                .model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName())
                .name("MATH_TUTOR")
                .instructions("You are a personal Math Tutor.")
                .tools(toolList)
                .build();
        when(mockApi.createAssistant(assistantRequest)).
            thenReturn(just(Assistant.builder().id("test-id").model("test-model").build()));
        Assistant assistant = service.createAssistant(assistantRequest);
        verify(mockApi, times(1)).createAssistant(assistantRequest);

        ThreadRequest threadRequest = ThreadRequest.builder()
                .build();
        when(mockApi.createThread(threadRequest)).
            thenReturn(just(Thread.builder().id("test-thread-id").build()));
        Thread thread = service.createThread(threadRequest);
        verify(mockApi, times(1)).createThread(threadRequest);

        MessageRequest messageRequest = MessageRequest.builder()
                .content("What's the weather of Xiamen?")
                .build();

        when(mockApi.createMessage(thread.getId(), messageRequest)).
            thenReturn(just(Message.builder().build()));
        Message message = service.createMessage(thread.getId(), messageRequest);
        verify(mockApi, times(1)).createMessage(thread.getId(), messageRequest);

        RunCreateRequest runCreateRequest = RunCreateRequest.builder()
                .assistantId(assistant.getId())
                .build();

        when(mockApi.createRun(thread.getId(), runCreateRequest)).
            thenReturn(just(Run.builder().build()));
        Run run = service.createRun(thread.getId(), runCreateRequest);
        verify(mockApi, times(1)).createRun(thread.getId(), runCreateRequest);
        assertNotNull(run);

        when(mockApi.retrieveRun(thread.getId(), run.getId())).
            thenReturn(just(Run.builder().status("in_progress").build()));
        Run retrievedRun = service.retrieveRun(thread.getId(), run.getId());
        verify(mockApi, times(1)).retrieveRun(thread.getId(), run.getId());
        while (!(retrievedRun.getStatus().equals("completed")) 
                && !(retrievedRun.getStatus().equals("failed")) 
                && !(retrievedRun.getStatus().equals("requires_action"))){
            when(mockApi.retrieveRun(thread.getId(), run.getId())).
                thenReturn(just(Run.builder().status("requires_action").
                    threadId(thread.getId()).
                    requiredAction(
                        RequiredAction.builder().
                            submitToolOutputs(SubmitToolOutputs.builder().
                                toolCalls(List.of(ToolCall.builder().id("test-tool-call-id").build())).
                                build()).
                            build()).build()));
            retrievedRun = service.retrieveRun(thread.getId(), run.getId());
            verify(mockApi, times(2)).retrieveRun(thread.getId(), run.getId());
        }
        if (retrievedRun.getStatus().equals("requires_action")) {
            RequiredAction requiredAction = retrievedRun.getRequiredAction();
            System.out.println("requiredAction");
            System.out.println(mapper.writeValueAsString(requiredAction));
            List<ToolCall> toolCalls = requiredAction.getSubmitToolOutputs().getToolCalls();
            ToolCall toolCall = toolCalls.get(0);
            String toolCallId = toolCall.getId();

            SubmitToolOutputRequestItem toolOutputRequestItem = SubmitToolOutputRequestItem.builder()
                    .toolCallId(toolCallId)
                    .output("sunny")
                    .build();
            List<SubmitToolOutputRequestItem> toolOutputRequestItems = new ArrayList<>();
            toolOutputRequestItems.add(toolOutputRequestItem);
            SubmitToolOutputsRequest submitToolOutputsRequest = SubmitToolOutputsRequest.builder()
                    .toolOutputs(toolOutputRequestItems)
                    .build();
            when(mockApi.submitToolOutputs(thread.getId(), run.getId(), submitToolOutputsRequest)).
                thenReturn(just(Run.builder().status("in_progress").build()));
            retrievedRun = service.submitToolOutputs(retrievedRun.getThreadId(), retrievedRun.getId(),
                submitToolOutputsRequest);
            verify(mockApi, times(1)).submitToolOutputs(thread.getId(), run.getId(), submitToolOutputsRequest);

            while (!(retrievedRun.getStatus().equals("completed"))
                    && !(retrievedRun.getStatus().equals("failed"))
                    && !(retrievedRun.getStatus().equals("requires_action"))){
                when(mockApi.retrieveRun(thread.getId(), run.getId())).
                    thenReturn(just(Run.builder().status("completed").build()));
                retrievedRun = service.retrieveRun(thread.getId(), run.getId());
                verify(mockApi, times(3)).retrieveRun(thread.getId(), run.getId());
            }

            when(mockApi.listMessages(thread.getId())).
                thenReturn(just(OpenAiResponse.<Message>builder().data(
                    List.of(
                        Message.builder().id("test-message-id").build()
                    )).build()));
            OpenAiResponse<Message> response = service.listMessages(thread.getId());
            verify(mockApi, times(1)).listMessages(thread.getId());

            List<Message> messages = response.getData();
            assertThat(messages).isNotEmpty();
            assertThat(messages.get(0).getId()).isEqualTo("test-message-id");
        }
    }
}
