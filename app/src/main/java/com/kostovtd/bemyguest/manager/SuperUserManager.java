package com.kostovtd.bemyguest.manager;

import com.kostovtd.bemyguest.util.ShellCommandsUtil;

import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by kostovtd on 22.01.17.
 */

/**
 * The class helps in the construction of SuperUser and Shell commands
 * and their execution. It has the possibility to execute multiple
 * commands at once.
 */
public class SuperUserManager {

    private static final String TAG = SuperUserManager.class.getSimpleName();


    private List<String> suCommandList;


    public SuperUserManager() {
        this.suCommandList = new ArrayList<>();
    }


    public SuperUserManager changeDirectory(String directory) {
        String changeDirectoryCommand = ShellCommandsUtil.CHANGE_CURRENT_DIRECTORY + " " + directory;

        suCommandList.add(changeDirectoryCommand);

        return this;
    }


    public SuperUserManager getFullReadWritePermissions(String fileName) {
        String getFullReadWritePermissionsCommand = ShellCommandsUtil.CHANGE_PERMISSIONS + " " +
                ShellCommandsUtil.FULL_READ_WRITE_PERMISSIONS + " " + fileName;

        suCommandList.add(getFullReadWritePermissionsCommand);

        return this;
    }


    public SuperUserManager getCurrentPermissions(String fileName) {
        String getCurrentPermissionsCommand = ShellCommandsUtil.GET_CURRENT_PERMISSIONS_OCTAL + " " + fileName;

        suCommandList.add(getCurrentPermissionsCommand);

        return this;
    }


    public List<String> executeCommands() {
        List<String> resultList = Shell.SU.run(suCommandList);

        return resultList;
    }

}
