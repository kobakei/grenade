package io.github.kobakei.grenadesample;

import android.content.Context;
import android.content.Intent;
import java.lang.String;

/**
 * Launcher of Detail1Activity */
public class Detail1ActivityNavigator {
  private String foo;

  private int bar;

  private String hoge;

  private String fuga;

  private int flags;

  private String action;

  /**
   * Constructor with required params */
  public Detail1ActivityNavigator(String foo, int bar) {
    this.foo = foo;
    this.bar = bar;
  }

  /**
   * Set optional field */
  public Detail1ActivityNavigator hoge(String hoge) {
    this.hoge = hoge;
    return this;
  }

  /**
   * Set optional field */
  public Detail1ActivityNavigator fuga(String fuga) {
    this.fuga = fuga;
    return this;
  }

  /**
   * Add intent flags */
  public Detail1ActivityNavigator flags(int flags) {
    this.flags = flags;
    return this;
  }

  /**
   * Set action */
  public Detail1ActivityNavigator action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Build intent */
  public Intent build(Context context) {
    Intent intent = new Intent(context, Detail1Activity.class);
    intent.putExtra("foo", this.foo);
    intent.putExtra("bar", this.bar);
    intent.putExtra("hoge", this.hoge);
    intent.putExtra("fuga", this.fuga);
    intent.addFlags(this.flags);
    intent.setAction(this.action);
    return intent;
  }

  /**
   * Inject fields of activity from intent */
  public static void inject(Detail1Activity target, Intent intent) {
    target.foo = intent.getStringExtra("foo");
    target.bar = intent.getIntExtra("bar", 0);
    if (intent.hasExtra("hoge")) {
      target.hoge = intent.getStringExtra("hoge");
    }
    if (intent.hasExtra("fuga")) {
      target.fuga = intent.getStringExtra("fuga");
    }
  }
}
