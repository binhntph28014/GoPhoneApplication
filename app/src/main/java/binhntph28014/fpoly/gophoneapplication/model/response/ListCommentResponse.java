package binhntph28014.fpoly.gophoneapplication.model.response;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.model.Comment;

public class ListCommentResponse {
    private int code;
    private List<Comment> data;
    private String message;

    public ListCommentResponse() {
    }

    public ListCommentResponse(int code, List<Comment> data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
