# grenade

Granade is annotation based intent builder for activities and services.

## Download

TODO


## Basic usage

Add `@Launcher` annotation to activity and `@Extra` annotation to fields.

```java
@Launcher
public class DetailActivity extends AppCompatActivity {

    @Extra
    String foo;
    @Extra
    int bar;
    
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