package com.opentrivia.backend.model;

import java.util.List;
import java.util.UUID;

public record StoredQuiz(
        UUID id,
        int response_code,
        List<StoredQuestion> questions
) { }
