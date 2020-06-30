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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateBucketIT extends IntegrationHelper {

    @Test
    void GIVEN_ValidPayload_MUST_ReturnCreated() throws Exception {

        // given
        var uuid = UUID.randomUUID().toString();
        var position = faker.number().randomDouble(5, 1, 10);
        var name = faker.pokemon().name();
        var payload = """
            {
                "id": "%s",
                "position": %s,
                "name": "%s"
            }
            """.formatted(uuid, position, name);

        // when
        mockMvc
            .perform(post("/v1/buckets")
                .contentType("application/json")
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/v1/buckets/%s", uuid)));

        // then
        mockMvc
            .perform(get("/v1/buckets"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.*", hasSize(3)))
            .andExpect(jsonPath("$[*].id",
                containsInRelativeOrder(uuid, "6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e", "3731c747-ea27-42e5-a52b-1dfbfa9617db")))
            .andExpect(jsonPath("$[*].position",
                containsInRelativeOrder(position, 100.15, 200.987)))
            .andExpect(jsonPath("$[*].name", containsInRelativeOrder(name, "FIRST-BUCKET", "SECOND-BUCKET")));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    void GIVEN_InvalidData_MUST_ReturnBadRequest(String uuid,
                                                 double position,
                                                 String name,
                                                 List<String> errorsFields,
                                                 List<String> errorsDetails) throws Exception {
        // given
        name = name == null ? name : String.format("\"%s\"", name);
        uuid = uuid == null ? uuid : String.format("\"%s\"", uuid);
        var payload = """
            {
                "id": %s,
                "position": %s,
                "name": %s
            }
            """.formatted(uuid, position, name);

        // when
        mockMvc
            .perform(post("/v1/buckets")
                .contentType("application/json")
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
            arguments(null, validPosition, validName, List.of("uuid"), List.of("must not be null")),
            arguments("", validPosition, validName, List.of("uuid"), List.of("must not be null")),
            arguments(validUuid, -1, validName, List.of("position"), List.of("must be greater than 0")),
            arguments(validUuid, 0, validName, List.of("position"), List.of("must be greater than 0")),
            arguments(validUuid, validPosition, null, List.of("name"), List.of("must not be blank")),
            arguments(validUuid, validPosition, "", List.of("name", "name"), List.of("must not be blank", "size must be between 1 and 100")),
            arguments(validUuid, validPosition, "      ", List.of("name"), List.of("must not be blank")),
            arguments(validUuid, validPosition, invalidTextGreatherThan100Chars, List.of("name"), List.of("size must be between 1 and 100"))
        );
    }
}