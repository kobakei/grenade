package io.github.kobakei.grenadesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kobakei.grenade.annotation.Extra;
import io.github.kobakei.grenade.annotation.Navigator;
import io.github.kobakei.grenade.annotation.Optional;

/**
 * Simple sample
 */
@Navigator
public class Detail1Activity extends AppCompatActivity {

    @Extra
    public String foo;
    @Extra
    int bar;
    @Extra @Optional
    String hoge;
    @Extra @Optional
    String fuga;


    @Bind(R.id.foo)
    TextView fooView;
    @Bind(R.id.bar)
    TextView barView;
    @Bind(R.id.hoge)
    TextView hogeView;
    @Bind(R.id.fuga)
    TextView fugaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail1);
        ButterKnife.bind(this);

        Detail1ActivityNavigator.inject(this, getIntent());

        fooView.setText(foo);
        barView.setText(String.valueOf(bar));
        hogeView.setText(hoge);
        fugaView.setText(fuga);
    }

    public void onButtonClick(View view) {
        setResult(RESULT_OK, MainActivityNavigator.resultForOnDetail1Ok("hello", 123));
        finish();
    }
}
