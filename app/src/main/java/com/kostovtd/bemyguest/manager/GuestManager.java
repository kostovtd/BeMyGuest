package com.kostovtd.bemyguest.manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kostovtd.bemyguest.R;
import com.kostovtd.bemyguest.model.ShellCommandResult;
import com.kostovtd.bemyguest.util.KeyUtil;
import com.kostovtd.bemyguest.util.ShellCommandsUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by kostovtd on 06.01.17.
 */

public class GuestManager {

    private static final String TAG = GuestManager.class.getSimpleName();
    private static final String USER_DIR = "/data/system/users/";
    private static final String USER_LIST_FILE_NAME = "userlist.xml";


    private Handler mHandler;
    private Context mContext;


    public GuestManager(Context context, Handler handler) {
        this.mContext = context;
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

                    File usersDir = new File(USER_DIR);
                    boolean userDirExists = usersDir.exists();

                    if(userDirExists) {
                        File userListFile = new File(USER_DIR + USER_LIST_FILE_NAME);
                        boolean userListFileExists = userListFile.exists();

                        if(userListFileExists) {

                        } else {
                            String missingDirStr = mContext.getString(R.string.status_missing_directory);
                            String dirStr = USER_DIR + USER_LIST_FILE_NAME;
                            String errorStr = String.format(missingDirStr, dirStr);
                            Message errorMessage = constructErrorMessage(StatusMessage.MISSING_DIRECTORY,
                                    KeyUtil.CREATE_GUEST_RESULT_KEY, errorStr);
                            mHandler.sendMessage(errorMessage);
                        }
                    } else {
                        String missingDirStr = mContext.getString(R.string.status_missing_directory);
                        String errorStr = String.format(missingDirStr, USER_DIR);
                        Message errorMessage = constructErrorMessage(StatusMessage.MISSING_DIRECTORY,
                                KeyUtil.CREATE_GUEST_RESULT_KEY, errorStr);
                        mHandler.sendMessage(errorMessage);
                    }

                    // check if the USER_DIR exists DONE
                    // check if userlist.xml exists DONE
                    // parse userlist.xml DOM
                    // edit userlist.xml increase values
                    // create new user dir + new_user.xml
                    // create accounts.db + accounts.db-journal + package-restrictions.xml + settings.db
                    // + settings.db-journal
                    // reboot
                    String goToUserDirCommand = ShellCommandsUtil.CHANGE_CURRENT_DIRECTORY + " " + USER_DIR;
                    String printFilesInUserDirCommand = ShellCommandsUtil.PRINT_CURRENT_DIRECTORY ;

                    ArrayList<String> suCommandsList = new ArrayList<>();
                    suCommandsList.add(goToUserDirCommand);
                    suCommandsList.add(printFilesInUserDirCommand);

                    List<String> resultList = Shell.SU.run(suCommandsList);

                    if(resultList != null) {
                        ShellCommandResult shellCommandResult = new ShellCommandResult(resultList);
                        Message message = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(KeyUtil.CREATE_GUEST_RESULT_KEY, shellCommandResult);
                        message.what = StatusMessage.CREATE_GUEST_SUCCESSFUL;
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    } else {
                        mHandler.sendEmptyMessage(StatusMessage.ERROR_EXECUTING_SU_COMMANDS);
                    }

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


    /**
     * Construct a simple error {@link Message}, which contains
     * the cause of the error.
     * @param status A numerical representation of the cause of the error. Refer to {@link StatusMessage}.
     * @param key The key used for getting a value from {@link Bundle}.
     * @param message The actual error message.
     * @return A {@link Message} object.
     */
    private Message constructErrorMessage(int status, String key, String message) {
        Message errorMessage = mHandler.obtainMessage();
        Bundle bundle = new Bundle();

        bundle.putString(key, message);
        errorMessage.what = status;
        errorMessage.setData(bundle);

        return errorMessage;
    }
}
