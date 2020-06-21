package org.seariver.kanbanboard.write.adapter.in;

import helper.IntegrationHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "test.dataset=WriteBucketControllerIT")
class WriteBucketControllerIT extends IntegrationHelper {

    @Test
    void GIVEN_ValidData_MUST_CreateBucket() throws Exception {
        // given
        var id = UUID.randomUUID();
        var position = faker.number().randomDouble(5, 1, 10);
        var name = faker.pokemon().name();
        var payload = """
            {
                "id": "%s",
                "position": %s,
                "name": "%s"
            }
            """.formatted(id, position, name);

        // when
        mockMvc
            .perform(post("/v1/buckets")
                .contentType("application/json")
                .content(payload))
            .andExpect(status().isCreated());

        // then
        mockMvc
            .perform(get("/v1/buckets"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$[*].id",
                containsInRelativeOrder(id.toString(), "6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e", "3731c747-ea27-42e5-a52b-1dfbfa9617db")))
            .andExpect(jsonPath("$[*].position",
                containsInRelativeOrder(position, 100.15, 200.987)))
            .andExpect(jsonPath("$[*].name", containsInRelativeOrder(name, "FIRST-BUCKET", "SECOND-BUCKET")));
    }

    @Test
    void GIVEN_InvalidData_MUST_ReturnError() throws Exception {
        // given
        var id = UUID.randomUUID();
        var position = faker.number().randomDouble(5, 1, 10);
        var name = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras fringilla elit elementum, ullamcorper turpis consequat.";
        var payload = """
            {
                "id": "%s",
                "position": %s,
                "name": "%s"
            }
            """.formatted(id, position, name);

        // when
        mockMvc
            .perform(post("/v1/buckets")
                .contentType("application/json")
                .content(payload))
            .andExpect(status().isBadRequest());
    }


    @Disabled("Waiting full implementation")
    @Test
    void GIVEN_ValidData_MUST_UpdateBucket() throws Exception {

        // given
        var id = UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db");
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
            .perform(put("/v1/buckets/{uuid}", id)
                .contentType("application/json")
                .content(payload))
            .andExpect(status().isOk());

        // then
        mockMvc
            .perform(get("/v1/buckets"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$[*].id", containsInRelativeOrder(id, "6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e")))
            .andExpect(jsonPath("$[*].position", containsInRelativeOrder(position, 100.15)))
            .andExpect(jsonPath("$[*].name", containsInRelativeOrder(name, "FIRST-BUCKET")));
    }
}