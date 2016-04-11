package io.github.kobakei.grenadesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kobakei.grenade.annotation.Extra;
import io.github.kobakei.grenade.annotation.Launcher;
import io.github.kobakei.grenade.annotation.WithParceler;
import io.github.kobakei.grenadesample.entity.User;

@Launcher
public class Detail4Activity extends AppCompatActivity {

    @Extra @WithParceler
    User user;

    @Bind(R.id.firstName)
    TextView firstNameView;
    @Bind(R.id.lastName)
    TextView lastNameView;
    @Bind(R.id.age)
    TextView ageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail4);
        ButterKnife.bind(this);
        Detail4ActivityIntentBuilder.inject(this, getIntent());

        firstNameView.setText(user.firstName);
        lastNameView.setText(user.lastName);
        ageView.setText(String.valueOf(user.age));
    }
}
