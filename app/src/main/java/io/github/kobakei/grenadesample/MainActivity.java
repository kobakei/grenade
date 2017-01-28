package io.github.kobakei.grenadesample;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.kobakei.grenade.annotation.Navigator;
import io.github.kobakei.grenade.annotation.OnActivityResult;
import io.github.kobakei.grenadesample.entity.User;

@Navigator
public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_DETAIL1 = 1001;
    private static final int REQ_CODE_DETAIL2 = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void onButton1Clicked(View view) {
        Intent intent = new Detail1ActivityNavigator("foo", 123)
                .hoge("hoge")
                .fuga("fuga")
                .flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                .build(this);
        startActivityForResult(intent, REQ_CODE_DETAIL1);
    }

    void onButton21Clicked(View view) {
        Intent intent = new Detail2ActivityNavigator("foo", 12345).build(this);
        startActivityForResult(intent, REQ_CODE_DETAIL2);
    }

    void onButton22Clicked(View view) {
        startActivity(new Detail2ActivityNavigator("foo", true).build(this));
    }

    void onButton3Clicked(View view) {
        startActivity(new Detail3ActivityNavigator(
                "Hello world",
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
                new int[] {1,2,3},
                new long[] {},
                new short[] {},
                new float[] {},
                new double[] {},
                new boolean[] {},
                new char[] {},
                new byte[] {},
                new String[] {"AAA", "BBB", "CCC"},
                new Parcelable[] {},
                new CharSequence[] {},
                null,
                new ArrayList<String>(){{
                    add("aaa");
                    add("bbb");
                    add("ccc");
                }},
                new ArrayList<CharSequence>(),
                new ArrayList<Integer>(),
                new ArrayList<Parcelable>()
                ).build(this));
    }

    void onButton4Clicked(View view) {
        User user = new User("Jack", "Bauer", 50);
        List<User> friends = new ArrayList<>();
        friends.add(new User("Chloe", "O'brian", 30));
        friends.add(new User("Tony", "Almeida", 45));
        startActivity(new Detail4ActivityNavigator(user, friends).build(this));
    }

    void onButton5Clicked(View view) {
        startService(new MyIntentServiceNavigator("hoge", "fuga")
                .action(MyIntentService.ACTION_FOO)
                .build(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainActivityNavigator.onActivityResult(this, requestCode, resultCode, data);
    }

    @OnActivityResult(requestCode = REQ_CODE_DETAIL1, resultCodes = {Activity.RESULT_OK})
    void onDetail1Ok(String p1, int p2, float p3, double p4, short p5, long p6) {
        String text = "";
        text += p1 + ", ";
        text += p2 + ", ";
        text += p3 + ", ";
        text += p4 + ", ";
        text += p5 + ", ";
        text += p6 + ", ";
        Toast.makeText(this, "Detail1 OK: " + text, Toast.LENGTH_SHORT).show();
    }

    @OnActivityResult(requestCode = REQ_CODE_DETAIL1, resultCodes = {Activity.RESULT_CANCELED})
    void onDetail1Canceled() {
        Toast.makeText(this, "Detail1 CANCELED", Toast.LENGTH_SHORT).show();
    }

    @OnActivityResult(requestCode = REQ_CODE_DETAIL2, resultCodes = {Activity.RESULT_OK, Activity.RESULT_CANCELED})
    void onDetail2() {
        Toast.makeText(this, "Detail2 OK/CANCELED", Toast.LENGTH_SHORT).show();
    }
}
