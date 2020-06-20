package helper;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Tag;

@Tag("all")
public abstract class TestHelper {

    protected static Faker faker = new Faker();
}
