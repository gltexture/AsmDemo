package javassist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavassistTransformer2 {
    public static void run() throws CannotCompileException, NotFoundException, IOException {
        final Path classFile = Path.of("target/classes/exmpl.BCodeExmp.class");

        final ClassPool pool = ClassPool.getDefault(); // ПУЛ КЛАССОВ JAVASSIST
        final CtClass clazz = pool.get("exmpl.BCodeExmp"); // JAVASSIST ПРЕДСТАВЛЕНИЕ КЛАССА

        // НОВОЕ ПОЛЕ С ТИПОМ java.lang.String
        final CtField field = new CtField(pool.get("java.lang.String"), "newText", clazz);
        field.setModifiers(Modifier.PUBLIC);
        clazz.addField(field, "\"JAVASSIST!\"");

        final CtMethod method = clazz.getDeclaredMethod("doNothing");
        method.setBody("""
                {
                    System.out.println(this.newText);
                }
                """);

        Files.write(classFile, clazz.toBytecode());
    }
}
