package com.codeexplainer.core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class OllamaClient {

    private String model = "qwen2.5-coder:7b";
    private String endpoint = "http://localhost:11434/api/generate";
    private final HttpClient httpClient;

    public OllamaClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Uses an Ollama model to explain a given code snippet to a user
     * @param code selected by the user to be explained
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

        AppSettings settings = AppSettings.getInstance();
        if(settings != null && settings.getState() != null  && settings.getState().endpoint != null)
            this.endpoint = settings.getState().endpoint;
        if(settings != null && settings.getState() != null  && settings.getState().model != null)
            this.model = settings.getState().model;

        String body = "{\"model\":\"" + model + "\",\"prompt\":\"" + escapedPrompt + "\",\"stream\":false}";

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder().uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        } catch(Exception e) {
            return CompletableFuture.completedFuture("Invalid endpoint URL. Please check your settings.");
        }
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
