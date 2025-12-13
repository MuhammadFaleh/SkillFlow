package org.example.skillflow.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey;

    public AiService(@Value("${openai.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    private String askChat(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);
        messages.add(userMsg);

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);

        Map responseBody = responseEntity.getBody();
        if (responseBody == null) {
            return "AI did not return a response.";
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (choices == null || choices.isEmpty()) {
            return "AI returned no choices.";
        }

        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        if (message == null) {
            return "AI returned an empty message.";
        }

        Object content = message.get("content");
        return content != null ? content.toString() : "AI returned no content.";

    }

    public String getRejectNoteForEmail( String notes ) {
        String prompt = """
        ROLE:
        You are an HR manager reviewing employee requests to add new training request to the system.
                
        TASK:
        Write a rejection email message to employee based on the manager's rejection note.
                
        CONSTRAINTS:     
        Be polite and professional
        Keep the message short (2–4 sentences)
        Do not mention internal system rules
        
                
        INPUT (Manager's rejection note):
        "%s"
                
        OUTPUT:
        A short, polite rejection message suitable for email do not include names just say dera employee and the end say best regards HR manger.
                """.formatted(notes);

        return askChat(prompt);
    }

    public String getRejectSkillForEmail(String notes) {
        String prompt = """
        ROLE:
        You are an HR manager reviewing employee requests to add skill to employee in the system.
                
        TASK:
        Write a rejection email message to an employee based on the manager's rejection note.
                
        CONSTRAINTS:     
        Be polite and professional
        Keep the message short (2–4 sentences)
        Do not mention internal system rules
        
                
        INPUT (Manager's rejection note):
        "%s"
                
        OUTPUT:
        A short, polite rejection message suitable for email.
                """.formatted(notes);

        return askChat(prompt);
    }

    public String getRejectEnrollForEmail(String notes) {
        String prompt = """
        ROLE:
        You are an HR manager reviewing employee requests to enroll in a training for a skill in the company.
        in the company they are training employees to improve their skills so company provides training and the employee can request to enroll in a training.
        
                
        TASK:
        Write a rejection email message to an employee based on the manager's rejection note.
                
        CONSTRAINTS:     
        Be polite and professional
        Keep the message short (2–4 sentences)
        Do not mention internal system rules
        
                
        INPUT (Manager's rejection note):
        "%s"
                
        OUTPUT:
        A short, polite rejection message suitable for email.
                """.formatted(notes);

        return askChat(prompt);
    }

    public String getCompleteTrainingSessionForEmail(String description) {
        String prompt = """
        ROLE:
        You are an HR manager sending automation messages to employees who completed training session in company.
        
                
        TASK:
        Write a completion email message to an employee.
                
        CONSTRAINTS:     
        Be polite and professional
        Keep the message short (2–4 sentences)
        Do not mention internal system rules
        
                
        INPUT (Training description):
        "%s"
                
        OUTPUT:
        A short, polite completion message suitable for email do not include names just say dera employee and the end say best regards HR manger.
                """.formatted(description);

        return askChat(prompt);
    }
}