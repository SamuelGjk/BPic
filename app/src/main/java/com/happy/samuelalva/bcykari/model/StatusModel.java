package com.happy.samuelalva.bcykari.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public class StatusModel implements Parcelable {
    public String author;
    public String avatar;
    public String cover;
    public String detail;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(avatar);
        dest.writeString(cover);
        dest.writeString(detail);
    }

    public static final Parcelable.Creator<StatusModel> CREATOR = new Parcelable.Creator<StatusModel>() {
        @Override
        public StatusModel createFromParcel(Parcel source) {
            StatusModel ret = new StatusModel();
            ret.author = source.readString();
            ret.avatar = source.readString();
            ret.cover = source.readString();
            ret.detail = source.readString();
            return ret;
        }

        @Override
        public StatusModel[] newArray(int size) {
            return new StatusModel[size];
        }
    };
}
