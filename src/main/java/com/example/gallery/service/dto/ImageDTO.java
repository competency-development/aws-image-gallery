package com.example.gallery.service.dto;

import com.example.gallery.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO for {@link Image}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String key;
    private String url;
    private String description;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("key", key)
                .append("url", url)
                .append("description", description)
                .toString();
    }

}
