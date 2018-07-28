![Logo](LogoDesign/logotype1.png)

# findbugs-android
A Gradle plugin that creates FindBugs tasks for each variant of android application or library project

## Why
In order to create FindBugs report for Android projects, you need to create `FindBugs` tasks and configure them by providing paths to source code, compiled classes and classpath. It can be troublesome since Android projects can have different flavors and build types, thus requiring additional paths to be set. This plugin provides those tasks already configured for you.

In addition, the plugin automatically excludes Generated classes from Android databinding library, android resource, Butterknife and Dagger 2. If you want more added to this list or want it configurable, [open an issue](https://github.com/ChaitanyaPramod/findbugs-android/issues/new).

## Usage
Apply the plugin after the Android gradle plugin. The FindBugs tasks are already hooked to the `check` task.
```groovy
buildscript {
  repositories {
    // ...
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
  productFlavors {
    free {}
    paid {}
  }
}
```
This would create the following Gradle tasks:
* `findbugsFreeDebug`
* `findbugsFreeRelease`
* `findbugsPaidDebug`
* `findbugsPaidRelease`

Note that running `check` task runs all these findbugs tasks as well.

## Configuration
Use the standard findbugs extension to configure the FindBugs tasks
```groovy
buildscript {
  repositories {
    // ...
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
  productFlavors {
    free {}
    paid {}
  }
}
```

## Contributing
Contributers will find looking through [sources](https://github.com/gradle/gradle/tree/master/subprojects/code-quality/src/main/groovy/org/gradle/api/plugins/quality) of `FindBugs`, `FindBugsExtension` and `FindBugsPlugin` useful.

This plugin has been heavily inspired by [jacoco-android-gradle-plugin](https://github.com/arturdm/jacoco-android-gradle-plugin)

## License
```
MIT License

Copyright (c) 2016 Chaitanya Pramod

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
