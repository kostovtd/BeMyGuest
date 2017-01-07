package com.kostovtd.bemyguest.manager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.kostovtd.bemyguest.ShellCommandResult;
import com.kostovtd.bemyguest.util.KeyUtil;
import com.kostovtd.bemyguest.util.ShellCommandsUtil;

import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by kostovtd on 06.01.17.
 */

public class GuestManager {

    private static final String TAG = GuestManager.class.getSimpleName();
    private static final String USER_DIR = "/data/system/users/";


    private Handler mHandler;


    public GuestManager(Handler handler) {

        this.mHandler = handler;

    }



    public void createGuestUser() {
        Log.d(TAG, "createGuestUser: hit");


        final Runnable createGuestRunnable = new Runnable() {
            @Override
            public void run() {
                // Moves the current Thread into the background
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                boolean suAvailable = isSUAvailable();

                if(suAvailable) {
                    String goToUserDirCommand = ShellCommandsUtil.CHANGE_CURRENT_DIRECTORY + " " + USER_DIR;
                    String printFilesInUserDirCommand = ShellCommandsUtil.PRINT_CURRENT_DIRECTORY;

                    ArrayList<String> suCommandsList = new ArrayList<>();
                    suCommandsList.add(goToUserDirCommand);
                    suCommandsList.add(printFilesInUserDirCommand);

                    List<String> resultList = Shell.SU.run(suCommandsList);
                    ShellCommandResult shellCommandResult = new ShellCommandResult(resultList);
                    Message message = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(KeyUtil.CREATE_GUEST_RESULT_KEY, shellCommandResult);
                    message.what = StatusMessage.CREATE_GUEST_SUCCESSFULL;
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                } else {
                    mHandler.sendEmptyMessage(StatusMessage.NO_SU_AVAILABLE);
                }

            }
        };


        Thread createGuestThread = new Thread(createGuestRunnable);
        createGuestThread.start();
    }


    private boolean isSUAvailable() {
        return Shell.SU.available();
    }
}
