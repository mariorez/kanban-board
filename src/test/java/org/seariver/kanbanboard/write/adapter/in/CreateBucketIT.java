package org.seariver.kanbanboard.write.adapter.in;

import helper.IntegrationHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasSize;
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
    void GIVEN_InvalidData_MUST_ReturnBadRequest(String uuid, double position, String name) throws Exception {

        // given
        var payload = """
            {
                "id": "%s",
                "position": %s,
                "name": %s
            }
            """.formatted(uuid, position, name);

        // when
        mockMvc
            .perform(post("/v1/buckets")
                .contentType("application/json")
                .content(payload))
            .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidData() {

        var validUuid = UUID.randomUUID().toString();
        double validPosition = faker.number().randomDouble(5, 1, 10);
        var textGreatherThan100Chars = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras fringilla elit elementum, ullamcorper turpis consequat.";

        return Stream.of(
            /*arguments(null, validPosition, "WHATEVER"),
            arguments("", validPosition, "WHATEVER"),*/
            arguments(validUuid, -1, "WHATEVER"),
            arguments(validUuid, 0, "WHATEVER"),
            arguments(validUuid, validPosition, null),
            arguments(validUuid, validPosition, ""),
            arguments(validUuid, validPosition, "      "),
            arguments(validUuid, validPosition, textGreatherThan100Chars)
        );
    }
}