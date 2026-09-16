package com.opentrivia.backend.controller.dto.answer;

import java.util.UUID;

public record AnswerResult(
        UUID id,
        String selected_answer,
        boolean is_correct
) { }
