package io.kestra.plugin.chtijug;

import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import io.kestra.core.models.annotations.PluginProperty;
import io.kestra.core.models.tasks.RunnableTask;
import io.kestra.core.models.tasks.Task;
import io.kestra.core.runners.RunContext;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SuperBuilder
@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Schema(
    title = "Plugin example"
)
@Plugin(
    examples = {
        @Example(
            title = "Plugin example",
            code = {
                """
                    TODO"""
            }
        )
    }
)
public class HttpGet extends Task implements RunnableTask<HttpGet.Output> {
    @Schema(
        title = "The URL"
    )
    @PluginProperty(dynamic = true) // If the variables will be rendered with template {{ }}
    private String url;

    @Override
    public HttpGet.Output run(RunContext runContext) throws Exception {
        Logger logger = runContext.logger();

        String render = runContext.render(url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
            .GET()
            .build();
        logger.info("Get " + render);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return Output.builder()
            .response(response.body())
            .build();
    }

    /**
     * Input or Output can be nested as you need
     */
    @Builder
    @Getter
    public static class Output implements io.kestra.core.models.tasks.Output {
        @Schema(
            title = "The response"
        )
        private final String response;
    }
}
