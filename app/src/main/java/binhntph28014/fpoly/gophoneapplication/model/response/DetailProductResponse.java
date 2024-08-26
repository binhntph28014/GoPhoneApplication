package binhntph28014.fpoly.gophoneapplication.model.response;


import binhntph28014.fpoly.gophoneapplication.model.ProductDetail;

public class DetailProductResponse {
    String message;
    int code;
    ProductDetail result;

    public DetailProductResponse() {
    }

    public DetailProductResponse(String message, int code, ProductDetail result) {
        this.message = message;
        this.code = code;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ProductDetail getResult() {
        return result;
    }

    public void setResult(ProductDetail result) {
        this.result = result;
    }
}
