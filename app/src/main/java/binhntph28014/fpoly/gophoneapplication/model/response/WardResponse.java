package binhntph28014.fpoly.gophoneapplication.model.response;


import java.util.List;

import binhntph28014.fpoly.gophoneapplication.model.Ward;


public class WardResponse {
    private List<Ward> results;


    public WardResponse() {
    }

    @Override
    public String toString() {
        return "WardResponse{" +
                "results=" + results +
                '}';
    }

    public List<Ward> getResults() {
        return results;
    }

    public void setResults(List<Ward> results) {
        this.results = results;
    }
}
