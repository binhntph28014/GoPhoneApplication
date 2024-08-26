package binhntph28014.fpoly.gophoneapplication.model.body;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.model.OptionAndQuantity;

public class PurchaseBody {
    @SerializedName("_id")
    private String id;
    @SerializedName("user_id")
    private String userId;
    private List<OptionAndQuantity> productsOrder;
    @SerializedName("total_price")
    private int totalPrice;
    @SerializedName("info_id")
    private String infoId;
    //    @SerializedName("payment_status")
    private boolean payment_status = false;

    public PurchaseBody() {
    }

    public PurchaseBody(String id, String userId, List<OptionAndQuantity> productsOrder, int totalPrice, String infoId, boolean payment_status) {
        this.id = id;
        this.userId = userId;
        this.productsOrder = productsOrder;
        this.totalPrice = totalPrice;
        this.infoId = infoId;
        this.payment_status = payment_status;
    }

    @Override
    public String toString() {
        return "PurchaseBody{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", productsOrder=" + productsOrder +
                ", totalPrice=" + totalPrice +
                ", infoId='" + infoId + '\'' +
                ", payment_status=" + payment_status +
                '}';
    }

    public List<OptionAndQuantity> getProductsOrder() {
        return productsOrder;
    }

    public void setProductsOrder(List<OptionAndQuantity> productsOrder) {
        this.productsOrder = productsOrder;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPayment_status() {
        return payment_status;
    }

    public void setPayment_status(boolean payment_status) {
        this.payment_status = payment_status;
    }
}