package com.theokanning.openai;

import static java.time.Duration.*;

import java.time.Duration;

/**
 * Use the OpenAiService from the new 'service' library. See README for more details.
 * 
 * @deprecated Has moved to {@link com.theokanning.openai.client.OpenAiService}.
 */
@Deprecated
public class OpenAiService extends com.theokanning.openai.client.OpenAiService {
    /**
     * Creates a new OpenAiService that wraps OpenAiApi
     *
     * @param token OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     */
    public OpenAiService(final String token) {
        this(token, BASE_URL, ofSeconds(10));
    }

    /**
     * Creates a new OpenAiService that wraps OpenAiApi
     *
     * @param token   OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     * @param timeout http read timeout in seconds, 0 means no timeout
     * @deprecated use {@link OpenAiService(String, Duration)}
     */
    @Deprecated
    public OpenAiService(final String token, final int timeout) {
        this(token, BASE_URL, ofSeconds(timeout));
    }

    /**
     * Creates a new OpenAiService that wraps OpenAiApi
     *
     * @param token   OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     * @param timeout http read timeout, Duration.ZERO means no timeout
     */
    public OpenAiService(final String token, final Duration timeout) {
        this(token, BASE_URL, timeout);
    }

    /**
     * Creates a new OpenAiService that wraps OpenAiApi
     *
     * @param token   OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     * @param timeout http read timeout, Duration.ZERO means no timeout
     */
    public OpenAiService(final String token, final String baseUrl, final Duration timeout) {
    	super(token, baseUrl, timeout);
    }

    /**
     * Creates a new OpenAiService that wraps OpenAiApi
     *
     * @param api OpenAiApi instance to use for all methods
     */
    public OpenAiService(final OpenAiApi api) {
        super(api);
    }
}
