package helper;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSourceHelper extends BasicDataSource {

    public DataSourceHelper() {

        String url = "jdbc:h2:mem:DATABASE_TEST;" +
                "MODE=PostgreSQL;" +
                "INIT=RUNSCRIPT FROM 'src/main/resources/db/migration/V001__Initial_setup.sql'\\;" +
                "RUNSCRIPT FROM 'classpath:fixture/dataset.sql'\\;";

        this.setUrl(url);
    }
}
