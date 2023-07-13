package com.example.gallery.service.dto;

import java.io.Serializable;

/**
 * DTO for {@ling Image}.
 */
public class ImageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;
    private String url;
    private String description;

    public ImageDTO(String key, String url, String description) {
        this.key = key;
        this.url = url;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ImageDTO [key=" + key + ", url=" + url + ", description=" + description + "]";
    }

}
