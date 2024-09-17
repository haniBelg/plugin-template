package io.kestra.plugin.typesense;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.typesense.api.Client;

import com.google.common.collect.ImmutableMap;

import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.runners.RunContext;
import io.kestra.core.runners.RunContextFactory;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@KestraTest
@MicronautTest
@Testcontainers
class DocumentIndexTest {

        @Container
        public static TypesenseContainer typesense = new TypesenseContainer();

        @Inject
        Client client;

        @Inject
        private RunContextFactory runContextFactory;

        @Test
        void run() throws Exception {
                HashMap<String, Object> document = new HashMap<>();
                document.put("id", "124");
                document.put("company_name", "Stark Industries");
                document.put("num_employees", 5215);
                document.put("country", "USA");
                RunContext runContext = runContextFactory.of(ImmutableMap.of(
                                "document", document,
                                "index", "companies"));

                DocumentIndex task = DocumentIndex.builder()
                                .client(client)
                                .build();

                DocumentIndex.Output runOutput = task.run(runContext);

                assertEquals(runOutput.getDocument(), document);
        }
}
