package bytebuddy;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class ByteBuddyTransformer2 {
    public static void run(Instrumentation instrumentation) {
        new AgentBuilder.Default() // РАНТАЙМ ТРАНСФОРМАЦИЯ
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named("org.apache.commons.lang3.StringUtils"))
                .transform((builder, type, classLoader,
                            module, domain) ->
                        builder
                                .method(ElementMatchers.named("isBlank"))
                                .intercept(MethodDelegation.to(ByteBuddyTransformer2.class))
                ).installOn(instrumentation);
    }

    public static boolean isBlank(CharSequence cs) {
        System.out.println("HACKED!");
        return false;
    }
}