package com.kostovtd.bemyguest;

import android.app.Activity;
import android.os.Bundle;
import android.os.UserManager;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Todor on 12/17/2016.
 */

public class HomeActivity extends Activity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @BindView(R.id.root_container)
    LinearLayout rootContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
