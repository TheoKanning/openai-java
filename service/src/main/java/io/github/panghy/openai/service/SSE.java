package io.github.panghy.openai.service;

/**
 * Simple Server Sent Event representation
 */
public class SSE {
    private static final String DONE_DATA = "[DONE]";
    
    private final String data;

    public SSE(String data){
        this.data = data;
    }

    public String getData(){
        return this.data;
    }

    public byte[] toBytes(){
        return String.format("data: %s\n\n", this.data).getBytes();
    }

    public boolean isDone(){
        return DONE_DATA.equalsIgnoreCase(this.data);
    }
}