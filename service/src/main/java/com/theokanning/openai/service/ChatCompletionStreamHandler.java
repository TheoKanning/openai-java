package com.theokanning.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.Usage;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import okhttp3.sse.EventSource;

import java.util.*;

/**
 * Created by julian0zzx on 2023/3/14.
 */
public abstract class ChatCompletionStreamHandler extends StreamHandler<ChatCompletionResult> {

    ChatCompletionResult result = new ChatCompletionResult();
    ObjectMapper mapper = OpenApiStreamService.defaultObjectMapper();

    long promptTokens;
    long completionTokens;
    long totalTokens;

    @Override
    public ChatCompletionResult handleEvent(EventSource eventSource, String data) {
        if (!"[DONE]".equalsIgnoreCase(data)) {
            try {
                ChatCompletionResult ccr = mapper.readValue(data, ChatCompletionResult.class);

                innerHandleEvent(ccr);

                Usage u = ccr.getUsage();
                if (null != u) {
                    promptTokens += u.getPromptTokens();
                    completionTokens += u.getCompletionTokens();
                    totalTokens += u.getTotalTokens();
                } else {
                    u = new Usage();
                }
                u.setPromptTokens(promptTokens);
                u.setCompletionTokens(completionTokens);
                u.setTotalTokens(totalTokens);
                result.setUsage(u);
                result.setId(ccr.getId());
                result.setModel(ccr.getModel());
                result.setObject(ccr.getObject());
                result.setCreated(ccr.getCreated());

                List<ChatCompletionChoice> ccc0 = result.getChoices();
                if (null == ccc0) {
                    result.setChoices(ccr.getChoices());
                } else {
                    List<ChatCompletionChoice> ccc1 = ccr.getChoices();
                    HashMap<Integer, ChatCompletionChoice> idxChoiceMap1 = new HashMap<>();
                    for (ChatCompletionChoice ccc : ccc1) {
                        idxChoiceMap1.put(ccc.getIndex(), ccc);
                    }

                    HashMap<Integer, ChatCompletionChoice> idxChoiceMap0 = new HashMap<>();
                    for (ChatCompletionChoice ccc : ccc0) {
                        idxChoiceMap0.put(ccc.getIndex(), ccc);
                    }

                    for (Integer idx : idxChoiceMap0.keySet()) {
                        ChatCompletionChoice choice0 = idxChoiceMap0.get(idx);
                        String fr = idxChoiceMap1.get(idx).getFinishReason();
                        choice0.setFinishReason(fr);
                        if ("stop".equalsIgnoreCase(fr) || "length".equalsIgnoreCase(fr) || "content_filter".equalsIgnoreCase(fr)) {
                            choice0.setMessage(choice0.getDelta());
                            choice0.getMessage().setRole(choice0.getDelta().getRole());
                            choice0.setDelta(null);

                            eventSource.cancel();
                        } else {
                            ChatMessage delta = choice0.getDelta();

                            final String deltaCnt = delta.getContent();
                            String cnt = (deltaCnt == null || "null".equalsIgnoreCase(deltaCnt)) ? "" : deltaCnt;
                            delta.setContent(cnt + idxChoiceMap1.get(idx).getDelta().getContent());
                            delta.setRole(delta.getRole());
                            choice0.setDelta(delta);
                        }

                        idxChoiceMap0.put(idx, choice0);
                    }

                    List<ChatCompletionChoice> completionChoices = new ArrayList<>(idxChoiceMap0.values());
                    completionChoices.sort((o1, o2) -> o1.getIndex().compareTo(o2.getIndex()));
                    result.setChoices(completionChoices);
                }

                return result;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            eventSource.cancel();
        }

        return result;
    }


}
