package com.opentrivia.backend.model;

import java.util.List;
import java.util.UUID;

public record StoredQuestion(
        UUID id,
        String type,
        String difficulty,
        String category,
        String question,
        String correctAnswer,
        List<String> incorrectAnswers
) { }
