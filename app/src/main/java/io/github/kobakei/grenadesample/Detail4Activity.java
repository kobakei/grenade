package io.github.kobakei.grenadesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kobakei.grenade.annotation.Extra;
import io.github.kobakei.grenade.annotation.Navigator;
import io.github.kobakei.grenadesample.entity.User;

@Navigator
public class Detail4Activity extends AppCompatActivity {

    @Extra
    User user;
    @Extra
    List<User> friends;

    @Bind(R.id.firstName)
    TextView firstNameView;
    @Bind(R.id.lastName)
    TextView lastNameView;
    @Bind(R.id.age)
    TextView ageView;
    @Bind(R.id.friends)
    TextView friendsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail4);
        ButterKnife.bind(this);
        Detail4ActivityNavigator.inject(this, getIntent());

        firstNameView.setText(user.firstName);
        lastNameView.setText(user.lastName);
        ageView.setText(String.valueOf(user.age));
        String friendsText = "";
        for (User friend : friends) {
            friendsText += friend.firstName + " ";
        }
        friendsView.setText(friendsText);
    }
}
