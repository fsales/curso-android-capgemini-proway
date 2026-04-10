package com.fsales.app.rumo.core.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchUnitRunner
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.runner.RunWith

@RunWith(ArchUnitRunner::class)
@AnalyzeClasses(
    packages = ["com.fsales.app.rumo.core"],
    importOptions = [ImportOption.DoNotIncludeTests::class]
)
class CoreArchitectureRulesTest {

    companion object {
        @JvmField
        @ArchTest
        val coreMustNotDependOnAppUiAndNavigationPackages: ArchRule = noClasses()
            .that().resideInAnyPackage("com.fsales.app.rumo.core..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "com.fsales.app.rumo..ui..",
                "com.fsales.app.rumo..navigation.."
            )
            .allowEmptyShould(true)

        @JvmField
        @ArchTest
        val coreMustNotDependOnComposeNavigationOrActivityUiLibs: ArchRule = noClasses()
            .that().resideInAnyPackage("com.fsales.app.rumo.core..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "androidx.compose..",
                "androidx.navigation3..",
                "androidx.hilt.navigation.compose..",
                "androidx.activity.compose..",
                "com.google.android.material.."
            )
            .allowEmptyShould(true)

        @JvmField
        @ArchTest
        val coreDomainPackageMustNotDependOnRoomOrAndroidFramework: ArchRule = noClasses()
            .that().resideInAPackage("com.fsales.app.rumo.core..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "androidx.room..",
                "android.app..",
                "android.content.."
            )
            .allowEmptyShould(true)
    }
}


