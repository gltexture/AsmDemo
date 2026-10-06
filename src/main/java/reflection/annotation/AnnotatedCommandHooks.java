package reflection.annotation;

public class AnnotatedCommandHooks {
    @CommandHook(comm = "/help", description = "Help command.")
    public static void commandHelp() {
        System.out.println("!Help command!");
    }

    @CommandHook(comm = "/start", description = "Start command.")
    public static void commandStart() {
        System.out.println("!Start command!");
    }
}
