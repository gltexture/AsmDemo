package reflection.simple;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionDemo {
    public static void run() throws NoSuchFieldException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        final ReflectionSecretClass instance = new ReflectionSecretClass();
        final Class<?> clazz = instance.getClass();

        // ПОЛУЧЕНИЕ ПРИВАТНОГО ПОЛЯ
        final Field secretCodeField = clazz.getDeclaredField("secretCode");
        secretCodeField.setAccessible(true); // СНИМАЕТ ОГРАНИЧЕНИЕ ДОСТУПА
        System.out.println("Secret code: " + secretCodeField.get(instance));

        // ПОЛУЧЕНИЕ + ВЫЗОВ ПРИВАТНОГО МЕТОДА
        final Method printMethod = clazz.getDeclaredMethod("printSecretCode");
        printMethod.setAccessible(true); // СНИМАЕТ ОГРАНИЧЕНИЕ ДОСТУПА
        printMethod.invoke(instance);

        // ЗАМЕНА ЗНАЧЕНИЯ ПОЛЯ + ПРОВЕРКА
        secretCodeField.set(instance, "ChangedSecretCode");
        printMethod.invoke(instance);
    }
}
