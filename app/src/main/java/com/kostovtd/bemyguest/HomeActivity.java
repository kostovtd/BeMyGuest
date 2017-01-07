package com.kostovtd.bemyguest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kostovtd.bemyguest.manager.GuestManager;
import com.kostovtd.bemyguest.manager.StatusMessage;
import com.kostovtd.bemyguest.model.ShellCommandResult;
import com.kostovtd.bemyguest.util.KeyUtil;
import com.kostovtd.bemyguest.util.PermissionsChecker;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        final Context context = HomeActivity.this;

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int status = msg.what;

                switch (status) {
                    case StatusMessage.NO_SU_AVAILABLE:
                        Snackbar.make(rootContainer, R.string.status_no_su_available, Snackbar.LENGTH_LONG).show();
                        break;
                    case StatusMessage.ERROR_EXECUTING_SU_COMMANDS:
                        Snackbar.make(rootContainer, R.string.status_error_executing_su_command,
                                Snackbar.LENGTH_LONG).show();
                        break;
                    case StatusMessage.MISSING_DIRECTORY:
                        if(msg.getData() != null) {
                            Bundle bundle = msg.getData();
                            String errorStr = bundle.getString(KeyUtil.MISSING_DIRECTORY_KEY);
                            Snackbar.make(rootContainer, errorStr, Snackbar.LENGTH_LONG).show();
                        }
                        break;
                    case StatusMessage.CREATE_GUEST_SUCCESSFUL:
                        if(msg.getData() != null) {
                            Bundle bundle = msg.getData();
                            ShellCommandResult shellCommandResult = bundle.getParcelable(KeyUtil.CREATE_GUEST_RESULT_KEY);
                            if(shellCommandResult != null)
                                Log.d(TAG, "Shell result: " + shellCommandResult.toString());
                        }
                        break;
                }


            }
        };


        final GuestManager guestManager = new GuestManager(this, mHandler);

        bTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestManager.createGuestUser();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
