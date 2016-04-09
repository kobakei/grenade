package io.github.kobakei.ibericosample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void onButton1Clicked(View view) {
        startActivity(new Detail1ActivityIntentBuilder("foo", 123)
                .hoge("hoge")
                .fuga("fuga")
                .flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                .build(this));
    }

    void onButton21Clicked(View view) {
        startActivity(new Detail2ActivityIntentBuilder("foo", 123).build(this));
    }

    void onButton22Clicked(View view) {
        startActivity(new Detail2ActivityIntentBuilder("foo", 10000L).build(this));
    }

    void onButton3Clicked(View view) {
        startActivity(new Detail3ActivityIntentBuilder(
                "arg1",
                123,
                10000L,
                (short)123,
                0.5f,
                0.5,
                true,
                null,
                null,
                null,
                'a',
                (byte)0x12,
                new int[] {1,2,3}
                ).build(this));
    }
}
