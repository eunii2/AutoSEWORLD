package org.backend.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.backend.Service.ConferenceDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conference")
public class GPTController {

    private final Dotenv dotenv = Dotenv.load();
    private static final Logger logger = LoggerFactory.getLogger(GPTController.class);
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final ConferenceDataLoader conferenceDataLoader;
    private final ObjectMapper objectMapper;

    public GPTController(ConferenceDataLoader conferenceDataLoader, ObjectMapper objectMapper) {
        this.conferenceDataLoader = conferenceDataLoader;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/extract")
    public List<Map<String, Object>> extractConferenceInfo(@RequestBody String text) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        String apiKey = dotenv.get("OPENAI_API_KEY");
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String prompt = "1. Analyze the text and first check for these conditions:\n" +
                "    - If the title or content contains 'CFP' or 'Call for Papers' (do not include 'Call for Proposals').\n" +
                "    - If the content is related to a conference, not a journal.\n\n" +
                "2. If both conditions are met, extract the following information:\n" +
                "- Conference Name (including full name and any abbreviation)\n" +
                "- Submission Deadline (format: YYYY-MM-DD)\n" +
                "- CFP URL\n" +
                "- Conference Location\n" +
                "- Conference Dates (format: YYYY-MM-DD to YYYY-MM-DD)\n\n" +
                "3. Ensure the output uses the following JSON format:\n" +
                "{\n" +
                "    \"is_cfp\": \"Y\",\n" +
                "    \"is_conference\": \"Y\",\n" +
                "    \"conference_name\": \"\",  // Include full name and abbreviation\n" +
                "    \"submission_deadline\": \"\",  // Use YYYY-MM-DD format\n" +
                "    \"cfp_url\": \"\",\n" +
                "    \"conference_location\": \"\",\n" +
                "    \"conference_dates\": \"\"  // Use YYYY-MM-DD to YYYY-MM-DD format\n" +
                "}\n\n" +
                "4. If the conditions are not met, return the following:\n" +
                "{\n" +
                "    \"is_cfp\": \"N\",\n" +
                "    \"is_conference\": \"N\"\n" +
                "}\n\n" +
                "Here is the text to analyze:\n" + text;

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", "gpt-4");
        requestBodyMap.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        requestBodyMap.put("max_tokens", 200);
        requestBodyMap.put("temperature", 0.7);

        try {
            String requestBody = objectMapper.writeValueAsString(requestBodyMap);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, request, String.class);

            // 응답 본문 로깅
            logger.info("Response Body: " + response.getBody());

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new ResponseStatusException(response.getStatusCode(), "Failed to fetch data from OpenAI API");
            }

            // OpenAI 응답에서 'message'에서 'content'를 가져옴
            Map<String, Object> responseBodyMap = objectMapper.readValue(response.getBody(), Map.class);
            Map<String, Object> choices = (Map<String, Object>) ((List<Object>) responseBodyMap.get("choices")).get(0);
            Map<String, Object> message = (Map<String, Object>) choices.get("message");

            // 'content'를 문자열로 받아옴
            String content = (String) message.get("content");

            // content가 JSON 형식일 경우에만 파싱
            if (content.trim().startsWith("{") && content.trim().endsWith("}")) {
                // 코드 블록이 있을 수 있으니 제거
                content = content.replace("```json", "").replace("```", "").trim();
                Map<String, Object> extractedData = objectMapper.readValue(content, Map.class);

                // 결과 리스트에 추가
                List<Map<String, Object>> outputList = new ArrayList<>();
                outputList.add(extractedData);

                return outputList;
            } else {
                // JSON 형식이 아닐 경우 원본 텍스트 그대로 반환 (또는 다른 처리를 할 수 있음)
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Response content is not in JSON format.");
            }

        } catch (Exception e) {
            logger.error("Error during OpenAI API call", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during OpenAI API call", e);
        }
    }

}