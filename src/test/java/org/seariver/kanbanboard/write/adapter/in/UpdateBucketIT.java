package org.seariver.kanbanboard.write.adapter.in;

import helper.IntegrationHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
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

class UpdateBucketIT extends IntegrationHelper {

    @Test
    void GIVEN_ValidPayload_MUST_ReturnSuccess() throws Exception {

        // given
        var uuid = "3731c747-ea27-42e5-a52b-1dfbfa9617db";
        var position = faker.number().randomDouble(5, 1, 10);
        var name = faker.pokemon().name();
        var payload = """
            {
                "position": %s,
                "name": "%s"
            }
            """.formatted(position, name);

        // when
        mockMvc
            .perform(put("/v1/buckets/{id}", uuid)
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
            .andExpect(jsonPath("$[*].position", containsInRelativeOrder(position, 100.15)))
            .andExpect(jsonPath("$[*].name", containsInRelativeOrder(name, "FIRST-BUCKET")));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    void GIVEN_InvalidData_MUST_ReturnBadRequest(String uuid,
                                                 double position,
                                                 String name,
                                                 List<String> errorsFields,
                                                 List<String> errorsDetails) throws Exception {
        // given
        name = name == null ? null : String.format("\"%s\"", name);
        var payload = """
            {
                "position": %s,
                "name": %s
            }
            """.formatted(position, name);

        // when
        mockMvc
            .perform(put("/v1/buckets/{id}", uuid)
                .contentType(APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Invalid field")))
            .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder(errorsFields.toArray(new String[0]))))
            .andExpect(jsonPath("$.errors[*].detail", containsInAnyOrder(errorsDetails.toArray(new String[0]))));
    }

    private static Stream<Arguments> provideInvalidData() {

        var validUuid = UUID.randomUUID().toString();
        var validPosition = faker.number().randomDouble(5, 1, 10);
        var validName = "WHATEVER";
        var invalidTextGreatherThan100Chars = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras fringilla elit elementum, ullamcorper turpis consequat.";

        return Stream.of(
            arguments("null", validPosition, validName, List.of("id"), List.of("null")),
            arguments("    ", validPosition, validName, List.of("code"), List.of("1001")),
            arguments(validUuid, -1, validName, List.of("position"), List.of("must be greater than 0")),
            arguments(validUuid, 0, validName, List.of("position"), List.of("must be greater than 0")),
            arguments(validUuid, validPosition, null, List.of("name"), List.of("must not be blank")),
            arguments(validUuid, validPosition, "", List.of("name", "name"), List.of("must not be blank", "size must be between 1 and 100")),
            arguments(validUuid, validPosition, "      ", List.of("name"), List.of("must not be blank")),
            arguments(validUuid, validPosition, invalidTextGreatherThan100Chars, List.of("name"), List.of("size must be between 1 and 100"))
        );
    }
}