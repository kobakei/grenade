package io.github.kobakei.ibericosample;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.kobakei.iberico.annotation.Intent;
import io.github.kobakei.iberico.annotation.Extra;

@Intent
public class Detail1Activity extends AppCompatActivity {

    @Extra
    String foo;
    @Extra
    int bar;

    @Extra
    @Nullable
    String hoge;

    @Extra
    @Nullable
    String fuga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail1);
    }
}
