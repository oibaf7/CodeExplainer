package com.codeexplainer;



import com.codeexplainer.core.OllamaClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OllamaClientTest {

    OllamaClient ollamaClient;
    HttpClient httpClient;

    @BeforeEach
    void initialize() {
        this.httpClient = mock(HttpClient.class);
        this.ollamaClient = new OllamaClient(httpClient);
    }

    @Test
    void testParseJsonWithQuotes() {
        String json = "{\"model\":\"qwen2.5-coder:7b\",\"created_at\":\"2026-04-27T13:45:54.3873085Z\",\"response\":\"\\\"Hello there!\\\" I said with a smile.\",\"done\":true,\"done_reason\":\"stop\",\"context\":[151644,8948,198,2610,525,1207,16948,11,3465,553,54364,14817,13,1446,525,264,10950,17847,13,151645,198,151644,872,198,31115,752,264,2033,448,264,1990,12641,304,697,2033,0,151645,198,151644,77091,198,1,9707,1052,8958,358,1053,448,264,15289,13],\"total_duration\":1804491700,\"load_duration\":91846000,\"prompt_eval_count\":41,\"prompt_eval_duration\":1106229300,\"eval_count\":11,\"eval_duration\":590076600}";
        assertEquals("\\\"Hello there!\\\" I said with a smile.", ollamaClient.parseJson(json));
    }

    @Test
    void testParseJsonWithBackslash() {
        String json = "{\"model\":\"qwen2.5-coder:7b\",\"created_at\":\"2026-04-27T13:47:36.2561024Z\",\"response\":\"Here's a playful message with a backslash:\\n\\n\\\"Escaping to the next level!\\\\\\\\\\\"\",\"done\":true,\"done_reason\":\"stop\",\"context\":[151644,8948,198,2610,525,1207,16948,11,3465,553,54364,14817,13,1446,525,264,10950,17847,13,151645,198,151644,872,198,31115,752,264,2033,448,264,1182,50256,0,151645,198,151644,77091,198,8420,594,264,56069,1943,448,264,1182,50256,1447,1,36121,14216,311,279,1790,2188,14771,2105],\"total_duration\":12006775100,\"load_duration\":88167400,\"prompt_eval_count\":38,\"prompt_eval_duration\":11004418700,\"eval_count\":20,\"eval_duration\":894073500}";
        assertEquals("Here's a playful message with a backslash:\\n\\n\\\"Escaping to the next level!\\\\\\\\\\\"", ollamaClient.parseJson(json));
    }

    @Test
    void testParseJsonWithSampleResponse() {
        String json = "{\"model\":\"qwen2.5-coder:7b\",\"created_at\":\"2026-04-27T13:41:53.0992959Z\",\"response\":\"Hello! How can I assist you today?\",\"done\":true,\"done_reason\":\"stop\",\"context\":[151644,8948,198,2610,525,1207,16948,11,3465,553,54364,14817,13,1446,525,264,10950,17847,13,151645,198,151644,872,198,9707,0,151645,198,151644,77091,198,9707,0,2585,646,358,7789,498,3351,30],\"total_duration\":4340022100,\"load_duration\":3988757700,\"prompt_eval_count\":31,\"prompt_eval_duration\":95791000,\"eval_count\":10,\"eval_duration\":215035100}";
        assertEquals("Hello! How can I assist you today?", ollamaClient.parseJson(json));
    }

    @Test
    void testInvalidJson() {
        String json = "{error}";
        assertEquals("Wrong response format! Verify the AI model is running at the right port!",
                ollamaClient.parseJson(json));
    }
}