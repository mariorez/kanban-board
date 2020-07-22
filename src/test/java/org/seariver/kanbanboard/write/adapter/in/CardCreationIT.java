package org.seariver.kanbanboard.write.adapter.in;

import helper.IntegrationHelper;
import org.junit.jupiter.api.Test;
import org.seariver.kanbanboard.write.domain.core.WriteCardRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardCreationIT extends IntegrationHelper {

    private static final String ENDPOINT_PATH = "/v1/buckets/{bucketExternalId}/cards";

    @Autowired
    private WriteCardRepository repository;

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

        // then
        var newCard = repository.findByExternalId(UUID.fromString(externalId)).get();
        assertThat(newCard.getBucketId()).isEqualTo(2);
        assertThat(newCard.getPosition()).isEqualTo(position);
        assertThat(newCard.getName()).isEqualTo(name);
    }
}
