package com.fsales.app.rumo.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchUnitRunner
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.runner.RunWith

@RunWith(ArchUnitRunner::class)
@AnalyzeClasses(
    packages = ["com.fsales.app.rumo"],
    importOptions = [ImportOption.DoNotIncludeTests::class]
)
class ArchitectureRulesTest {

    companion object {
        @JvmField
        @ArchTest
        val viewmodelsMustNotDependOnComposeOrNavigationUiLibs: ArchRule = noClasses()
            .that().haveSimpleNameEndingWith("ViewModel")
            .should().dependOnClassesThat().resideInAnyPackage(
                "androidx.compose..",
                "androidx.navigation3.ui..",
                "androidx.hilt.navigation.compose.."
            )
            .allowEmptyShould(true)

        @JvmField
        @ArchTest
        val uiPackageMustNotAccessDataLayerDirectly: ArchRule = noClasses()
            .that().resideInAPackage("com.fsales.app.rumo..ui..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "com.fsales.app.rumo..data..",
                "com.fsales.app.rumo.core..data..",
                "androidx.room.."
            )
            .allowEmptyShould(true)

        @JvmField
        @ArchTest
        val domainPackageMustStayFrameworkAgnostic: ArchRule = noClasses()
            .that().resideInAPackage("com.fsales.app.rumo..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "com.fsales.app.rumo..ui..",
                "com.fsales.app.rumo..data..",
                "androidx.compose..",
                "androidx.room..",
                "androidx.navigation3..",
                "android.app..",
                "android.content.."
            )
            .allowEmptyShould(true)

        @JvmField
        @ArchTest
        val dataPackageMustNotDependOnUiOrNavigation: ArchRule = noClasses()
            .that().resideInAPackage("com.fsales.app.rumo..data..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "com.fsales.app.rumo..ui..",
                "com.fsales.app.rumo..navigation..",
                "androidx.compose.."
            )
            .allowEmptyShould(true)
    }
}



