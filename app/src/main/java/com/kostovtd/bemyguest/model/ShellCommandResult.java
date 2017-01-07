package com.kostovtd.bemyguest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kostovtd on 07.01.17.
 */

public class ShellCommandResult implements Parcelable {

    private List<String> resultList;


    public ShellCommandResult(List<String> resultList) {
        this.resultList = resultList;
    }


    public List<String> getResultList() {
        return resultList;
    }


    public void setResultList(List<String> resultList) {
        this.resultList = resultList;
    }


    @Override
    public String toString() {
        String resultStr = "";

        for(String result : resultList) {
            resultStr += result + "\n";
        }

        return resultStr;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.resultList);
    }

    protected ShellCommandResult(Parcel in) {
        this.resultList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ShellCommandResult> CREATOR = new Parcelable.Creator<ShellCommandResult>() {
        @Override
        public ShellCommandResult createFromParcel(Parcel source) {
            return new ShellCommandResult(source);
        }

        @Override
        public ShellCommandResult[] newArray(int size) {
            return new ShellCommandResult[size];
        }
    };
}
