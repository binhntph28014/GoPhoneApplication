package binhntph28014.fpoly.gophoneapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comment {
    @SerializedName("_id")
    private String id;
    @SerializedName("product_id")
    private Product product;
    @SerializedName("user_id")
    private User user;
    @SerializedName("order_id")

    private String order;
    private String name;

    public Comment(String id, Product product, User user, String order, String name, String content, List<String> image, int rate, String createdAt, String updatedAt) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.order = order;
        this.name = name;
        this.content = content;
        this.image = image;
        this.rate = rate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    private String content;
    private List<String> image;
    private int rate;
    private String createdAt;
    private String updatedAt;

    public String getOrder() {
        return order;
    }

    public Comment(String id, Product product, User user, String order, String content, List<String> image, int rate, String createdAt, String updatedAt) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.order = order;
        this.content = content;
        this.image = image;
        this.rate = rate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Comment() {
    }

    public Comment(User user, String content, int rate, String updatedAt) {
        this.user = user;
        this.content = content;
        this.rate = rate;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Comment(Product product) {
        this.product = product;
    }

    public Comment(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
