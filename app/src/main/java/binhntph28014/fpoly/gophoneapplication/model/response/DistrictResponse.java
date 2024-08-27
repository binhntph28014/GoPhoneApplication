package binhntph28014.fpoly.gophoneapplication.model.response;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.model.District;


public class DistrictResponse {
    private List<District> results;

    public DistrictResponse() {
    }

    @Override
    public String toString() {
        return "DistrictResponse{" +
                "results=" + results +
                '}';
    }

    public List<District> getResults() {
        return results;
    }

    public void setResults(List<District> results) {
        this.results = results;
    }
}
