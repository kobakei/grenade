package io.github.kobakei.ibericosample;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.kobakei.iberico.annotation.Extra;
import io.github.kobakei.iberico.annotation.Launcher;

/**
 * Sample of multiple constructor
 */
@Launcher({
        "foo, bar1",
        "foo, bar2"
})
public class Detail2Activity extends AppCompatActivity {

    @Extra
    String foo;
    @Extra
    int bar1;
    @Extra
    long bar2;

    @Extra
    @Nullable
    String hoge;

    @Extra
    @Nullable
    String fuga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        Detail2ActivityIntentBuilder.inject(this, getIntent());
    }
}
