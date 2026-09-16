package com.opentrivia.backend.controller.dto.quiz;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(
        UUID id,
        String type,
        String difficulty,
        String category,
        String question,
        List<String> answers
) { }
