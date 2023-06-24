package example;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.utils.TikTokensUtil;

import java.util.ArrayList;
import java.util.List;

class TikTokensExample {

    public static void main(String... args) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "Hello OpenAI 1."));
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "Hello OpenAI 2.   "));

        int tokens_1 = TikTokensUtil.tokens(TikTokensUtil.ModelEnum.GPT_3_5_TURBO.getName(), messages);
        int tokens_2 = TikTokensUtil.tokens(TikTokensUtil.ModelEnum.GPT_3_5_TURBO.getName(), "Hello OpenAI 1.");
    }

}
