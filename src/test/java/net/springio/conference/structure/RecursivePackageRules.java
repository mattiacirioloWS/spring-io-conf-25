package net.springio.conference.structure;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;

import java.lang.annotation.Annotation;
import java.util.Optional;

public final class RecursivePackageRules {

	private RecursivePackageRules() {
		// Prevent instantiation
	}

	public static DescribedPredicate<JavaClass> resideInPackageOrAncestorAnnotatedWith(
			Class<? extends Annotation> aClass, JavaClasses classes) {
		return new DescribedPredicate<>(
				"reside in a package or ancestor package annotated with @" + aClass.getSimpleName()) {
			@Override
			public boolean test(JavaClass input) {
				return findPackageOrAncestorAnnotatedWith(aClass, classes, input).isPresent();
			}
		};
	}

	public static Optional<JavaPackage> findPackageOrAncestorAnnotatedWith(Class<? extends Annotation> aClass,
																		   JavaClasses classes, JavaClass input) {
		String pkgName = input.getPackageName();
		while (pkgName.contains(".")) {
			JavaPackage pkg = classes.getPackage(pkgName);
			if (pkg != null && pkg.isAnnotatedWith(aClass)) {
				return Optional.of(pkg);
			}
			int idx = pkgName.lastIndexOf('.');
			pkgName = pkgName.substring(0, idx);
		}
		return Optional.empty();
	}

}
