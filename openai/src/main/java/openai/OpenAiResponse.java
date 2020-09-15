package openai;

import java.util.List;

public class OpenAiResponse<T> {
    public List<T> data;
    public String object;
}
