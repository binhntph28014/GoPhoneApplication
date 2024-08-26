package binhntph28014.fpoly.gophoneapplication.model.response;

public class YeuthichRequestBody {
    private String _id;
    private String user_id;
    private String product_id;
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public YeuthichRequestBody(String _id, String user_id, String product_id) {
        this._id = _id;
        this.user_id = user_id;
        this.product_id = product_id;
    }
    public String getUser_id() {
        return user_id;
    }

    public YeuthichRequestBody(String user_id, String product_id) {
        this.user_id = user_id;
        this.product_id = product_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}