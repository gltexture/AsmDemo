package reflection.simple;

public class ReflectionSecretClass {
    private final String secretCode;

    public ReflectionSecretClass() {
        this.secretCode = "HelloWorld(print)";
    }

    private void printSecretCode() {
        System.out.println(this.secretCode);
    }
}
