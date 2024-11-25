package com.example.quickcart.Product;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

public class Product implements Parcelable {
    private String category;
    private String description;
    private int id;
    private ArrayList<String> images;
    private double price;
    private Rating rating; // Nested class for the "rating" field
    private String title;

    public Product() {}

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Rating getRating() {
        return rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    // Parcelable implementation
    protected Product(Parcel in) {
        id = in.readInt();
        title = in.readString();
        price = in.readDouble();
        description = in.readString();
        category = in.readString();
        images = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeDouble(price);
        parcel.writeString(description);
        parcel.writeString(category);
        parcel.writeStringList(images);
    }

    public static class Rating implements Parcelable  {
        private int count;
        private double rate;

        public Rating() {}

        protected Rating(Parcel in) {
            rate = in.readFloat();
            count = in.readInt();
        }

        public static final Creator<Rating> CREATOR = new Creator<Rating>() {
            @Override
            public Rating createFromParcel(Parcel in) {
                return new Rating(in);
            }

            @Override
            public Rating[] newArray(int size) {
                return new Rating[size];
            }
        };

        public double getRate() {
            return rate;
        }

        public int getCount() {
            return count;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public void setRate(float rate) {
            this.rate = rate;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeDouble(rate);
            parcel.writeInt(count);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", images='" + images + '\'' +
                '}';
    }

    // equals and hascode implementation to use Product as a key in CartService class
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product product = (Product) obj;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
