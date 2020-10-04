package openai.engine;

import lombok.Data;

@Data
public class Engine {
    public String id;
    public String object;
    public String owner;
    public boolean ready;
}
