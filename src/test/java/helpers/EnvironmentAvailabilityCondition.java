package helpers;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class EnvironmentAvailabilityCondition implements ExecutionCondition {

    private static final ConditionEvaluationResult ENABLED =
            ConditionEvaluationResult.enabled("Окружение MAX доступно");

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        if (context.getTestMethod().isEmpty() || !PageAvailability.isUnavailable()) {
            return ENABLED;
        }

        return ConditionEvaluationResult.disabled(
                "Окружение MAX недоступно после срабатывания ограничения запросов"
        );
    }
}
