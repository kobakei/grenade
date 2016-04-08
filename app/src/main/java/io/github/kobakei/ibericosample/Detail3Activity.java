package io.github.kobakei.ibericosample;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.kobakei.iberico.annotation.Extra;
import io.github.kobakei.iberico.annotation.Launcher;

/**
 * List of all supported field types.
 */
@Launcher
public class Detail3Activity extends AppCompatActivity {

    @Extra
    String arg1;
    @Extra
    int arg2;
    @Extra
    long arg3;
    @Extra
    short arg4;
    @Extra
    float arg5;
    @Extra
    double arg6;
    @Extra
    boolean arg7;
    @Extra
    Parcelable arg8;
    @Extra
    Bundle arg9;
    @Extra
    CharSequence arg10;
    @Extra
    char arg11;
    @Extra
    byte arg12;
    @Extra
    int[] arg13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail3);
    }
}
