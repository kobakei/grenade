package io.github.kobakei.grenadesample;

import android.content.Context;
import android.content.Intent;
import io.github.kobakei.grenadesample.entity.User;
import java.lang.String;
import java.util.List;
import org.parceler.Parcels;

/**
 * Launcher of Detail4Activity */
public class Detail4ActivityNavigator {
  private User user;

  private List<User> friends;

  private int flags;

  private String action;

  /**
   * Constructor with required params */
  public Detail4ActivityNavigator(User user, List<User> friends) {
    this.user = user;
    this.friends = friends;
  }

  /**
   * Add intent flags */
  public Detail4ActivityNavigator flags(int flags) {
    this.flags = flags;
    return this;
  }

  /**
   * Set action */
  public Detail4ActivityNavigator action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Build intent */
  public Intent build(Context context) {
    Intent intent = new Intent(context, Detail4Activity.class);
    intent.putExtra("user", Parcels.wrap(this.user));
    intent.putExtra("friends", Parcels.wrap(this.friends));
    intent.addFlags(this.flags);
    intent.setAction(this.action);
    return intent;
  }

  /**
   * Inject fields of activity from intent */
  public static void inject(Detail4Activity target, Intent intent) {
    target.user = Parcels.unwrap(intent.getParcelableExtra("user"));
    target.friends = Parcels.unwrap(intent.getParcelableExtra("friends"));
  }

  /**
   * Call this method in your Activity's onActivityResult */
  public static void onActivityResult(Detail4Activity target, int requestCode, int resultCode, Intent intent) {
  }
}
