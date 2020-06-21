package helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import java.sql.SQLException;

@Tag("unit")
public abstract class DataSourceHelper extends TestHelper {

    protected BasicDataSource dataSource = new BasicDataSource();
    protected String dataSetName;

    @BeforeEach
    void init() {
        String url = "jdbc:h2:mem:UNIT_TEST;" +
            "MODE=PostgreSQL;" +
            "INIT=RUNSCRIPT FROM 'src/main/resources/db/migration/V001__Initial_setup.sql'\\;" +
            "RUNSCRIPT FROM 'classpath:fixture/" + dataSetName + ".sql'\\;";

        dataSource.setUrl(url);
        dataSource.setMaxTotal(1);
    }

    @AfterEach
    void terminate() throws SQLException {
        dataSource.close();
    }
}
