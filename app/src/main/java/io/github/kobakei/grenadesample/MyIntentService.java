package io.github.kobakei.grenadesample;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import io.github.kobakei.grenade.annotation.Extra;
import io.github.kobakei.grenade.annotation.Navigator;

@Navigator
public class MyIntentService extends IntentService {

    private static final String TAG = MyIntentService.class.getSimpleName();

    public static final String ACTION_FOO = "io.github.kobakei.grenadesample.action.FOO";
    public static final String ACTION_BAZ = "io.github.kobakei.grenadesample.action.BAZ";

    @Extra
    String param1;
    @Extra
    String param2;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MyIntentServiceNavigator.inject(this, intent);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        String text = "Foo: " + param1 + ", " + param2;
        Log.v(TAG, text);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        String text = "Baz: " + param1 + ", " + param2;
        Log.v(TAG, text);
    }
}
