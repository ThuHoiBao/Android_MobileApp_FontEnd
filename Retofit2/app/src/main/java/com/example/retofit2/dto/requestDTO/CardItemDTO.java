package com.example.retofit2.dto.requestDTO;

import android.os.Parcel;
import android.os.Parcelable;

public class CardItemDTO implements Parcelable {
    private int cartItemId;
    private String productName;
    private String color;
    private double price;
    private int quantity;
    private String productImage;
    private boolean isSelected = false;
    private boolean outOfStockItems;
    private String message; // Ensure this is handled correctly

    public CardItemDTO(int cartItemId, String productName, String color, double price, int quantity, String productImage, boolean outOfStockItems, String message) {
        this.cartItemId = cartItemId;
        this.productName = productName;
        this.color = color;
        this.price = price;
        this.quantity = quantity;
        this.productImage = productImage;
        this.outOfStockItems = outOfStockItems;
        this.message = message;
    }

    // Parcelable constructor
    protected CardItemDTO(Parcel in) {
        cartItemId = in.readInt();
        productName = in.readString();
        color = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        productImage = in.readString();
        isSelected = in.readByte() != 0;
        outOfStockItems = in.readByte() != 0;
        message = in.readString(); // Read the message field from the parcel
    }

    public static final Parcelable.Creator<CardItemDTO> CREATOR = new Parcelable.Creator<CardItemDTO>() {
        @Override
        public CardItemDTO createFromParcel(Parcel in) {
            return new CardItemDTO(in);
        }

        @Override
        public CardItemDTO[] newArray(int size) {
            return new CardItemDTO[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isOutOfStockItems() {
        return outOfStockItems;
    }

    public void setOutOfStockItems(boolean outOfStockItems) {
        this.outOfStockItems = outOfStockItems;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cartItemId);           // Ensure writing the cartItemId
        dest.writeString(productName);       // Write the productName
        dest.writeString(color);             // Write the color
        dest.writeDouble(price);             // Write the price
        dest.writeInt(quantity);            // Write the quantity
        dest.writeString(productImage);     // Write the productImage
        dest.writeByte((byte) (isSelected ? 1 : 0)); // Write isSelected
        dest.writeByte((byte) (outOfStockItems ? 1 : 0)); // Write outOfStockItems
        dest.writeString(message);          // Write the message field correctly
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
