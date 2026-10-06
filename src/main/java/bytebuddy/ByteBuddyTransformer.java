package bytebuddy;

import exmpl.BCodeExmp;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.io.IOException;

public class ByteBuddyTransformer {
    public static void run() {
        try (final DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .redefine(BCodeExmp.class) // МОДИФИЦИРУЕМ КЛАСС
                .defineField("a", int.class, Visibility.PUBLIC) // НОВОЕ ПОЛЕ
                .defineField("b", int.class, Visibility.PUBLIC) // НОВОЕ ПОЛЕ
                .visit(Advice.to(DoNothingAdvice.class).on(ElementMatchers.named("doNothing"))) // ДОБАВЛЕНИЕ КОДА В МЕТОД
                .visit(Advice.to(ConstructorAdvice.class).on(ElementMatchers.isConstructor())) // ДОБАВЛЕНИЕ КОДА В КОНСТРУКТОР
                .make()) {
            dynamicType.saveIn(new File("target/classes/exmpl"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ConstructorAdvice {
        @Advice.OnMethodExit
        public static void onExit(@Advice.FieldValue(value = "a", readOnly = false) int a, @Advice.FieldValue(value = "b", readOnly = false) int b) {
            a = 5;
            b = 15;
        }
    }

    public static class DoNothingAdvice {
        @Advice.OnMethodEnter
        public static void onEnter(@Advice.FieldValue(value = "a", readOnly = false) int a, @Advice.FieldValue(value = "b", readOnly = false) int b) {
            System.out.println(a + b);
        }
    }
}