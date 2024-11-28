package com.example.quickcart.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetails implements Parcelable {
    private int id;
    private String title;
    private int count;
    private double price;

    public ProductDetails() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {  return price * count; }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductDetails(int id, String title, int count, double price) {
        this.id = id;
        this.title = title;
        this.count = count;
        this.price = price;
    }

    protected ProductDetails(Parcel in) {
        id = in.readInt();
        title = in.readString();
        count = in.readInt();
        price = in.readDouble();
    }

    public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>() {
        @Override
        public ProductDetails createFromParcel(Parcel in) {
            return new ProductDetails(in);
        }

        @Override
        public ProductDetails[] newArray(int size) {
            return new ProductDetails[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(count);
        parcel.writeDouble(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
