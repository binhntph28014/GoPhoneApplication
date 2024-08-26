package binhntph28014.fpoly.gophoneapplication.model.response;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.model.Info;

public class InfoResponse {
    private int code;
    private String message;
    private List<Info> result;

    public InfoResponse() {
    }

    public InfoResponse(int code, String message, List<Info> result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    @Override
    public String toString() {
        return "InfoResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Info> getResult() {
        return result;
    }

    public void setResult(List<Info> result) {
        this.result = result;
    }
}
