package org.seariver.kanbanboard.write.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.seariver.kanbanboard.write.domain.application.UpdateBucketCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Waiting full implementation")
@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "test.dataset=WriteBucketControllerIT")
class WriteBucketControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private Faker faker = new Faker();

    @Test
    void GIVEN_ValidData_MUST_UpdateBucket() throws Exception {

        // given
        var id = UUID.fromString("3731c747-ea27-42e5-a52b-1dfbfa9617db");
        double position = faker.number().randomDouble(5, 1, 10);
        var name = faker.pokemon().name();
        var command = new UpdateBucketCommand(id, position, name);

        // when
        mockMvc
            .perform(put("/v1/buckets/{uuid}", id)
                .contentType("application/json")
                .content(mapper.writeValueAsString(command)))
            .andExpect(status().isOk());

        // then
        mockMvc
            .perform(get("/v1/buckets"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$[*].id", containsInRelativeOrder(id, "6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e")))
            .andExpect(jsonPath("$[*].positon", containsInRelativeOrder(position, 100.15)))
            .andExpect(jsonPath("$[*].name", containsInRelativeOrder(name, "FIRST-BUCKET")));
    }
}