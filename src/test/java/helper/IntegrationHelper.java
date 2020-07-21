package helper;

import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/fixture/dataset.sql")
public abstract class IntegrationHelper extends TestHelper {

    @Autowired
    protected MockMvc mockMvc;

    protected static String[] args(String... items) {
        return items;
    }
}
