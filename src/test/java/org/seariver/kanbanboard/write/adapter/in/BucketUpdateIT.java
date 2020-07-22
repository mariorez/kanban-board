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

class BucketUpdateIT extends IntegrationHelper {

    private static final String ENDPOINT_PATH = "/v1/buckets/{id}";

    @Test
    void GIVEN_ValidPayload_MUST_ReturnSuccess() throws Exception {

        // given
        var externalId = "3731c747-ea27-42e5-a52b-1dfbfa9617db";
        var name = faker.pokemon().name();
        var payload = """
                {
                    "name": "%s"
                }
                """.formatted(name);

        // when
        mockMvc
                .perform(put(ENDPOINT_PATH, externalId)
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNoContent());

        // then
        mockMvc
                .perform(get("/v1/buckets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[*].id", containsInRelativeOrder("6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e", externalId)))
                .andExpect(jsonPath("$[*].position", containsInRelativeOrder(100.15, 200.987)))
                .andExpect(jsonPath("$[*].name", containsInRelativeOrder("FIRST-BUCKET", name)));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    void GIVEN_InvalidData_MUST_ReturnBadRequest(String externalId,
                                                 String name,
                                                 String[] errorsFields,
                                                 String[] errorsDetails) throws Exception {
        // given
        name = name == null ? null : String.format("\"%s\"", name);
        var payload = """
                {
                    "name": %s
                }
                """.formatted(name);

        // when
        mockMvc
                .perform(put(ENDPOINT_PATH, externalId)
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Invalid field")))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder(errorsFields)))
                .andExpect(jsonPath("$.errors[*].detail", containsInAnyOrder(errorsDetails)));
    }

    private static Stream<Arguments> provideInvalidData() {

        var validExternalId = UUID.randomUUID().toString();
        var validName = "WHATEVER";
        var invalidTextGreatherThan100Chars = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras fringilla elit elementum, ullamcorper turpis consequat.";

        return Stream.of(
                arguments("null", validName, args("externalId"), args("invalid UUID format")),
                arguments("    ", validName, args("externalId"), args("invalid UUID format")),
                arguments(validExternalId, null, args("name"), args("must not be blank")),
                arguments(validExternalId, "", args("name", "name"), args("must not be blank", "size must be between 1 and 100")),
                arguments(validExternalId, "      ", args("name"), args("must not be blank")),
                arguments(validExternalId, invalidTextGreatherThan100Chars, args("name"), args("size must be between 1 and 100"))
        );
    }
}