package io.kestra.plugin.chtijug;

import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.annotations.PluginProperty;
import io.kestra.core.models.tasks.RunnableTask;
import io.kestra.core.models.tasks.Task;
import io.kestra.core.runners.RunContext;
import io.kestra.core.serializers.JacksonMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

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
public class HttpPost extends Task implements RunnableTask<HttpPost.Output> {
    @Schema(
        title = "The URL"
    )
    @PluginProperty(dynamic = true) // If the variables will be rendered with template {{ }}
    private String url;

    @Schema(
        title = "The content"
    )
    @PluginProperty(dynamic = true)
    private Object content;

    @Override
    public HttpPost.Output run(RunContext runContext) throws Exception {
        Logger logger = runContext.logger();

        String render = runContext.render(url);

        String body;
        if (content instanceof String s) {
            body = runContext.render(s);
        }
        else if (content instanceof Map map) {
            var renderedMap = runContext.render((Map<String, Object>) map);
            body = JacksonMapper.ofJson().writeValueAsString(renderedMap);
        }
        else {
            throw new IllegalArgumentException();
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        logger.info("Post " + render);
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
