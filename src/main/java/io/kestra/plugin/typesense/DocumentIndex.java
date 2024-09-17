package io.kestra.plugin.typesense;

import java.util.HashMap;
import java.util.Map;

import org.typesense.api.Client;

import io.kestra.core.models.tasks.RunnableTask;
import io.kestra.core.models.tasks.Task;
import io.kestra.core.runners.RunContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class DocumentIndex extends Task implements RunnableTask<DocumentIndex.Output> {

    @Builder
    @Getter
    public static class Output implements io.kestra.core.models.tasks.Output {
        @Schema(title = "document after index", description = "document after index")
        private final Map<String, Object> document;
    }

    Client client;

    @Override
    public DocumentIndex.Output run(RunContext runContext) throws Exception {
        HashMap<String, Object> document = (HashMap<String, Object>) runContext.getVariables().get("document");
        String index = (String) runContext.getVariables().get("index");
        return DocumentIndex.Output
                .builder()
                .document(client.collections(index).documents().create(document))
                .build();
    }

}
