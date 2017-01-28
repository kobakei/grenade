package io.github.kobakei.grenadesample;

import android.content.Context;
import android.content.Intent;
import java.lang.String;

/**
 * Launcher of MainActivity */
public class MainActivityNavigator {
  private int flags;

  private String action;

  /**
   * Constructor with required params */
  public MainActivityNavigator() {
  }

  /**
   * Add intent flags */
  public MainActivityNavigator flags(int flags) {
    this.flags = flags;
    return this;
  }

  /**
   * Set action */
  public MainActivityNavigator action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Build intent */
  public Intent build(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.addFlags(this.flags);
    intent.setAction(this.action);
    return intent;
  }

  /**
   * Inject fields of activity from intent */
  public static void inject(MainActivity target, Intent intent) {
  }

  /**
   * Create result intent */
  public static Intent resultForOnDetail1Ok(String param0, int param1, float param2, double param3, short param4, long param5) {
    Intent intent = new Intent();
    intent.putExtra("param0", param0);
    intent.putExtra("param1", param1);
    intent.putExtra("param2", param2);
    intent.putExtra("param3", param3);
    intent.putExtra("param4", param4);
    intent.putExtra("param5", param5);
    return intent;
  }

  /**
   * Call this method in your Activity's onActivityResult */
  public static void onActivityResult(MainActivity target, int requestCode, int resultCode, Intent intent) {
    if (requestCode == 1001 && java.util.Arrays.asList(-1).contains(resultCode)) {
      String param0 = intent.getStringExtra("param0");
      int param1 = intent.getIntExtra("param1", 0);
      float param2 = intent.getFloatExtra("param2", 0.0f);
      double param3 = intent.getDoubleExtra("param3", 0.0);
      short param4 = intent.getShortExtra("param4", (short)0);
      long param5 = intent.getLongExtra("param5", 0L);
      target.onDetail1Ok(param0,param1,param2,param3,param4,param5);
    }
    if (requestCode == 1001 && java.util.Arrays.asList(0).contains(resultCode)) {
      target.onDetail1Canceled();
    }
    if (requestCode == 1002 && java.util.Arrays.asList(-1,0).contains(resultCode)) {
      target.onDetail2();
    }
  }
}
