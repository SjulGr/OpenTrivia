package com.opentrivia.backend.client;

import com.opentrivia.backend.controller.dto.opentdb.OpenTdbResponse;
import com.opentrivia.backend.exception.OpenTdbException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenTdbClient {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://opentdb.com")
            .build();

    public OpenTdbResponse getQuestion(
            int amount,
            String category,
            String difficulty,
            String type
    ) {
        String uri = "/api.php";
        uri += "?amount=" + amount;
        if (!category.isEmpty()) uri += "&category=" + category;
        if (!difficulty.isEmpty()) uri += "&difficulty=" + difficulty;
        if (!type.isEmpty()) uri += "&type=" + type;

        try {
            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(OpenTdbResponse.class);
        } catch (Exception exception) {
            throw new OpenTdbException("Failed to retrieve questions from OpenTDB", exception);
        }
    }

}
