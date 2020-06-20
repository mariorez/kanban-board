package org.seariver.kanbanboard.read.adapter.in;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "test.dataset=ReadBucketControllerIT")
class ReadBucketControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void WHEN_GetAllBuckets_MUST_ListByPositionOrder() throws Exception {

        mockMvc
            .perform(get("/v1/buckets"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$[*].id", containsInRelativeOrder("6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e", "3731c747-ea27-42e5-a52b-1dfbfa9617db")))
            .andExpect(jsonPath("$[*].position", containsInRelativeOrder(100.15, 200.987)))
            .andExpect(jsonPath("$[*].name", containsInRelativeOrder("FIRST-BUCKET", "SECOND-BUCKET")));
    }
}
