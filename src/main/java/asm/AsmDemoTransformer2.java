package asm;

import org.objectweb.asm.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AsmDemoTransformer2 {
    public static void run() throws IOException {
        final Path classFile = Path.of("target/classes/exmpl.BCodeExmp.class");
        final byte[] bytecode = Files.readAllBytes(classFile);
        final ClassReader reader = new ClassReader(bytecode);
        final ClassWriter writer = new ClassWriter(reader, 0);
        final ClassVisitor visitor = new ClassVisitor(Opcodes.ASM9, writer) {
            // ДОБАВЛЯЕМ ПОЛЕ a
            @Override
            public void visitEnd() {
                super.visitField(Opcodes.ACC_PUBLIC, "a", "I", null, null).visitEnd();
                super.visitEnd();
            }

            // НАХОДИМ НАШ МЕТОД, ДОБАВЛЯЕМ ИНСТРУКЦИИ
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                final MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if ("<init>".equals(name)) {
                    return new MethodVisitor(Opcodes.ASM9, methodVisitor) {
                        @Override
                        public void visitInsn(final int opcode) {
                            if (opcode == Opcodes.RETURN) {
                                // this
                                super.visitVarInsn(Opcodes.ALOAD, 0);
                                // 5
                                super.visitIntInsn(Opcodes.BIPUSH, 5);
                                // this.a = 5
                                super.visitFieldInsn(Opcodes.PUTFIELD, "exmpl.BCodeExmp", "a", "I");
                            }
                            super.visitInsn(opcode);
                        }
                    };
                }

                if (!name.equals("doNothing")) {
                    return methodVisitor;
                }
                return new MethodVisitor(Opcodes.ASM9, methodVisitor) {
                    @Override
                    public void visitInsn(int opcode) {
                        if (opcode == Opcodes.RETURN) {
                            // System.out
                            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            // this
                            super.visitVarInsn(Opcodes.ALOAD, 0);
                            // this.a
                            super.visitFieldInsn(Opcodes.GETFIELD, "exmpl.BCodeExmp", "a", "I");
                            // System.out.println(this.a)
                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
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
