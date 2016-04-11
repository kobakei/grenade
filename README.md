# grenade

[![Build Status](https://circleci.com/gh/kobakei/grenade.svg?style=shield)](https://circleci.com/gh/kobakei/grenade/tree/master)
[![JitPack](https://jitpack.io/v/kobakei/grenade.svg)](https://jitpack.io/#kobakei/grenade)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Grenade-green.svg?style=true)](https://android-arsenal.com/details/1/3406)

Grenade is annotation based intent builder for activities and services.
By using this library, you can build Intent with extras and retrieve extras by type safe way and less code.

This library is strongly inspired by [emilsjolander/IntentBuilder](https://github.com/emilsjolander/IntentBuilder) but some advanced features are added.

## Download

Project build.gradle

```groovy
buildscript {
    dependencies {
        ...
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
    }
}

allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

App build.gradle

```groovy
apply plugin: 'com.neenbedankt.android-apt'

dependencies {
    ...
    apt 'com.github.kobakei.grenade:processor:LATEST_VERSION'
    compile 'com.github.kobakei.grenade:library:LATEST_VERSION'
}
```

`LATEST_VERSION` is  [![JitPack](https://jitpack.io/v/kobakei/grenade.svg)](https://jitpack.io/#kobakei/grenade)

## Basic usage

Add `@Launcher` annotation to activity and `@Extra` annotation to fields.

```java
@Launcher
public class DetailActivity extends AppCompatActivity {

    // Required params
    @Extra
    String foo;
    @Extra
    int bar;

    // Optional params
    @Extra @Nullable
    String hoge;
    @Extra @Nullable
    boolean fuga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
```

Once you build, `DetailActivityIntentBuilder` will be generated. Building intent to launch `DetailActivity` is as below.

```java
// Build intent and start activity
startActivity(new DetailActivityIntentBuilder(foo, bar)
    .hoge(hoge)
    .fuga(fuga)
    .flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
    .build(context);
```

And handling intent is as below.

```java
@Launcher
public class DetailActivity extends AppCompatActivity {

    ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject fields
        DetailActivityIntentBuilder.inject(this, getIntent());
    }
}
```

## Multiple constructors

By specifying fields in `@Launcher` annotation, multiple constructors with different set of required params will be generated.

```java
@Launcher({
    "foo,bar1",
    "foo,bar2"
})
public class DetailActivity extends AppCompatActivity {
    @Extra
    String foo;
    @Extra
    int bar1;
    @Extra
    long bar2;

    ...
}
```

You can use them as below.

```java
startActivity(new DetailActivityIntentBuilder(foo, bar1)
    .build(context));
startActivity(new DetailActivityIntentBuilder(foo, bar2)
    .build(context));
```

## Work with Parceler

[Parceler](https://github.com/johncarl81/parceler) is a famous library to generate Parcelable reader/writer.
Grenade offers build-in support of Parceler.
When field type has `@Parcel` annotation, Grenade will wrap/unwrap an entity with Parceler.
To install Parceler to your project, please read Parceler's README.


```java
// Entity class with @Parcel annotation
@Parcel
public class User {
    ...
}
```

```java
@Launcher
public class DetailActivity extends AppCompatActivity {
    // Use parcelable entity as field
    @Extra
    User user;
    ...
}
```

```java
startActivity(new DetailActivityIntentBuilder(new User())
    .build(context));
```

## Proguard

If you use grenade with Parceler, [proguard setting of Parceler](https://github.com/johncarl81/parceler#configuring-proguard) is needed.

## License

```
Copyright 2016 Keisuke Kobayashi

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
