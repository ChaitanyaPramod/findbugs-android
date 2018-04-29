package com.chaitanyapramod.gradle.android.findbugs

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.FindBugsPlugin

public class FindBugsAndroidPlugin implements Plugin<Project> {

    private static final Collection<String> androidDataBindingExcludes =
            ['android/databinding/**/*.class',
             '**/android/databinding/*Binding.class',
             '**/BR.*'].asImmutable()

    private static final Collection<String> androidExcludes =
            ['**/R.class',
             '**/R$*.class',
             '**/BuildConfig.*',
             '**/Manifest*.*'].asImmutable()

    private static final Collection<String> butterKnifeExcludes =
            ['**/*$ViewInjector*.*',
             '**/*$ViewBinder*.*'].asImmutable()

    private static final Collection<String> dagger2Excludes =
            ['**/*_MembersInjector.class',
             '**/Dagger*Component.class',
             '**/Dagger*Component$Builder.class',
             '**/*Module_*Factory.class'].asImmutable()

    private static final Collection<String> defaultExcludes =
            (androidDataBindingExcludes + androidExcludes + butterKnifeExcludes + dagger2Excludes)
                    .asImmutable()

    @Override
    public void apply(Project project) {
        project.plugins.apply(FindBugsPlugin)

        Plugin plugin = findAndroidPluginOrThrow(project.plugins)
        def variants = getVariants(project, plugin)

        variants.all { variant ->
            FindBugs task = createFindBugsTask(project, variant)

            project.tasks.getByName('check').dependsOn task
        }
    }

    private static Plugin findAndroidPluginOrThrow(PluginContainer plugins) {
        Plugin plugin = plugins.findPlugin('android') ?: plugins.findPlugin('android-library')
        if (!plugin) {
            throw new GradleException(
                    'You must apply the Android plugin or the Android library plugin before using the findbugs-android plugin')
        }
        return plugin
    }

    private static def getVariants(Project project, Plugin plugin) {
        boolean isLibraryPlugin = plugin.class.name.endsWith('.LibraryPlugin')
        project.android[isLibraryPlugin ? 'libraryVariants' : 'applicationVariants']
    }

    private static FindBugs createFindBugsTask(Project project, def variant) {
        FindBugs task = project.tasks.create("findbugs${variant.name.capitalize()}", FindBugs)

        task.description = "Run FindBugs analysis for $variant.name classes"
        task.group = 'Verification'
        task.source = sourceDirs(variant)
        task.classes = project.fileTree(dir: variant.javaCompile.destinationDir, excludes: defaultExcludes)
        task.classpath = variant.javaCompile.classpath

        task.dependsOn variant.javaCompile

        return task
    }

    private static def sourceDirs(def variant) {
        variant.sourceSets.java.srcDirs.collect { it.path }.flatten()
    }
}
