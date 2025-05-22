package net.springio.conference.structure;

import net.springio.conference.SpringIoConf;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class Documentation {
    @Test
    void createModuleDocumentation() {
        ApplicationModules modules = ApplicationModules.of(SpringIoConf.class);
        new Documenter(modules)
                .writeDocumentation()
                .writeIndividualModulesAsPlantUml();
    }
}
