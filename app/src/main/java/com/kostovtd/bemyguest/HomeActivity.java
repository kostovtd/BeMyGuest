package com.kostovtd.bemyguest;

import android.app.Activity;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kostovtd.bemyguest.util.PermissionsChecker;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Todor on 12/17/2016.
 */

public class HomeActivity extends Activity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private static final String[] PERMISSIONS = new String[] {
            //TODO add permissions here
    };


    private PermissionsChecker permissionsChecker;


    @BindView(R.id.root_container)
    LinearLayout rootContainer;

    @BindView(R.id.button_count_users)
    Button bCountUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        permissionsChecker = new PermissionsChecker(this);

        bCountUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManager userManager = (UserManager) getSystemService(USER_SERVICE);
                int numberOfUsers = userManager.getUserCount();
                String toastMessage = "Number of users: " + numberOfUsers;
                Toast.makeText(HomeActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(permissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PermissionsActivity.PERMISSION_REQUEST_CODE, PERMISSIONS);
    }
}
