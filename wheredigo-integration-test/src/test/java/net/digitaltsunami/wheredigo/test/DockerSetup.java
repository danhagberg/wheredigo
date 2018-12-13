package net.digitaltsunami.wheredigo.test;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ProjectName;
import com.palantir.docker.compose.connection.DockerPort;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import cucumber.api.java.sl.In;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class DockerSetup {

    public static DockerComposeRule docker;
    private static final String ELASTIC_SEARCH = "elasticsearch";
    private static final int ELASTIC_INFERNAL_WEB_PORT = 9200;
    private static final int ELASTIC_SEARCH_PORT = 9300;

    public static DockerComposeRule setupEnvironment() {

        if (docker == null) {
            docker = DockerComposeRule.builder()
                    .file("src/test/resources/docker-compose-elastic.yml")
                    .projectName(ProjectName.random())
                    .waitingForService(ELASTIC_SEARCH,
                            HealthChecks.toRespond2xxOverHttp(ELASTIC_INFERNAL_WEB_PORT,
                                    (port) -> port.inFormat("http://$HOST:$EXTERNAL_PORT")))
                    .build();
        }
        try {
            docker.before();

            Runnable shutdownDocker = () -> docker.after();
            Runtime.getRuntime().addShutdownHook(new Thread(shutdownDocker));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to start docker-compose", e);
        }
        return docker;
    }

    public static void initialize(ConfigurableApplicationContext applicationContext) {

        DockerPort dockerPort = DockerSetup.docker.containers()
                .container(ELASTIC_SEARCH)
                .port(ELASTIC_SEARCH_PORT);

        String newProp = String.format("wheredigo.elasticsearch.cluster-nodes=%s:%d",
                dockerPort.getIp(), dockerPort.getExternalPort());
        System.out.printf("New Prop:%s%n", newProp);
        TestPropertyValues values = TestPropertyValues.of(newProp);
        values.applyTo(applicationContext);
    }
}
