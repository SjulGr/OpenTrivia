package com.opentrivia.backend.controller.dto.opentdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenTdbResponse(
        int response_code,
        List<OpenTdbQuestion> results
) { }
