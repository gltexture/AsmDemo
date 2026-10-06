import bytebuddy.ByteBuddyTransformer2;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.apache.commons.lang3.StringUtils;
import java.lang.instrument.Instrumentation;

public class Main {
    public static void main(String[] args) {
        final Instrumentation instrumentation = ByteBuddyAgent.install();
        ByteBuddyTransformer2.run(instrumentation);
        System.out.println(StringUtils.isBlank(""));
        System.out.println(StringUtils.isBlank("   "));
        System.out.println(StringUtils.isBlank("Hello"));


      // try {
      //     ReflectionDemoAnnotation.run();
      // } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      //     throw new RuntimeException(e);
      // }

       // ByteBuddyTransformer.run();
       // {
       //     System.out.println("12 + 8 = " + BCodeExmp.add(12, 8));
       // }

       //try {
       //    AsmDemoTransformer2.run();
       //} catch (IOException e) {
       //    throw new RuntimeException(e);
       //}
    }
}
