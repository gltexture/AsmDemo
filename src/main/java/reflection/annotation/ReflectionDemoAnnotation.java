package reflection.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ReflectionDemoAnnotation {
    public static void run() throws NoSuchFieldException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        final AnnotationProcessor annotationProcessor = new AnnotationProcessor(AnnotatedCommandHooks.class);

        // DEMO
        {
            final Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                final String line = scanner.nextLine();
                final Runnable runnable = annotationProcessor.getCommandsMap().get(line);
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
    }

    private static class AnnotationProcessor {
        private final Map<String, Runnable> commandsMap; // СООТВЕТСТВИЕ {КОМАНДА -> ДЕЙСТВИЕ}

        public AnnotationProcessor(Class<?> clazz) {
            this.commandsMap = new HashMap<>();
            this.preprocess(clazz);
        }

        private void preprocess(Class<?> clazz) {
            for (Method method : clazz.getDeclaredMethods()) { // ПРОХОД ПО МАССИВУ ОБЪЯВЛЕННЫХ МЕТОДОВ КЛАССА
                if (method.isAnnotationPresent(CommandHook.class)) { // ЕСТЬ НАША АННОТАЦИЯ?
                    if (!Modifier.isPublic(method.getModifiers())) { // ПУБЛИЧНЫЙ МЕТОД?
                        throw new RuntimeException("Method is not public! " + method);
                    }
                    if (!Modifier.isStatic(method.getModifiers())) { // СТАТИЧНЫЙ МЕТОД?
                        throw new RuntimeException("Method is not static! " + method);
                    }
                    CommandHook commandHook = method.getAnnotation(CommandHook.class); // ПОЛУЧАЕМ ЕЕ ЭКЗЕМПЛЯР
                    final Runnable command = () -> { // ЗАПАКОВКА ВЫЗОВА МЕТОДА В Runnable
                        try {
                            method.invoke(null);
                        } catch (ReflectiveOperationException e) {
                            throw new RuntimeException(e);
                        }
                    };
                    this.getCommandsMap().put(commandHook.comm(), command); // РЕГИСТРАЦИЯ
                    System.out.println("Registered command: " + method.getName() + " | " +
                            commandHook.comm() + " | " + commandHook.description());
                }
            }
        }

        public Map<String, Runnable> getCommandsMap() {
            return this.commandsMap;
        }
    }
}
