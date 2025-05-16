package net.springio.conference.structure;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import net.springio.conference.shared.application.Policy;
import net.springio.conference.shared.application.UseCase;
import org.jmolecules.archunit.JMoleculesDddRules;
import org.jmolecules.ddd.annotation.*;
import org.jmolecules.event.annotation.DomainEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.conditions.ArchConditions.not;
import static com.tngtech.archunit.lang.conditions.ArchConditions.or;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static net.springio.conference.structure.BoundedContextRules.notDependOnOtherContextUnlessAllowed;
import static net.springio.conference.structure.BoundedContextRules.resideInAnyBoundedContext;
import static net.springio.conference.structure.DddArchUnitRules.*;

public class DDDStructureTests {

    private static final String PROJECT_PACKAGE = "net.springio.conference";
    private final DescribedPredicate<JavaClass> notImplementsMapperInterface =
            new DescribedPredicate<>("not implements interface annotated with @Mapper") {
                @Override
                public boolean test(JavaClass javaClass) {
                    return javaClass.getInterfaces().stream()
                            .flatMap(type -> type.getAllInvolvedRawTypes().stream())
                            .noneMatch(i -> i.isAnnotatedWith(Mapper.class.getName()));
                }
            };

    static Stream<String> boundedContexts() {
        JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(PROJECT_PACKAGE);

        Set<String> allPackageNames = classes.stream().map(JavaClass::getPackageName).collect(Collectors.toSet());

        return allPackageNames.stream()
                .map(classes::getPackage)
                .filter(Objects::nonNull)
                .filter(pkg -> pkg.isAnnotatedWith(BoundedContext.class))
                .map(JavaPackage::getName)
                .distinct();
    }

	@Test
	void checkAllClassesFollowDDDStructure() {
		JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
				.importPackages(PROJECT_PACKAGE);

		JMoleculesDddRules.all().check(classes);
	}

	private static GivenClassesConjunction applicationAnnotatedClasses() {
		return classes().that()
				.areAnnotatedWith(Service.class)
				.or()
				.areAnnotatedWith(UseCase.class)
				.or()
				.areAnnotatedWith(Policy.class);
	}

    private static GivenClassesConjunction domainAnnotatedClasses() {
        return classes().that()
                .areAnnotatedWith(AggregateRoot.class)
                .or()
                .areAnnotatedWith(Entity.class)
                .or()
                .areAnnotatedWith(Factory.class)
                .or()
                .areAnnotatedWith(ValueObject.class)
                .or()
                .areAnnotatedWith(Repository.class)
                .or()
                .areAnnotatedWith(DomainEvent.class);
    }

    @ParameterizedTest
    @MethodSource("boundedContexts")
    void dddElementsShouldBeInCorrectLayers(String basePackage) {
        JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        // Aggregates in Domain Layer
        domainAnnotatedClasses()
                .should(beInDomainLayer(classes))
                .andShould(not(beInApplicationLayer(classes)))
                .andShould(not(beInInfrastructureLayer(classes)))
                .andShould(not(beInInterfaceLayer(classes)))
                .check(classes);

        // Services in Application Layer
        applicationAnnotatedClasses()
                .should(beInApplicationLayer(classes))
                .andShould(not(beInDomainLayer(classes)))
                .andShould(not(beInInfrastructureLayer(classes)))
                .andShould(not(beInInterfaceLayer(classes)))
                .check(classes);

    }

    @ParameterizedTest
    @MethodSource("boundedContexts")
    void domainObjectsShouldNotBeJpaAnnotated(String basePackage) {
        JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        domainAnnotatedClasses()
                .should(not(beJpaAnnotatedNorExtendJpaClasses()))
                .because("JMolecules @AggregateRoot or @Entity should not be JPA annotated entities")
                .check(classes);
    }

    @ParameterizedTest
    @MethodSource("boundedContexts")
    void avoidSpringServicesAndComponentsAndBeansInDomainAndApplicationLayer(String basePackage) {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        // Spring stereotypes in the correct layers
        classes().that()
                .areAnnotatedWith(Component.class)
                .or()
                .areAnnotatedWith(org.springframework.stereotype.Service.class)
                .and(notImplementsMapperInterface)
                .should(or(beInInterfaceLayer(classes), (beInInfrastructureLayer(classes))))
                .andShould(not(beInDomainLayer(classes)))
                .andShould(not(beInApplicationLayer(classes)))
                .allowEmptyShould(true)
                .check(classes);
    }

    @ParameterizedTest
    @MethodSource("boundedContexts")
    void checkThatConfigurationsOrBeanAnnotatedMethodsAreInConfigPackage(String basePackage) {
        JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        classes().that()
                .areAnnotatedWith(Configuration.class)
                .should()
                .resideInAPackage("..config..");

    }

    @MethodSource("boundedContexts")
    void checkThatBeanAnnotatedMethodsAreInConfigPackage(String basePackage) {
        JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        methods().that()
                .areAnnotatedWith(org.springframework.context.annotation.Bean.class)
                .should()
                .beDeclaredInClassesThat()
                .resideInAnyPackage("..config..")
                .check(classes);
    }

    @ParameterizedTest
    @MethodSource("boundedContexts")
    void enforceSpringDataInInfrastructureLayerAndPersistencePackage(String basePackage) {
        JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        // Spring Data classes in the correct layers
        classes().that()
                .areAnnotatedWith(jakarta.persistence.Entity.class)
                .or()
                .areAnnotatedWith(Embeddable.class)
                .or()
                .areAnnotatedWith(MappedSuperclass.class)
                .or()
                .areAssignableTo(org.springframework.data.repository.Repository.class)
                .or()
                .areAssignableTo(org.springframework.data.repository.CrudRepository.class)
                .or()
                .areAssignableTo(org.springframework.data.repository.PagingAndSortingRepository.class)
                .should(beInInfrastructureLayer(classes))
                .andShould()
                .resideInAPackage("..persistence..")
                .andShould(not(beInDomainLayer(classes)))
                .andShould(not(beInApplicationLayer(classes)))
                .andShould(not(beInInterfaceLayer(classes)))
                .check(classes);
    }

    @ParameterizedTest
    @MethodSource("boundedContexts")
    void enforceSpringControllerInInterfaceLayerAndApiPackage(String basePackage) {
        JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        // Spring Controller classes in the correct layers
        classes().that()
                .areAnnotatedWith(Controller.class)
                .or()
                .areAnnotatedWith(RestController.class)
                .should(beInInterfaceLayer(classes))
                .andShould()
                .resideInAPackage("..api..")
                .andShould(not(beInDomainLayer(classes)))
                .andShould(not(beInApplicationLayer(classes)))
                .andShould(not(beInInfrastructureLayer(classes)))
                .allowEmptyShould(true)
                .check(classes);
    }

    @ParameterizedTest
    @MethodSource("boundedContexts")
    void enforceLayeringDependencies(String basePackage) {
        JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        // Domain should not depend on other layers
        noClasses().that(areInDomainLayerAndNotDomainEvents(classes))
                .should()
                .dependOnClassesThat(areInApplicationLayer(classes))
                .orShould()
                .dependOnClassesThat(areInInfrastructureLayer(classes))
                .orShould()
                .dependOnClassesThat(areInInterfaceLayer(classes))
                .check(classes);

        // Application should not depend on Infrastructure
        noClasses().that(areInApplicationLayer(classes))
                .should()
                .dependOnClassesThat(areInInfrastructureLayer(classes))
                .orShould()
                .dependOnClassesThat(areInInterfaceLayer(classes))
                .check(classes);

        // Interface should not depend on Domain
        noClasses().that(areInInterfaceLayer(classes))
                .should()
                .dependOnClassesThat(areInDomainLayerAndNotDomainEvents(classes))
                .orShould()
                .dependOnClassesThat(areInInfrastructureLayer(classes))
                .allowEmptyShould(true)
                .check(classes);
    }

    @Test
    void enforceBoundedContextIsolation() {
        JavaClasses classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(PROJECT_PACKAGE);
        classes().that(resideInAnyBoundedContext(classes)).and().resideOutsideOfPackage("..config..")
                .should(notDependOnOtherContextUnlessAllowed(classes))
                .because(
                        "Bounded contexts must be isolated except via their interfaces, domain events or application services")
                .check(classes);
    }

    @ParameterizedTest
    @MethodSource("boundedContexts")
    void controllersShouldNotReturnEntitiesOrAggregates(String basePackage) {
        var classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        methods()
                .that().areDeclaredInClassesThat()
                .areAnnotatedWith(RestController.class)
                .or().areAnnotatedWith(Controller.class)
                .should(notReturnEntityOrAggregateTypes())
                .allowEmptyShould(true)
                .check(classes);
    }

    @ParameterizedTest
    @MethodSource("boundedContexts")
    void servicesOrComponentsShouldNotReturnEntitiesOrAggregates(String basePackage) {
        var classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(basePackage);

        methods()
                .that().areDeclaredInClassesThat()
                .areAnnotatedWith(UseCase.class)
                .or().areAnnotatedWith(Service.class)
                .or().areAnnotatedWith(org.springframework.stereotype.Service.class)
                .or().areAnnotatedWith(Component.class)
                .should(notReturnEntityOrAggregateTypes())
                .check(classes);
    }

    private ArchCondition<JavaMethod> notReturnEntityOrAggregateTypes() {
        return new ArchCondition<>("not return @Entity or @AggregateRoot annotated types") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                JavaType returnType = method.getReturnType();

                if (returnType.getAllInvolvedRawTypes().stream().anyMatch(this::isEntityOrAggregate)) {
                    // Check raw return type (e.g., directly returning an Entity)
                    events.add(SimpleConditionEvent.violated(method,
                            method.getFullName() + " returns a type annotated with @Entity or @AggregateRoot: " + returnType.getName()));
                }

            }

            private boolean isEntityOrAggregate(JavaClass javaClass) {
                return javaClass.isAnnotatedWith(Entity.class)
                        || javaClass.isAnnotatedWith(AggregateRoot.class);
            }
        };
    }

}
