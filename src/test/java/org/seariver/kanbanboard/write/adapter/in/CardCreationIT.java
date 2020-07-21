package org.seariver.kanbanboard.write.adapter.in;

import helper.IntegrationHelper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardCreationIT extends IntegrationHelper {

    public static final String ENDPOINT_PATH = "/v1/buckets/{bucketExternalId}/cards";

    @Test
    void GIVEN_ValidPayload_MUST_ReturnCreated() throws Exception {

        // given
        var bucketExternalId = "6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e";
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
                .perform(post(ENDPOINT_PATH, bucketExternalId)
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isCreated());
    }
}
