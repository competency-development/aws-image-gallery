package com.example.gallery.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    private String key;

    private String url;

    private String description;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("key")
    public String getKey() {
        return key;
    }

    @DynamoDbAttribute("url")
    public String getUrl() {
        return url;
    }

    @DynamoDbAttribute("description")
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("key", key)
                .append("url", url)
                .append("description", description)
                .toString();
    }

}
