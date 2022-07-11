package com.example.catbreed.ui.main;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

public class CatBridViewModel extends ViewModel {
    private String name;
    private String id;
    private String imageUrl;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @BindingAdapter("app:imageUrl")
    public static void setImageUrl(ImageView imageView,String url){
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}
