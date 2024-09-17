package io.kestra.plugin.typesense;

import java.time.Duration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

public class TypesenseContainer extends GenericContainer<TypesenseContainer> {
    private static final int CONTAINER_PORT = 8108;
    private static final String API_KEY = "xyz";

    public TypesenseContainer() {
        super(DockerImageName.parse("typesense/typesense:27.0"));
        withExposedPorts(CONTAINER_PORT);
        withEnv("TYPESENSE_API_KEY", API_KEY);
        withEnv("TYPESENSE_DATA_DIR", "/tmp");
        
        setWaitStrategy(new HttpWaitStrategy()
                .forPort(CONTAINER_PORT)
                .forPath("/health")
                .withHeader("X-TYPESENSE-API-KEY", API_KEY)
                .forStatusCode(200)
                .withStartupTimeout(Duration.ofMinutes(2)));
    }

    public String getApiKey() {
        return API_KEY;
    }

    public String getContainerUrl() {
        return String.format("http://%s:%d", getHost(), getMappedPort(CONTAINER_PORT));
    }
}