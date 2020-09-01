package com.mardous.newsletter.api.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mardous.newsletter.api.request.RequestBase;

import java.util.ArrayList;
import java.util.List;

public class News implements RequestBase {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("totalResults")
    @Expose
    private int totalResults;

    @SerializedName("articles")
    @Expose
    private List<Article> article;

    @NonNull
    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Nullable
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    @NonNull
    public List<Article> getArticle() {
        if (article == null) {
            article = new ArrayList<>();
        }
        return article;
    }

    public void setArticle(List<Article> article) {
        this.article = article;
    }
}
