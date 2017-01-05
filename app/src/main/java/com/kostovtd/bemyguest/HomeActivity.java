package com.kostovtd.bemyguest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kostovtd.bemyguest.util.PermissionsChecker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.chainfire.libsuperuser.Shell;

/**
 * Created by Todor on 12/17/2016.
 */

public class HomeActivity extends Activity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 1001;

    private static final String[] PERMISSIONS = new String[] {
            //TODO add permissions here
    };


    private PermissionsChecker permissionsChecker;


    @BindView(R.id.root_container)
    LinearLayout rootContainer;

    @BindView(R.id.button_test)
    Button bTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: hit");
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        permissionsChecker = new PermissionsChecker(this);

        bTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "SU available: " + Shell.SU.available());
                Log.d(TAG, "SU version: " + Shell.SU.version(true));
                List<String> resultString = Shell.SU.run("ls /data/system/users/");
                Log.d(TAG, "result size: " + resultString.size());
                for(String result : resultString) {
                    Log.d(TAG, result + "\n");
                }
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

    private void testCreateUser() {
        Intent createUserIntent = UserManager.createUserCreationIntent("userName", "accoutName", null, null);
        startActivityForResult(createUserIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
