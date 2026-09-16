package com.opentrivia.backend.controller.dto.answer;

import java.util.List;
import java.util.UUID;

public record AnswerResponse(
        UUID id,
        List<AnswerResult> results
) { }
