package io.github.kobakei.ibericosample;

import android.content.Context;
import android.content.Intent;

/**
 * TODO autogen
 * Created by keisukekobayashi on 16/04/08.
 */
public class Detail1ActivityIntentBuilder {

    private String foo;
    private int bar;

    private String hoge;
    private String fuga;

    private int flags;

    public Detail1ActivityIntentBuilder(String foo, int bar) {
        this.foo = foo;
        this.bar = bar;
    }

    public Intent build(Context context) {
        Intent intent = new Intent(context, Detail1Activity.class);
        intent.putExtra("foo", foo);
        intent.putExtra("bar", bar);
        intent.putExtra("hoge", hoge);
        intent.putExtra("fuga", fuga);
        return intent;
    }

    public Detail1ActivityIntentBuilder flags(int flags) {
        this.flags = flags;
        return this;
    }

    public Detail1ActivityIntentBuilder hoge(String hoge) {
        this.hoge = hoge;
        return this;
    }

    public Detail1ActivityIntentBuilder fuga(String fuga) {
        this.fuga = fuga;
        return this;
    }
}
