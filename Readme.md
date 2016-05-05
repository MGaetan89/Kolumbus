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
    compile 'com.github.MGaetan89.Kolumbus:kolumbus:v0.7'
}
```

If you only want to use Kolumbus in your debug build, you can use the following configuration instead:

```gradle
dependencies {
    debugCompile 'com.github.MGaetan89.Kolumbus:kolumbus:v0.7'
    releaseCompile 'com.github.MGaetan89.Kolumbus:kolumbus-no-op:v0.7'
}
```

## Usage

Check the [demo application](app/src/main/kotlin/io/kolumbus/demo/DemoActivity.kt) to see how to use Kolumbus.

The following example assumes that you have two classes named `Category` and `Product` in your project, which both inherits from `io.realm.RealmModel`.

```kotlin
// Kotlin
import io.kolumbus.Kolumbus
import io.kolumbus.KolumbusModule
import io.realm.Realm
import io.realm.RealmConfiguration

// Register the Kolumbus module when defining your Realm configuration
val realmConfiguration = RealmConfiguration.Builder(context)
    .modules(KolumbusModule())
    .build()
Realm.setDefaultConfiguration(realmConfiguration)

// Configure Kolumbus
Kolumbus.explore(Category::class.java)
    .explore(Product::class.java)
    .navigate(context)
```

```java
// Java
import io.kolumbus.Kolumbus;
import io.kolumbus.KolumbusModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;

// Register the Kolumbus module when defining your Realm configuration
RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
    .modules(new KolumbusModule())
    .build();
Realm.setDefaultConfiguration(realmConfiguration);

// Configure Kolumbus
Kolumbus.INSTANCE
    .explore(Category.class)
    .explore(Product.class)
    .navigate(context);
```

## Customization

### Override resources

One way to customize the  behavior/display of Kolumbus is to override the [public resources](https://github.com/MGaetan89/Kolumbus/blob/master/kolumbus/src/main/res/values/public.xml) in your application.

### Create a custom `Architect`

Kolumbus provides a basic way to customize how the content of a table is displayed.
To use this feature, you have to extends from `Architect` and override the desired methods.

```kotlin
// Kotlin
import android.widget.TextView
import io.kolumbus.Architect

class MyArchitect: Architect() {
    override fun displayEmpty(textView: TextView) {
        textView.text = "Field is empty"
    }
}
```

```java
// Java
import android.widget.TextView;
import io.kolumbus.Architect;

public class MyArchitect extends Architect {
    @Override
    public void displayEmpty(@NonNull TextView textView) {
        textView.setText("Field is empty")
    }
}
```

Then, you need to pass an instance of your `Architect` to `Kolumbus`, as follow:

```kotlin
// Kotlin
import io.kolumbus.Kolumbus

Kolumbus
    // All `explore()` calls you might need
    .withArchitect(MyArchitect())
    .navigate(context)
```

```java
// Java
import io.kolumbus.Kolumbus;

Kolumbus.INSTANCE
    // All `explore()` calls you might need
    .withArchitect(new MyArchitect())
    .navigate(context)
```

## Compatibility

The following table presents the compatibility matrix between Kolumbus versions and Realm versions.

| Kolumbus version | Realm version |
| ----- | ----- |
| 0.7 | 0.89.0 - 0.90.0 |
| 0.5 - 0.6 | 0.88.0 - 0.88.3 |
| 0.1 - 0.4 | ? - 0.87.5 |

## TODO

In no particular order:
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
