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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void onButton1Clicked(View view) {
        startActivity(new Detail1ActivityNavigator("foo", 123)
                .hoge("hoge")
                .fuga("fuga")
                .flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                .build(this));
    }

    void onButton21Clicked(View view) {
        startActivity(new Detail2ActivityNavigator("foo", 123).build(this));
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
    }

    @OnActivityResult(requestCode = REQ_CODE_DETAIL1, resultCodes = {Activity.RESULT_OK})
    void onActivityResult_Detail1_OK() {
        Toast.makeText(this, "onActivityResult_Detail1_OK", Toast.LENGTH_SHORT).show();
    }
}
