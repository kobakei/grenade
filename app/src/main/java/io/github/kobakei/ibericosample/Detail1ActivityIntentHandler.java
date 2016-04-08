package io.github.kobakei.ibericosample;

import android.content.Intent;

/**
 * Created by keisukekobayashi on 16/04/08.
 */
public class Detail1ActivityIntentHandler {

    public static void inject(Detail1Activity target, Intent intent) {
        target.foo = intent.getStringExtra("foo");
        target.bar = intent.getIntExtra("bar", 0);
        target.hoge = intent.getStringExtra("hoge");
        target.fuga = intent.getStringExtra("fuga");
    }

}
