package com.example.quickcart.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// ShippingAddress class to represent the shipping details
public class ShippingAddress implements Parcelable {
    private String fullName;
    private String phone;
    private String houseNumber;
    private String streetName;
    private String city;
    private String postalCode;
    private String province;

    public ShippingAddress() {
    }

    // Constructor
    public ShippingAddress(String fullName, String phone, String houseNumber, String streetName,
                           String city, String postalCode, String province) {
        this.fullName = fullName;
        this.phone = phone;
        this.houseNumber = houseNumber;
        this.streetName = streetName;
        this.city = city;
        this.postalCode = postalCode;
        this.province = province;
    }

    protected ShippingAddress(Parcel in) {
        fullName = in.readString();
        phone = in.readString();
        houseNumber = in.readString();
        streetName = in.readString();
        city = in.readString();
        postalCode = in.readString();
        province = in.readString();
    }

    public static final Creator<ShippingAddress> CREATOR = new Creator<ShippingAddress>() {
        @Override
        public ShippingAddress createFromParcel(Parcel in) {
            return new ShippingAddress(in);
        }

        @Override
        public ShippingAddress[] newArray(int size) {
            return new ShippingAddress[size];
        }
    };

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(fullName);
        parcel.writeString(phone);
        parcel.writeString(houseNumber);
        parcel.writeString(streetName);
        parcel.writeString(city);
        parcel.writeString(postalCode);
        parcel.writeString(province);
    }
}

