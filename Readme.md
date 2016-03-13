# Kolumbus

[![Build Status](https://travis-ci.org/MGaetan89/Kolumbus.svg?branch=master)](https://travis-ci.org/MGaetan89/Kolumbus) [![Coverage Status](https://coveralls.io/repos/github/MGaetan89/Kolumbus/badge.svg?branch=master)](https://coveralls.io/github/MGaetan89/Kolumbus?branch=master) [![Latest version](https://jitpack.io/v/MGaetan89/Kolumbus.svg)](https://jitpack.io/#MGaetan89/Kolumbus) [![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/MGaetan89/Kolumbus/master/LICENSE) [![Minimum API](https://img.shields.io/badge/API-9%2B-green.svg)](https://android-arsenal.com/api?level=9)

Kolumbus allows you to explore your [Realm](https://realm.io/) database from within your Android application.

**Note:** This library is still under heavy development and a lot of improvements has to done. It was made available to collect feedbacks and feature requests.

## Screenshots

- [List of tables](https://raw.githubusercontent.com/MGaetan89/Kolumbus/master/screenshots/Tables_List.png)
- [Content of the Category table](https://raw.githubusercontent.com/MGaetan89/Kolumbus/master/screenshots/Table_Category_Content.png)
- [Info about the Category table](https://raw.githubusercontent.com/MGaetan89/Kolumbus/master/screenshots/Table_Category_Info.png)
- [Content of the Product table](https://raw.githubusercontent.com/MGaetan89/Kolumbus/master/screenshots/Table_Product_Content.png)
- [Info about the Product table](https://raw.githubusercontent.com/MGaetan89/Kolumbus/master/screenshots/Table_Product_Info.png)

## Installation

In your root `build.gradle` file, add the [Jitpack](https://jitpack.io/) url to the list of repositories, if you don't have it yet:

```gradle
allprojects {
    repositories {
        maven {
            url "https://jitpack.io"
        }
    }
}
```

In your module `build.gradle` file, add the Kolumbus dependency:

```gradle
dependencies {
    compile 'com.github.MGaetan89.Kolumbus:kolumbus:v0.5'
}
```

If you only want to use Kolumbus in your debug build, you can use the following configuration instead:

```gradle
dependencies {
    debugCompile 'com.github.MGaetan89.Kolumbus:kolumbus:v0.5'
    releaseCompile 'com.github.MGaetan89.Kolumbus:kolumbus-no-op:v0.5'
}
```

## Usage

Check the [demo application](app/src/main/kotlin/io/kolumbus/demo/DemoActivity.kt) to see how to use Kolumbus.

The following example assumes that you have two classes named `Category` and `Product` in your project, which both extends from `io.realm.RealmObject`.

```kotlin
// Usage in a Kotlin project
// Register the Kolumbus module when defining your Realm configuration
val realmConfiguration = RealmConfiguration.Builder(context)
    .setModules(KolumbusModule())
    .build()
Realm.setDefaultConfiguration(realmConfiguration)

// Configure Kolumbus
Kolumbus.explore(Category::class.java)
    .explore(Product::class.java)
    .navigate(context)
```

```java
// Usage in a Java project
// Register the Kolumbus module when defining your Realm configuration
RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
    .setModules(new KolumbusModule())
    .build();
Realm.setDefaultConfiguration(realmConfiguration);

// Configure Kolumbus
Kolumbus.INSTANCE
    .explore(Category.class)
    .explore(Product.class)
    .navigate(context);
```

## TODO

In no particular order:
- Find a better and more efficient way to display a table content than `TableLayout`
- Allow customizing the way the data are displayed
- Sort the content of the displayed table
- Filter the fields that are displayed
- Filter the data that are displayed

## License

```
Copyright 2016 MGaetan89

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
