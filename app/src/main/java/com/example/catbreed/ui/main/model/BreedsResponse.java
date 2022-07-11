package com.example.catbreed.ui.main.model;

import java.util.List;

public class BreedsResponse {
    private List<BreedsDatum> list;

    public void setList(List<BreedsDatum> list) {
        this.list = list;
    }

    public List<BreedsDatum> getList() {
        return list;
    }
}
