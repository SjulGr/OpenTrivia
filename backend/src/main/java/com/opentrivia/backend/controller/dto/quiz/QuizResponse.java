package com.opentrivia.backend.controller.dto.quiz;

import java.util.List;
import java.util.UUID;

public record QuizResponse(
        UUID id,
        int response_code,
        List<QuestionResponse> questions
) { }
