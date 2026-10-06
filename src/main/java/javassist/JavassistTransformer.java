package javassist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavassistTransformer {
    public static void run() throws CannotCompileException, NotFoundException, IOException {
        final Path classFile = Path.of("target/classes/exmpl.BCodeExmp.class");

        final ClassPool pool = ClassPool.getDefault(); // ПУЛ КЛАССОВ JAVASSIST
        final CtClass clazz = pool.get("exmpl.BCodeExmp"); // JAVASSIST ПРЕДСТАВЛЕНИЕ КЛАССА
        final CtMethod method = clazz.getDeclaredMethod("add"); // ПРЕДСТАВЛЕНИЕ МЕТОДА КЛАССА

        // МЕНЯЕМ ТЕЛО МЕТОДА
        method.setBody("""
                {
                    return $1 - $2;
                }
                """);

        Files.write(classFile, clazz.toBytecode());
    }
}
