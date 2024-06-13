package com.theokanning.openai.utils;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * Token calculation tool class
 */
public class TikTokensUtil {
    /**
     * Model name corresponds to Encoding
     */
    private static final Map<String, Encoding> modelMap = new HashMap<>();
    /**
     * Registry instance
     */
    private static final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();

    static {
        for (ModelType modelType : ModelType.values()) {
            modelMap.put(modelType.getName(), registry.getEncodingForModel(modelType));
        }
        modelMap.put(ModelEnum.GPT_3_5_TURBO_0301.getName(), registry.getEncodingForModel(ModelType.GPT_3_5_TURBO));
        modelMap.put(ModelEnum.GPT_4_32K.getName(), registry.getEncodingForModel(ModelType.GPT_4));
        modelMap.put(ModelEnum.GPT_4_32K_0314.getName(), registry.getEncodingForModel(ModelType.GPT_4));
        modelMap.put(ModelEnum.GPT_4_0314.getName(), registry.getEncodingForModel(ModelType.GPT_4));
        modelMap.put(ModelEnum.GPT_4_1106_preview.getName(), registry.getEncodingForModel(ModelType.GPT_4));
    }

    /**
     * Get encoding array through Encoding and text.
     *
     * @param enc  Encoding type
     * @param text Text information
     * @return Encoding array
     */
    public static List<Integer> encode(Encoding enc, String text) {
        return isBlank(text) ? new ArrayList<>() : enc.encode(text);
    }

    /**
     * Calculate tokens of text information through Encoding.
     *
     * @param enc  Encoding type
     * @param text Text information
     * @return Number of tokens
     */
    public static int tokens(Encoding enc, String text) {
        return encode(enc, text).size();
    }


    /**
     * Reverse calculate text information through Encoding and encoded array
     *
     * @param enc     Encoding
     * @param encoded Encoding array
     * @return Text information corresponding to the encoding array.
     */
    public static String decode(Encoding enc, List<Integer> encoded) {
        return enc.decode(encoded);
    }

    /**
     * Get an Encoding object by Encoding type
     *
     * @param encodingType
     * @return Encoding
     */
    public static Encoding getEncoding(EncodingType encodingType) {
        Encoding enc = registry.getEncoding(encodingType);
        return enc;
    }

    /**
     * Obtain the encoding array by encoding;
     *
     * @param text
     * @return Encoding array
     */
    public static List<Integer> encode(EncodingType encodingType, String text) {
        if (isBlank(text)) {
            return new ArrayList<>();
        }
        Encoding enc = getEncoding(encodingType);
        List<Integer> encoded = enc.encode(text);
        return encoded;
    }

    /**
     * Compute the tokens of the specified string through EncodingType.
     *
     * @param encodingType
     * @param text
     * @return Number of tokens
     */
    public static int tokens(EncodingType encodingType, String text) {
        return encode(encodingType, text).size();
    }


    /**
     * Reverse the encoded array to get the string text using EncodingType and the encoded array.
     *
     * @param encodingType
     * @param encoded
     * @return The string corresponding to the encoding array.
     */
    public static String decode(EncodingType encodingType, List<Integer> encoded) {
        Encoding enc = getEncoding(encodingType);
        return enc.decode(encoded);
    }


    /**
     * Get an Encoding object by model name.
     *
     * @param modelName
     * @return Encoding
     */
    public static Encoding getEncoding(String modelName) {
        return modelMap.get(modelName);
    }

    /**
     * Get the encoded array by model name using encode.
     *
     * @param text Text information
     * @return Encoding array
     */
    public static List<Integer> encode(String modelName, String text) {
        if (isBlank(text)) {
            return new ArrayList<>();
        }
        Encoding enc = getEncoding(modelName);
        if (Objects.isNull(enc)) {
            return new ArrayList<>();
        }
        List<Integer> encoded = enc.encode(text);
        return encoded;
    }

    /**
     * Calculate the tokens of a specified string by model name.
     *
     * @param modelName
     * @param text
     * @return Number of tokens
     */
    public static int tokens(String modelName, String text) {
        return encode(modelName, text).size();
    }


    /**
     * Calculate the encoded array for messages by model name.
     * Refer to the official processing logic:
     * <a href=https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb>https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb</a>
     *
     * @param modelName
     * @param messages
     * @return Number of tokens
     */
    public static int tokens(String modelName, List<ChatMessage> messages) {
        Encoding encoding = getEncoding(modelName);
        int tokensPerMessage = 0;
        int tokensPerName = 0;
        //3.5统一处理
        if (modelName.equals("gpt-3.5-turbo-0301") || modelName.equals("gpt-3.5-turbo")) {
            tokensPerMessage = 4;
            tokensPerName = -1;
        }
        //4.0统一处理
        if (modelName.equals("gpt-4") || modelName.equals("gpt-4-0314")) {
            tokensPerMessage = 3;
            tokensPerName = 1;
        }
        int sum = 0;
        for (ChatMessage msg : messages) {
            sum += tokensPerMessage;
            if(msg.getContent() instanceof String){
                sum += tokens(encoding, msg.getContent().toString());
            }
            sum += tokens(encoding, msg.getRole());
            sum += tokens(encoding, msg.getName());
            if (isNotBlank(msg.getName())) {
                sum += tokensPerName;
            }
        }
        sum += 3;
        return sum;
    }

    /**
     * Reverse the string text through the model name and the encoded array.
     *
     * @param modelName
     * @param encoded
     * @return
     */
    public static String decode(String modelName, List<Integer> encoded) {
        Encoding enc = getEncoding(modelName);
        return enc.decode(encoded);
    }


    /**
     * Obtain the modelType.
     *
     * @param name
     * @return
     */
    public static ModelType getModelTypeByName(String name) {
        if (ModelEnum.GPT_3_5_TURBO_0301.getName().equals(name)) {
            return ModelType.GPT_3_5_TURBO;
        }
        if (ModelEnum.GPT_4.getName().equals(name)
                || ModelEnum.GPT_4_32K.getName().equals(name)
                || ModelEnum.GPT_4_32K_0314.getName().equals(name)
                || ModelEnum.GPT_4_0314.getName().equals(name)) {
            return ModelType.GPT_4;
        }

        for (ModelType modelType : ModelType.values()) {
            if (modelType.getName().equals(name)) {
                return modelType;
            }
        }
        return null;
    }

    @Getter
    @AllArgsConstructor
    public enum ModelEnum {
        /**
         * gpt-3.5-turbo
         */
        GPT_3_5_TURBO("gpt-3.5-turbo"),
        /**
         * Temporary model, not recommended for use.
         */
        GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301"),
        /**
         * GPT4.0
         */
        GPT_4("gpt-4"),
        /**
         * Temporary model, not recommended for use.
         */
        GPT_4_0314("gpt-4-0314"),
        /**
         * GPT4.0 超长上下文
         */
        GPT_4_32K("gpt-4-32k"),
        /**
         * Temporary model, not recommended for use.
         */
        GPT_4_32K_0314("gpt-4-32k-0314"),

        /**
         * Temporary model, not recommended for use.
         */
        GPT_4_1106_preview("gpt-4-1106-preview");
        private String name;
    }

    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == 65279 || c == 8234 || c == 0 || c == 12644 || c == 10240 || c == 6158;
    }

    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static boolean isBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for (int i = 0; i < length; ++i) {
                if (!isBlankChar(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

}
