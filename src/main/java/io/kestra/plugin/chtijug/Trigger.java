package io.kestra.plugin.chtijug;

import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.conditions.ConditionContext;
import io.kestra.core.models.executions.Execution;
import io.kestra.core.models.flows.State;
import io.kestra.core.models.triggers.AbstractTrigger;
import io.kestra.core.models.triggers.PollingTriggerInterface;
import io.kestra.core.models.triggers.TriggerContext;
import io.kestra.core.utils.IdUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.Optional;

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
public class Trigger extends AbstractTrigger implements PollingTriggerInterface {
    @Builder.Default
    private final Duration interval = Duration.ofSeconds(60);

    @Override
    public Optional<Execution> evaluate(ConditionContext conditionContext, TriggerContext context) throws Exception {
        return Optional.of(Execution.builder()
                .id(IdUtils.create())
                .namespace(context.getNamespace())
                .flowId(context.getFlowId())
                .flowRevision(context.getFlowRevision())
                .state(new State())
                .build()
        );
    }
}
