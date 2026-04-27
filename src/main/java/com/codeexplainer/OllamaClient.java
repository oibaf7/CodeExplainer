package com.codeexplainer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class OllamaClient {

    //to make configurable
    //model and endpoint
    public static final String MODEL = "qwen2.5-coder:7b";
    public static final String ENDPOINT = "http://localhost:11434/api/generate";
    public HttpClient httpClient;

    public OllamaClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Uses an Ollama model to explain a given code snippet to a user
     * @param code selected by user to be explained
     * @return a CompletableFuture with the output response after having been parsed
     */
    public CompletableFuture<String> explain(String code) {
        String prompt = "You are a code explanation assistant. Explain the following code clearly and concisely. " +
                "Be direct and technical. Refer to relevant lines. Do not repeat the code back. " +
                "Respond with inner HTML only using <p>, <b>, <code>, <pre>, <ul>, <li> tags. " +
                "No markdown, no <html>/<head>/<body> tags." +
                "Use <code> tags instead of backtick characters.\n\nCode:\n" + code;

        String escapedPrompt = prompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        String body = "{\"model\":\"" + MODEL + "\",\"prompt\":\"" + escapedPrompt + "\",\"stream\":false}";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parseJson);
    }

    /**
     * Parses the response from the Ollama API response
     * Relies on the Ollama response format
     * @param json Ollama API response
     * @return the actual response of the model without extra information
     */
    public String parseJson(String json) {
        if(!json.contains("response") || !json.contains("done")) {
            return "Wrong response format! Verify the AI model is running at the right port!";
        }
        int start = json.indexOf("\"response\":\"") + 12;
        int end = json.indexOf("\",\"done\"", start);
        return json.substring(start, end).replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\u003c", "<")
                .replace("\\u003e", ">");
    }

}
