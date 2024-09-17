package io.kestra.plugin.typesense;

import java.time.Duration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

public class TypesenseContainer extends GenericContainer<TypesenseContainer> {
    private final String protocol;
    private final String host;
    private final String port;
    private final String apiKey;

    public TypesenseContainer(String protocol,
            String host,
            String port,
            String apiKey) {
        super(DockerImageName.parse("typesense/typesense:27.0"));
        this.apiKey = apiKey;
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        withExposedPorts(Integer.parseInt(port));
        withEnv("TYPESENSE_API_KEY", this.apiKey);
        withEnv("TYPESENSE_DATA_DIR", "/tmp");

        setWaitStrategy(new HttpWaitStrategy()
                .forPort(Integer.parseInt(port))
                .forPath("/health")
                .withHeader("X-TYPESENSE-API-KEY", this.apiKey)
                .forStatusCode(200)
                .withStartupTimeout(Duration.ofMinutes(2)));
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getContainerUrl() {
        return String.format("http://%s:%d", getHost(), getMappedPort(Integer.parseInt(port)));
    }
}