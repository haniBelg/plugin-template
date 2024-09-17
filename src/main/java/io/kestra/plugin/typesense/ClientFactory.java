package io.kestra.plugin.typesense;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.typesense.api.Client;
import org.typesense.api.Configuration;
import org.typesense.resources.Node;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;

@Factory
public class ClientFactory {

    @Bean
    public Client client(
        @Value("${typesense.protocol}") String protocol,
        @Value("${typesense.host}") String host,
        @Value("${typesense.port}") String port,
        @Value("${typesense.api-key}") String apiKey) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(
                new Node(
                        protocol, // For Typesense Cloud use https
                        host, // For Typesense Cloud use xxx.a1.typesense.net
                        port // For Typesense Cloud use 443
                ));

        Configuration configuration = new Configuration(nodes, Duration.ofSeconds(2), apiKey);

        return new Client(configuration);
    }
}
