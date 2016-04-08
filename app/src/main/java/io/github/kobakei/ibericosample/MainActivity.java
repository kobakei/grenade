package io.github.kobakei.ibericosample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void onButton1Clicked(View view) {
        Toast.makeText(this, "button1", Toast.LENGTH_SHORT).show();

        startActivity(new Detail1ActivityIntentBuilder("hoge", 123)
                .flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                .build(this));
    }

    void onButton2Clicked(View view) {
        Toast.makeText(this, "button2", Toast.LENGTH_SHORT).show();
    }
}
