# Grenade

[![Build Status](https://circleci.com/gh/kobakei/grenade.svg?style=shield)](https://circleci.com/gh/kobakei/grenade/tree/master)
[![JitPack](https://jitpack.io/v/kobakei/grenade.svg)](https://jitpack.io/#kobakei/grenade)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Grenade-green.svg?style=true)](https://android-arsenal.com/details/1/3406)

Grenade is annotation based intent builder for activities and services.
By using this library, you can build Intent with extras and retrieve extras by type safe way and less code.

This library is strongly inspired by [emilsjolander/IntentBuilder](https://github.com/emilsjolander/IntentBuilder) but some advanced features are added.

## Download

Project build.gradle

```groovy
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
    annotationProcessor 'com.github.kobakei.grenade:processor:LATEST_VERSION'
    compile 'com.github.kobakei.grenade:library:LATEST_VERSION'
}
```

`LATEST_VERSION` is  [![JitPack](https://jitpack.io/v/kobakei/grenade.svg)](https://jitpack.io/#kobakei/grenade)

NOTE: if you use Android Gradle Plugin before 2.2.0, you must use android-apt plugin instead of [annotationProcessor](https://bitbucket.org/hvisser/android-apt) configuration.

## Basic usage

Add `@Navigator` annotation to activity and `@Extra` annotation to fields.

```java
@Navigator
public class DetailActivity extends AppCompatActivity {

    // Required params
    @Extra
    String foo;
    @Extra
    int bar;

    // Optional params
    @Extra @Optional
    String hoge;
    @Extra @Optional
    boolean fuga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
```

Once you build, `DetailActivityNavigator` will be generated. Building intent to launch `DetailActivity` is as below.

```java
// Build intent and start activity
startActivity(new DetailActivityNavigator(foo, bar)
    .hoge(hoge)
    .fuga(fuga)
    .flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
    .build(context);
```

And handling intent is as below.

```java
@Navigator
public class DetailActivity extends AppCompatActivity {

    ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject fields
        DetailActivityNavigator.inject(this, getIntent());
    }
}
```

Of course, you can use Grenade to start Service and BroadcastReceiver as same way.

```java
@Navigator
public class MyIntentService extends IntentService{

    @Extra
    String foo;
    @Extra
    String baz;

    @Override
    protected void onHandleIntent(Intent intent) {
      MyIntentServiceNavigator.inject(this, intent);
    }
}

// Code to start service
startService(new MyIntentServiceNavigator("foo", "baz").build(this));
```

## Multiple constructors

By specifying fields in `@Navigator` annotation, multiple constructors with different set of required params will be generated.

```java
@Navigator({
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
startActivity(new DetailActivityNavigator(foo, bar1)
    .build(context));
startActivity(new DetailActivityNavigator(foo, bar2)
    .build(context));
```

## Handling onActivityResult

Setting Intent object to `onActivityResult` has the same problem as above.
Grenade offers APIs to build Intent to pass `setResult` and to handle `onActivityResult` as type safe way.

As an example, let's think about handling DetailActivity's result in MasterActivity.

At first, in MasterActivity, add method to handle result of DetailActivity with `@OnActivityResult` annotation. Moreover, call `MasterActivityNavigator.onActivityResult` in `onActivityResult`.

```java
@OnActivityResult(requestCode = REQ_CODE_DETAIL1, resultCodes = {Activity.RESULT_OK})
void onDetailOk(String foo, int bar) {
    Toast.makeText(this, "Detail OK", Toast.LENGTH_SHORT).show();
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    MainActivityNavigator.onActivityResult(this, requestCode, resultCode, data);
}
```

In DetailActivity, setting result is as below:

```java
setResult(RESULT_OK, MainActivityNavigator.resultForOnDetailOk("Hello", 123));
finish();
```

Once you build, putting extras and getting extras will be done in MainActivityNavigator.

## Built-in support of Parceler

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
@Navigator
public class DetailActivity extends AppCompatActivity {
    // Use parcelable entity as field
    @Extra
    User user;
    ...
}
```

```java
startActivity(new DetailActivityNavigator(new User())
    .build(context));
```

## ProGuard

If you use Grenade with Parceler, [ProGuard setting of Parceler](https://github.com/johncarl81/parceler#configuring-proguard) is needed.

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
