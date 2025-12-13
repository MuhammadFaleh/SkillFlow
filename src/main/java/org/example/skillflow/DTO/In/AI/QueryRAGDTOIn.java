package org.example.skillflow.DTO.In.AI;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRAGDTOIn {
    @NotBlank(message = "question must not be empty")
    private String question;
}
