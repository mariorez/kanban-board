package org.seariver.kanbanboard.write.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.seariver.kanbanboard.write.domain.application.UpdateBucketCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WriteBucketControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void GIVEN_ValidData_MUST_UpdateBucket() throws Exception {

        // given
        var position = 200.25;
        var name = "SECOND-BUCKET";
        var id = UUID.fromString("6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e");
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
            .andExpect(jsonPath("$[*].name", containsInRelativeOrder("EXISTENT", "SECOND-BUCKET")));
    }
}