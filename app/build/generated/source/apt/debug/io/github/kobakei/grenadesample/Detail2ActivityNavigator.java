package io.github.kobakei.grenadesample;

import android.content.Context;
import android.content.Intent;
import java.lang.String;

/**
 * Launcher of Detail2Activity */
public class Detail2ActivityNavigator {
  private String foo;

  private int bar1;

  private boolean bar2;

  private String hoge;

  private String fuga;

  private int flags;

  private String action;

  /**
   * Constructor with required params */
  public Detail2ActivityNavigator(String foo, int bar1) {
    this.foo = foo;
    this.bar1 = bar1;
  }

  /**
   * Constructor with required params */
  public Detail2ActivityNavigator(String foo, boolean bar2) {
    this.foo = foo;
    this.bar2 = bar2;
  }

  /**
   * Set optional field */
  public Detail2ActivityNavigator hoge(String hoge) {
    this.hoge = hoge;
    return this;
  }

  /**
   * Set optional field */
  public Detail2ActivityNavigator fuga(String fuga) {
    this.fuga = fuga;
    return this;
  }

  /**
   * Add intent flags */
  public Detail2ActivityNavigator flags(int flags) {
    this.flags = flags;
    return this;
  }

  /**
   * Set action */
  public Detail2ActivityNavigator action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Build intent */
  public Intent build(Context context) {
    Intent intent = new Intent(context, Detail2Activity.class);
    intent.putExtra("foo", this.foo);
    intent.putExtra("bar1", this.bar1);
    intent.putExtra("bar2", this.bar2);
    intent.putExtra("hoge", this.hoge);
    intent.putExtra("fuga", this.fuga);
    intent.addFlags(this.flags);
    intent.setAction(this.action);
    return intent;
  }

  /**
   * Inject fields of activity from intent */
  public static void inject(Detail2Activity target, Intent intent) {
    target.foo = intent.getStringExtra("foo");
    target.bar1 = intent.getIntExtra("bar1", 0);
    target.bar2 = intent.getBooleanExtra("bar2", false);
    if (intent.hasExtra("hoge")) {
      target.hoge = intent.getStringExtra("hoge");
    }
    if (intent.hasExtra("fuga")) {
      target.fuga = intent.getStringExtra("fuga");
    }
  }
}
