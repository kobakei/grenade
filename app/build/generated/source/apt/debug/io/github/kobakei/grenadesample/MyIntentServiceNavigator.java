package io.github.kobakei.grenadesample;

import android.content.Context;
import android.content.Intent;
import java.lang.String;

/**
 * Launcher of MyIntentService */
public class MyIntentServiceNavigator {
  private String param1;

  private String param2;

  private int flags;

  private String action;

  /**
   * Constructor with required params */
  public MyIntentServiceNavigator(String param1, String param2) {
    this.param1 = param1;
    this.param2 = param2;
  }

  /**
   * Add intent flags */
  public MyIntentServiceNavigator flags(int flags) {
    this.flags = flags;
    return this;
  }

  /**
   * Set action */
  public MyIntentServiceNavigator action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Build intent */
  public Intent build(Context context) {
    Intent intent = new Intent(context, MyIntentService.class);
    intent.putExtra("param1", this.param1);
    intent.putExtra("param2", this.param2);
    intent.addFlags(this.flags);
    intent.setAction(this.action);
    return intent;
  }

  /**
   * Inject fields of activity from intent */
  public static void inject(MyIntentService target, Intent intent) {
    target.param1 = intent.getStringExtra("param1");
    target.param2 = intent.getStringExtra("param2");
  }
}
