package io.kestra.plugin.typesense;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.typesense.api.Client;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionResponse;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;

import com.google.common.collect.ImmutableMap;

import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.runners.RunContext;
import io.kestra.core.runners.RunContextFactory;
import jakarta.inject.Inject;

@KestraTest
class DocumentIndexTest {

        @Inject
        Client client;

        @BeforeEach
        void setup() throws Exception {
                CollectionSchema collectionSchema = new CollectionSchema();
                collectionSchema.name("companies")
                                .addFieldsItem(new Field().name(".*").type(FieldTypes.AUTO));
                CollectionResponse collectionResponse = client.collections().create(collectionSchema);
        }

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
