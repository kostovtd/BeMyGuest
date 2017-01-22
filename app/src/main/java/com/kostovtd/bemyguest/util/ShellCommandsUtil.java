package com.kostovtd.bemyguest.util;

/**
 * Created by kostovtd on 06.01.17.
 */

public class ShellCommandsUtil {

    public static final String PRINT_CURRENT_DIRECTORY = "pwd";

    public static final String CHANGE_CURRENT_DIRECTORY = "cd";

    public static final String CHANGE_PERMISSIONS = "chmod";

    public static final String GET_CURRENT_PERMISSIONS_OCTAL = "stat -c \"%a\"";

    // PERMISSIONS

    public static final String FULL_READ_WRITE_PERMISSIONS = "666";
}
