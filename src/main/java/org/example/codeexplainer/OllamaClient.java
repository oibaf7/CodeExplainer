package org.example.codeexplainer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OllamaClient {

    //to make configurable
    //model and endpoint
    public static final String MODEL = "qwen2.5-coder:7b";
    public static final String ENDPOINT = "http://localhost:11434/api/generate";
    public static final HttpClient httpClient = HttpClient.newHttpClient();

    public static CompletableFuture<String> explain(String code) {
        String prompt = "You are a code explanation assistant. " +
                "Explain the following code clearly and concisely. " +
                "Describe what it does, how it works, and any important patterns or concepts used. " +
                "Be direct and technical. Refer to relevant lines. Do not repeat the code back.\n\nCode:\n" + code;

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
                .thenApply(OllamaClient::parseJson);
    }

    public static String parseJson(String json) {
        int start = json.indexOf("\"response\":\"") + 12;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

}
