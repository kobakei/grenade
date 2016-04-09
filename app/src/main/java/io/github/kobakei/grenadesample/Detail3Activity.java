package io.github.kobakei.grenadesample;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

import io.github.kobakei.grenade.annotation.Extra;
import io.github.kobakei.grenade.annotation.Launcher;

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
    @Extra
    long[] arg14;
    @Extra
    short[] arg15;
    @Extra
    float[] arg16;
    @Extra
    double[] arg17;
    @Extra
    boolean[] arg18;
    @Extra
    char[] arg19;
    @Extra
    byte[] arg20;
    @Extra
    String[] arg21;
    @Extra
    Parcelable[] arg22;
    @Extra
    CharSequence[] arg23;
    @Extra
    Serializable arg24;
    @Extra
    ArrayList<String> arg25;
    @Extra
    ArrayList<CharSequence> arg26;
    @Extra
    ArrayList<Integer> arg27;
    @Extra
    ArrayList<Parcelable> arg28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail3);
        Detail3ActivityIntentBuilder.inject(this, getIntent());
    }
}
