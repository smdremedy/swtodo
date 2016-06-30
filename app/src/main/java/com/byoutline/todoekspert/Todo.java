package com.byoutline.todoekspert;

import android.os.Parcel;
import android.os.Parcelable;

public class Todo implements Parcelable {

    public String task;
    public boolean done;

    public Todo() {
    }

    protected Todo(Parcel in) {
        task = in.readString();
        done = in.readByte() != 0;
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(task);
        dest.writeByte((byte) (done ? 1 : 0));
    }

    @Override
    public String toString() {
        return "Todo{" +
                "task='" + task + '\'' +
                ", done=" + done +
                '}';
    }
}
