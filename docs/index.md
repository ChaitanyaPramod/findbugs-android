---
# You don't need to edit this file, it's empty on purpose.
# Edit theme's home layout instead if you wanna make some changes
# See: https://jekyllrb.com/docs/themes/#overriding-theme-defaults
layout: home
toc: true
---
findbugs-android Gradle plugin creates FindBugs tasks for each variant of Android application or library module

{% include toc %}

## Introduction
Static analysis tools are generous code reviewers you don't have to pay wages to, buy expensive hardware for and find issues and bugs in your code. Being built on Java, Android projects have access to many of the static analysis tools from Java ecosystem. 

## Why
In order to create FindBugs report for Android projects, you need to create `FindBugs` tasks and configure them by providing paths to source code, compiled classes and classpath. It can be troublesome since Android projects can have different flavors and build types, thus requiring additional paths to be set. This plugin provides those tasks already configured for you.

In addition, the plugin automatically excludes generated classes from Android databinding library, android resource, Butterknife and Dagger 2. If you want more added to this list or want it configurable, please [open an issue](https://github.com/ChaitanyaPramod/findbugs-android/issues/new).

## Usage
Apply the plugin after the Android gradle plugin. The FindBugs tasks are already hooked to the `check` task.
```gradle
buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    // ...
    classpath 'com.chaitanyapramod.gradle:findbugs-android:1.0'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.chaitanyapramod.findbugs-android'

android {
  // ...
  buildTypes {
      debug {}
      release {}
  }
}
```
This would create the following Gradle tasks:
* `findbugsDebug`
* `findbugsRelease`

Note that these findbugs tasks are also hooked to `check` task.

## Configuration
Use the standard findbugs extension to configure the FindBugs tasks
{% highlight groovy %}
buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    // ...
    classpath 'com.chaitanyapramod.gradle:findbugs-android:1.0'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.chaitanyapramod.findbugs-android'

findbugs {
  toolVersion '3.0.1'
  ignoreFailures true
  effort 'max'
  reportLevel 'high'
  excludeBugsFilter = file("$rootProject.projectDir/config/findbugs/excludeBugsFilter.xml")
}

android {
  // ...
  buildTypes {
      debug {}
      release {}
  }
}
{% endhighlight %}

### Report Format
By default, XML reports are generated. You can add the following snippet to get HTML reports

{% highlight groovy %}
tasks.withType(FindBugs) {
    reports {
        xml.enabled = false
        html.enabled = true
    }
}
{% endhighlight %}

## Contributing
Contributions are welcome. Contributers will find looking through [Gradle sources](https://github.com/gradle/gradle/tree/master/subprojects/code-quality/src/main/groovy/org/gradle/api/plugins/quality) of the classes [`FindBugs`](https://github.com/gradle/gradle/blob/master/subprojects/code-quality/src/main/groovy/org/gradle/api/plugins/quality/FindBugs.java), [`FindBugsExtension`](https://github.com/gradle/gradle/blob/master/subprojects/code-quality/src/main/groovy/org/gradle/api/plugins/quality/FindBugsExtension.java) and [`FindBugsPlugin`](https://github.com/gradle/gradle/blob/master/subprojects/code-quality/src/main/groovy/org/gradle/api/plugins/quality/FindBugsPlugin.java) useful.
