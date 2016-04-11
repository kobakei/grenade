package io.github.kobakei.grenadesample;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kobakei.grenade.annotation.Extra;
import io.github.kobakei.grenade.annotation.Launcher;

/**
 * Sample of multiple constructor
 * Field names should be joined with ','.
 */
@Launcher({
        "foo,bar1",
        "foo,bar2"
})
public class Detail2Activity extends AppCompatActivity {

    @Extra
    String foo;
    @Extra
    int bar1;
    @Extra
    boolean bar2;

    @Extra @Nullable
    String hoge;
    @Extra @Nullable
    String fuga;


    @Bind(R.id.foo)
    TextView fooView;
    @Bind(R.id.bar1)
    TextView bar1View;
    @Bind(R.id.bar2)
    TextView bar2View;
    @Bind(R.id.hoge)
    TextView hogeView;
    @Bind(R.id.fuga)
    TextView fugaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        ButterKnife.bind(this);
        Detail2ActivityIntentBuilder.inject(this, getIntent());

        fooView.setText(foo);
        bar1View.setText(String.valueOf(bar1));
        bar2View.setText(String.valueOf(bar2));
        hogeView.setText(hoge);
        fugaView.setText(fuga);
    }
}
