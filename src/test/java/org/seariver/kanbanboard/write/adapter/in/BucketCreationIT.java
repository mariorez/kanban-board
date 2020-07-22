package org.seariver.kanbanboard.write.adapter.in;

import helper.IntegrationHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BucketCreationIT extends IntegrationHelper {

    private static final String ENDPOINT_PATH = "/v1/buckets";

    @Autowired
    private WriteBucketRepository repository;


    @Test
    void GIVEN_ValidPayload_MUST_ReturnCreated() throws Exception {

        // given
        var externalId = UUID.randomUUID().toString();
        var position = faker.number().randomDouble(5, 1, 10);
        var name = faker.pokemon().name();
        var payload = """
                {
                    "id": "%s",
                    "position": %s,
                    "name": "%s"
                }
                """.formatted(externalId, position, name);

        // when
        mockMvc
                .perform(post(ENDPOINT_PATH)
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isCreated());

        // then
        var newBucket = repository.findByExteranlId(UUID.fromString(externalId)).get();
        assertThat(newBucket.getName()).isEqualTo(name);
        assertThat(newBucket.getPosition()).isEqualTo(position);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    void GIVEN_InvalidData_MUST_ReturnBadRequest(String externalId,
                                                 double position,
                                                 String name,
                                                 String[] errorsFields,
                                                 String[] errorsDetails) throws Exception {
        // given
        name = name == null ? null : String.format("\"%s\"", name);
        externalId = externalId == null ? null : String.format("\"%s\"", externalId);
        var payload = """
                {
                    "id": %s,
                    "position": %s,
                    "name": %s
                }
                """.formatted(externalId, position, name);

        // when
        mockMvc
                .perform(post(ENDPOINT_PATH)
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Invalid field")))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder(errorsFields)))
                .andExpect(jsonPath("$.errors[*].detail", containsInAnyOrder(errorsDetails)));
    }

    @Test
    void GIVEN_MalformedJson_MUST_ReturnBadRequest() throws Exception {

        // given
        var malformedJson = """
                {
                    "id": "786d35e5-83b0-4a1e-96de-5920cbe9180e"
                    position: 10.5,
                    "name": "WHATEVER"
                """;

        // when
        mockMvc
                .perform(post(ENDPOINT_PATH)
                        .contentType("application/json")
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Malformed JSON")));
    }

    @Test
    void GIVEN_DuplicatedKey_MUST_ReturnBadRequest() throws Exception {

        var duplicatedExternalId = "3731c747-ea27-42e5-a52b-1dfbfa9617db";
        var position = 100.15;

        // given
        var malformedJson = """
                {
                    "id": "%s",
                    "position": %s,
                    "name": "WHATEVER"
                }
                """.formatted(duplicatedExternalId, position);

        // when
        mockMvc
                .perform(post(ENDPOINT_PATH)
                        .contentType("application/json")
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Invalid field")))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("code")))
                .andExpect(jsonPath("$.errors[*].detail", containsInAnyOrder("1000")));
    }

    private static Stream<Arguments> provideInvalidData() {

        var validExternalId = UUID.randomUUID().toString();
        var validPosition = faker.number().randomDouble(5, 1, 10);
        var validName = "WHATEVER";
        var invalidTextGreatherThan100Chars = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras fringilla elit elementum, ullamcorper turpis consequat.";

        return Stream.of(
                arguments(null, validPosition, validName, args("externalId"), args("must not be null")),
                arguments("", validPosition, validName, args("externalId"), args("invalid UUID format")),
                arguments(validExternalId, -1, validName, args("position"), args("must be greater than 0")),
                arguments(validExternalId, 0, validName, args("position"), args("must be greater than 0")),
                arguments(validExternalId, validPosition, null, args("name"), args("must not be blank")),
                arguments(validExternalId, validPosition, "", args("name", "name"), args("must not be blank", "size must be between 1 and 100")),
                arguments(validExternalId, validPosition, "      ", args("name"), args("must not be blank")),
                arguments(validExternalId, validPosition, invalidTextGreatherThan100Chars, args("name"), args("size must be between 1 and 100"))
        );
    }
}