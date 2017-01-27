package io.github.kobakei.grenadesample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import java.io.Serializable;
import java.lang.CharSequence;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;

/**
 * Launcher of Detail3Activity */
public class Detail3ActivityNavigator {
  private String arg1;

  private int arg2;

  private long arg3;

  private short arg4;

  private float arg5;

  private double arg6;

  private boolean arg7;

  private Parcelable arg8;

  private Bundle arg9;

  private CharSequence arg10;

  private char arg11;

  private byte arg12;

  private int[] arg13;

  private long[] arg14;

  private short[] arg15;

  private float[] arg16;

  private double[] arg17;

  private boolean[] arg18;

  private char[] arg19;

  private byte[] arg20;

  private String[] arg21;

  private Parcelable[] arg22;

  private CharSequence[] arg23;

  private Serializable arg24;

  private ArrayList<String> arg25;

  private ArrayList<CharSequence> arg26;

  private ArrayList<Integer> arg27;

  private ArrayList<Parcelable> arg28;

  private int flags;

  private String action;

  /**
   * Constructor with required params */
  public Detail3ActivityNavigator(String arg1, int arg2, long arg3, short arg4, float arg5, double arg6, boolean arg7, Parcelable arg8, Bundle arg9, CharSequence arg10, char arg11, byte arg12, int[] arg13, long[] arg14, short[] arg15, float[] arg16, double[] arg17, boolean[] arg18, char[] arg19, byte[] arg20, String[] arg21, Parcelable[] arg22, CharSequence[] arg23, Serializable arg24, ArrayList<String> arg25, ArrayList<CharSequence> arg26, ArrayList<Integer> arg27, ArrayList<Parcelable> arg28) {
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.arg3 = arg3;
    this.arg4 = arg4;
    this.arg5 = arg5;
    this.arg6 = arg6;
    this.arg7 = arg7;
    this.arg8 = arg8;
    this.arg9 = arg9;
    this.arg10 = arg10;
    this.arg11 = arg11;
    this.arg12 = arg12;
    this.arg13 = arg13;
    this.arg14 = arg14;
    this.arg15 = arg15;
    this.arg16 = arg16;
    this.arg17 = arg17;
    this.arg18 = arg18;
    this.arg19 = arg19;
    this.arg20 = arg20;
    this.arg21 = arg21;
    this.arg22 = arg22;
    this.arg23 = arg23;
    this.arg24 = arg24;
    this.arg25 = arg25;
    this.arg26 = arg26;
    this.arg27 = arg27;
    this.arg28 = arg28;
  }

  /**
   * Add intent flags */
  public Detail3ActivityNavigator flags(int flags) {
    this.flags = flags;
    return this;
  }

  /**
   * Set action */
  public Detail3ActivityNavigator action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Build intent */
  public Intent build(Context context) {
    Intent intent = new Intent(context, Detail3Activity.class);
    intent.putExtra("__arg1__", this.arg1);
    intent.putExtra("arg2", this.arg2);
    intent.putExtra("arg3", this.arg3);
    intent.putExtra("arg4", this.arg4);
    intent.putExtra("arg5", this.arg5);
    intent.putExtra("arg6", this.arg6);
    intent.putExtra("arg7", this.arg7);
    intent.putExtra("arg8", this.arg8);
    intent.putExtra("arg9", this.arg9);
    intent.putExtra("arg10", this.arg10);
    intent.putExtra("arg11", this.arg11);
    intent.putExtra("arg12", this.arg12);
    intent.putExtra("arg13", this.arg13);
    intent.putExtra("arg14", this.arg14);
    intent.putExtra("arg15", this.arg15);
    intent.putExtra("arg16", this.arg16);
    intent.putExtra("arg17", this.arg17);
    intent.putExtra("arg18", this.arg18);
    intent.putExtra("arg19", this.arg19);
    intent.putExtra("arg20", this.arg20);
    intent.putExtra("arg21", this.arg21);
    intent.putExtra("arg22", this.arg22);
    intent.putExtra("arg23", this.arg23);
    intent.putExtra("arg24", this.arg24);
    intent.putStringArrayListExtra("arg25", this.arg25);
    intent.putCharSequenceArrayListExtra("arg26", this.arg26);
    intent.putIntegerArrayListExtra("arg27", this.arg27);
    intent.putParcelableArrayListExtra("arg28", this.arg28);
    intent.addFlags(this.flags);
    intent.setAction(this.action);
    return intent;
  }

  /**
   * Inject fields of activity from intent */
  public static void inject(Detail3Activity target, Intent intent) {
    target.arg1 = intent.getStringExtra("__arg1__");
    target.arg2 = intent.getIntExtra("arg2", 0);
    target.arg3 = intent.getLongExtra("arg3", 0L);
    target.arg4 = intent.getShortExtra("arg4", (short)0);
    target.arg5 = intent.getFloatExtra("arg5", 0.0f);
    target.arg6 = intent.getDoubleExtra("arg6", 0.0);
    target.arg7 = intent.getBooleanExtra("arg7", false);
    target.arg8 = intent.getParcelableExtra("arg8");
    target.arg9 = intent.getBundleExtra("arg9");
    target.arg10 = intent.getCharSequenceExtra("arg10");
    target.arg11 = intent.getCharExtra("arg11", (char)0);
    target.arg12 = intent.getByteExtra("arg12", (byte)0);
    target.arg13 = intent.getIntArrayExtra("arg13");
    target.arg14 = intent.getLongArrayExtra("arg14");
    target.arg15 = intent.getShortArrayExtra("arg15");
    target.arg16 = intent.getFloatArrayExtra("arg16");
    target.arg17 = intent.getDoubleArrayExtra("arg17");
    target.arg18 = intent.getBooleanArrayExtra("arg18");
    target.arg19 = intent.getCharArrayExtra("arg19");
    target.arg20 = intent.getByteArrayExtra("arg20");
    target.arg21 = intent.getStringArrayExtra("arg21");
    target.arg22 = intent.getParcelableArrayExtra("arg22");
    target.arg23 = intent.getCharSequenceArrayExtra("arg23");
    target.arg24 = intent.getSerializableExtra("arg24");
    target.arg25 = intent.getStringArrayListExtra("arg25");
    target.arg26 = intent.getCharSequenceArrayListExtra("arg26");
    target.arg27 = intent.getIntegerArrayListExtra("arg27");
    target.arg28 = intent.getParcelableArrayListExtra("arg28");
  }

  /**
   * Call this method in your Activity's onActivityResult */
  public static void onActivityResult(Detail3Activity target, int requestCode, int resultCode, Intent intent) {
  }
}
