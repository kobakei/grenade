package io.github.kobakei.grenadesample;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kobakei.grenade.annotation.Extra;
import io.github.kobakei.grenade.annotation.Navigator;

/**
 * List of all supported field types.
 */
@Navigator
public class Detail3Activity extends AppCompatActivity {

    // test option to specify key
    @Extra(key = "__arg1__")
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


    @Bind(R.id.arg1)
    TextView arg1View;
    @Bind(R.id.arg21)
    TextView arg21View;
    @Bind(R.id.arg25)
    TextView arg25View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail3);
        ButterKnife.bind(this);
        Detail3ActivityNavigator.inject(this, getIntent());

        arg1View.setText(arg1);
        String text21 = "";
        for (String s : arg21) {
            text21 += s + " ";
        }
        arg21View.setText(text21);
        String text25 = "";
        for (String s : arg25) {
            text25 += s + " ";
        }
        arg25View.setText(text25);
    }
}
