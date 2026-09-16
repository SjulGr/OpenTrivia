package com.opentrivia.backend.controller.dto.opentdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenTdbQuestion(
        String type,
        String difficulty,
        String category,
        String question,
        String correct_answer,
        List<String> incorrect_answers
) { }
