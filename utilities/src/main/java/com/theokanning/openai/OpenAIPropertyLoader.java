package com.theokanning.openai;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class OpenAIPropertyLoader {
    public static String loadProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("openai.properties");
            properties.load(input);
            return properties.getProperty("token");
        }
        catch (IOException e) {
            System.out.println(" Either 1) openai.properties file not found or 2) the token variable is not set in " +
                    "in the file. Please create an 'openai.properties' file in your project if it doesn't exist" +
                    "or set the token property in it to your api key." + e.getMessage());
            System.out.println("Checking to see if API key is set in environment variables...");
            return System.getenv("OPENAI_TOKEN");
        }
    }
}