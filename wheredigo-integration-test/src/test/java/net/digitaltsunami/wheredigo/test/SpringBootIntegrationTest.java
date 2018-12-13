package net.digitaltsunami.wheredigo.test;

import com.palantir.docker.compose.DockerComposeRule;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.digitaltsunami.wheredigo.WheredigoApiApplication;
import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;


@SpringBootTest(classes = WheredigoApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = SpringBootIntegrationTest.Initializer.class)

@Getter
public class SpringBootIntegrationTest {
    protected static final String BASE_PATH = "api/v1/wheredigo";
    private static final String ELASTIC_SEARCH = "elasticsearch";
    private static final int ELASTIC_SEARCH_PORT = 9300;
    public static DockerComposeRule docker;
    @Autowired
    public World world;

    @LocalServerPort
    private int port;
    private RequestSpecification requestSpec;


    public SpringBootIntegrationTest() {
        this.requestSpec = new RequestSpecBuilder().build();
    }

    @ClassRule
    public static TestRule classRule() {
        return DockerSetup.setupEnvironment();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            DockerSetup.setupEnvironment();
            DockerSetup.initialize(applicationContext);
        }
    }
}
