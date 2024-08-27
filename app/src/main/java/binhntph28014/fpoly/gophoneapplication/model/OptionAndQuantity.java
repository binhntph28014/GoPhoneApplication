package binhntph28014.fpoly.gophoneapplication.model;

import com.google.gson.annotations.SerializedName;

public class OptionAndQuantity {
    @SerializedName("_id")
    private String id;
    @SerializedName("option_id")
    private OptionOfListCart optionProduct;
    private int quantity;
    private int discount_value;

    public OptionAndQuantity(String id, OptionOfListCart optionProduct, int quantity, int discount_value) {
        this.id = id;
        this.optionProduct = optionProduct;
        this.quantity = quantity;
        this.discount_value = discount_value;
    }

    public int getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(int discount_value) {
        this.discount_value = discount_value;
    }

    public OptionAndQuantity() {
    }

    public OptionAndQuantity(String id, OptionOfListCart optionProduct, int quantity) {
        this.id = id;
        this.optionProduct = optionProduct;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartOfList{" +
                "id='" + id + '\'' +
                ", optionProduct=" + optionProduct +
                ", quantity=" + quantity +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OptionOfListCart getOptionProduct() {
        return optionProduct;
    }

    public void setOptionProduct(OptionOfListCart optionProduct) {
        this.optionProduct = optionProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
