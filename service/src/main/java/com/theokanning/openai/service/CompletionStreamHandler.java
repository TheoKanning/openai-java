package com.theokanning.openai.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.Usage;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionResult;
import okhttp3.sse.EventSource;

import java.util.*;

/**
 * Created by julian0zzx on 2023/3/14.
 */
public abstract class CompletionStreamHandler extends StreamHandler<CompletionResult> {

    CompletionResult result = new CompletionResult();
    ObjectMapper mapper = OpenApiStreamService.defaultObjectMapper();

    long promptTokens;
    long completionTokens;
    long totalTokens;

    @Override
    public CompletionResult handleEvent(EventSource eventSource, String data) {
        if (!"[DONE]".equalsIgnoreCase(data)) {
            try {
                CompletionResult ccr = mapper.readValue(data, CompletionResult.class);

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

                List<CompletionChoice> ccc0 = result.getChoices();
                if (null == ccc0) {
                    result.setChoices(ccr.getChoices());
                } else {
                    List<CompletionChoice> ccc1 = ccr.getChoices();
                    HashMap<Integer, CompletionChoice> idxChoiceMap1 = new HashMap<>();
                    for (CompletionChoice ccc : ccc1) {
                        idxChoiceMap1.put(ccc.getIndex(), ccc);
                    }

                    HashMap<Integer, CompletionChoice> idxChoiceMap0 = new HashMap<>();
                    for (CompletionChoice ccc : ccc0) {
                        idxChoiceMap0.put(ccc.getIndex(), ccc);
                    }

                    for (Integer idx : idxChoiceMap0.keySet()) {
                        CompletionChoice choice0 = idxChoiceMap0.get(idx);
                        String fr = idxChoiceMap1.get(idx).getFinish_reason();
                        choice0.setFinish_reason(fr);
                        if ("stop".equalsIgnoreCase(fr) || "length".equalsIgnoreCase(fr) || "content_filter".equalsIgnoreCase(fr)) {
                            choice0.setText(choice0.getText());

                            eventSource.cancel();
                        } else {
                            String curTxt = choice0.getText();

                            String cnt = (curTxt == null || "null".equalsIgnoreCase(curTxt)) ? "" : curTxt;
                            choice0.setText(cnt + idxChoiceMap1.get(idx).getText());
                        }

                        idxChoiceMap0.put(idx, choice0);
                    }

                    List<CompletionChoice> completionChoices = new ArrayList<>(idxChoiceMap0.values());
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

