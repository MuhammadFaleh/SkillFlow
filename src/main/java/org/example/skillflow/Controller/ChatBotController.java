package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.DTO.In.AI.EmployeeMatchDTOIn;
import org.example.skillflow.DTO.In.AI.QueryRAGDTOIn;
import org.example.skillflow.DTO.In.AI.SkillRecommendDTOIn;
import org.example.skillflow.DTO.In.AI.TrainRecommendDTOIn;
import org.example.skillflow.DTO.Out.QueryDTOOut;
import org.example.skillflow.Service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatBotController {

    private final AiService chatBotService;


    @PostMapping("/ask-rag")
    public ResponseEntity<?> askRAG(@RequestBody @Valid QueryRAGDTOIn question) {

        QueryDTOOut response = chatBotService.askRAG(question);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/recommend-skills")
    public ResponseEntity<?> recommendSkills(@RequestBody @Valid SkillRecommendDTOIn question) {
        QueryDTOOut response = chatBotService.recommendSkill(question);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/recommend-training")
    public ResponseEntity<?> recommendTraining(@RequestBody @Valid TrainRecommendDTOIn question) {
        QueryDTOOut response = chatBotService.recommendTraining(question);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/match-employee")
    public ResponseEntity<?> matchEmployee(@RequestBody @Valid EmployeeMatchDTOIn question) {
        QueryDTOOut response = chatBotService.employeeMatch(question);
        return ResponseEntity.status(200).body(response);
    }
}
