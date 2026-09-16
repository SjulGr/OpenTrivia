package com.opentrivia.backend.controller.dto.answer;

import java.util.List;
import java.util.UUID;

public record AnswerRequest(
        UUID id,
        List<SubmittedAnswer> questions
) { }
