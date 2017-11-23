package example.metrixir;

import enkan.system.EnkanSystem;
import example.metrixir.configuration.SystemConfiguration;

public class Application {

    public static void main(String[] args) {
        final EnkanSystem system = new SystemConfiguration().create();

        Runtime.getRuntime().addShutdownHook(new Thread(system::stop));
        system.start();
    }
}
