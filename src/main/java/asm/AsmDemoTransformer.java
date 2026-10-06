package asm;

import org.objectweb.asm.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AsmDemoTransformer {
    public static void run() throws IOException {
        final Path classFile = Path.of("target/classes/exmpl.BCodeExmp.class");
        final byte[] bytecode = Files.readAllBytes(classFile);
        // ASM СИСТЕМА ЧТЕНИЯ И ИНТЕРПРЕТАЦИИ БАЙТКОДА
        final ClassReader reader = new ClassReader(bytecode);
        // ASM СИСТЕМА ОБРАТНОГО ПРЕОБРАЗОВАНИЯ БАЙТКОДА
        final ClassWriter writer = new ClassWriter(reader, 0);
        // ASM СЛУШАТЕЛЬ СОБЫТИЯ ЧТЕНИЯ МЕТОДОВ
        final ClassVisitor visitor = new ClassVisitor(Opcodes.ASM9, writer) {
            // НАХОДИМ НАШ МЕТОД, МЕНЯЕМ ИНСТРУКЦИЮ ПО ОПКОДУ IADD
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                final MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (!name.equals("add")) {
                    return methodVisitor;
                }
                return new MethodVisitor(Opcodes.ASM9, methodVisitor) {

                    // ОБРАБОТКА ОТДЕЛЬНЫХ ИНСТРУКЦИЙ
                    @Override
                    public void visitInsn(int opcode) {
                        if (opcode == Opcodes.IADD) {
                            super.visitInsn(Opcodes.ISUB);
                            return;
                        }
                        super.visitInsn(opcode);
                    }
                };
            }
        };
        reader.accept(visitor, 0);
        Files.write(classFile, writer.toByteArray());
    }
}
