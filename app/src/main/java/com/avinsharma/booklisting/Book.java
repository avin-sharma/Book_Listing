package com.avinsharma.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Avin on 11-11-2016.
 */
public class Book implements Parcelable {
    String title;
    String description;
    String author;

    public Book(String title, String description, String author) {
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(author);
    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Book createFromParcel(Parcel parcel) {
            return new Book(parcel);
        }

        @Override
        public Book[] newArray(int i) {
            return new Book[i];
        }
    };

    public Book(Parcel parcel){
        title = parcel.readString();
        description = parcel.readString();
        author = parcel.readString();
    }
}
