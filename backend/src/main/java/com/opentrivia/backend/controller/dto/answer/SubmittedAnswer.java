package com.opentrivia.backend.controller.dto.answer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SubmittedAnswer(
        UUID id,
        String selected_answer
) { }
