package net.springio.conference;

import com.tngtech.archunit.core.domain.JavaClass;
import org.springframework.boot.SpringApplication;
import org.springframework.modulith.Modulith;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.core.NamedInterface;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Modulith(sharedModules = "shared")
public class SpringIoConf {
    private static final Logger logger = Logger.getLogger(SpringIoConf.class.getName());

    public static void main(String[] args) {
        ApplicationModules modules = ApplicationModules.of(SpringIoConf.class);
        modules.verify();

        modules.forEach(logModuleInfo());

        SpringApplication.run(SpringIoConf.class, args);
    }

    private static Consumer<ApplicationModule> logModuleInfo() {
        return module -> {
            List<NamedInterface> spis = module.getNamedInterfaces().stream()
                    .filter(NamedInterface::isNamed)
                    .toList();

            if (spis.isEmpty()) {
                logger.info("Registered module: " + module.getDisplayName());
            } else {
                StringBuilder msg = new StringBuilder()
                        .append("Registered module ")
                        .append(module.getDisplayName())
                        .append(" with SPI:");

                spis.forEach(ni -> msg.append(System.lineSeparator())
                        .append(" - ")
                        .append(ni.getName())
                        .append(", with classes: ")
                        .append(ni.asJavaClasses()
                                .map(JavaClass::getName)
                                .collect(Collectors.joining(", "))));

                logger.info(msg.toString());
            }
        };
    }
}
