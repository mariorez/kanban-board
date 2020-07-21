package org.seariver.kanbanboard.write.adapter.in;

import helper.IntegrationHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BucketMoveIT extends IntegrationHelper {

    public static final String ENDPOINT_PATH = "/v1/buckets/{id}/move";

    @Test
    void GIVEN_ValidPayload_MUST_MoveSuccess() throws Exception {

        // given
        var uuid = "3731c747-ea27-42e5-a52b-1dfbfa9617db";
        var newPosition = faker.number().randomDouble(5, 1, 10);
        var payload = """
                {
                    "position": %s
                }
                """.formatted(newPosition);

        // when
        mockMvc
                .perform(put(ENDPOINT_PATH, uuid)
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNoContent());

        // then
        mockMvc
                .perform(get("/v1/buckets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[*].id", containsInRelativeOrder(uuid, "6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e")))
                .andExpect(jsonPath("$[*].position", containsInRelativeOrder(newPosition, 100.15)))
                .andExpect(jsonPath("$[*].name", containsInRelativeOrder("SECOND-BUCKET", "FIRST-BUCKET")));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    void GIVEN_InvalidData_MUST_ReturnBadRequest(String uuid,
                                                 double position,
                                                 String[] errorsFields,
                                                 String[] errorsDetails) throws Exception {
        // given
        var payload = """
                {
                    "position": %s
                }
                """.formatted(position);

        // when
        mockMvc
                .perform(put(ENDPOINT_PATH, uuid)
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Invalid field")))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder(errorsFields)))
                .andExpect(jsonPath("$.errors[*].detail", containsInAnyOrder(errorsDetails)));
    }

    private static Stream<Arguments> provideInvalidData() {

        var validUuid = UUID.randomUUID().toString();
        var validName = "WHATEVER";

        return Stream.of(
                arguments(validUuid, -1, args("position"), args("must be greater than 0")),
                arguments(validUuid, 0, args("position"), args("must be greater than 0"))
        );
    }
}