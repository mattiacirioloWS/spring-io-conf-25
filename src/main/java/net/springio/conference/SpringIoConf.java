package net.springio.conference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.core.ApplicationModules;

import java.util.logging.Logger;

@SpringBootApplication
public class SpringIoConf {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(SpringIoConf.class.getName());
        ApplicationModules modules = ApplicationModules.of(SpringIoConf.class);
        modules.verify();

        modules.forEach(module -> logger.info(module.toString()));

        SpringApplication.run(SpringIoConf.class, args);
    }
}
