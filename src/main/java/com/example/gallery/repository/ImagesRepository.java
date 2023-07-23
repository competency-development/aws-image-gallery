package com.example.gallery.repository;

import com.example.gallery.domain.Image;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;
import software.amazon.awssdk.utils.ImmutableMap;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Repository
public class ImagesRepository {

    private static final String DB_TABLE_NAME = "Image";
    private static final Long DB_RCU = 1L;
    private static final Long DB_WCU = 1L;

    private final DynamoDbTable<Image> table;

    @Autowired
    public ImagesRepository(DynamoDbEnhancedClient dynamoDb) {
        this.table = dynamoDb.table(DB_TABLE_NAME, TableSchema.fromBean(Image.class));
    }

    /**
     * Stores {@link Image} into DynamoDB
     *
     * @param image contains entity of {@link Image} to be stored
     */
    public void save(Image image) {
        table.putItem(image);
    }

    /**
     * Deletes all {@link Image} by sort key
     *
     * @param key contains an partition key value of {@link Image} to be searched by
     */
    public void delete(String key) {
        table.deleteItem(DeleteItemEnhancedRequest.builder().key(Key.builder().partitionValue(key).build()).build());
    }

    /**
     * Scans for all {@link Image} entries
     */
    public PageIterable<Image> findAll() {
        return table.scan();
    }

    /**
     * Selects {@link Image} by partition key
     *
     * @param key contains an partition key value of {@link Image} to be searched by
     */
    public Optional<Image> find(String key) {
        return Optional.of(table.getItem(Key.builder().partitionValue(key).build()));
    }

    /**
     * Gets {@link Image} collection by partition key and url
     *
     * @param key contains an partition key value of {@link Image} to be searched by
     * @param url contains an url value of {@link Image} to be searched by
     */
    public PageIterable<Image> findByUrl(String key, String url) {
        AttributeValue attributeValue = AttributeValue.builder().n(String.valueOf(url)).build();

        Map<String, AttributeValue> expressionValues = ImmutableMap.<String, AttributeValue>builder()
                .put(":value", attributeValue)
                .build();
        Expression expression = Expression.builder()
                .expression("url = :value")
                .expressionValues(expressionValues)
                .build();

        // create a QueryConditional object that is used in the query operation
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(key).build());

        // get items in Image table and write out the id value
        return table.query(exp -> exp.queryConditional(queryConditional).filterExpression(expression));
    }

    public void createTableIfNotExists() {
        try {
            table.createTable(builder -> builder
                    .provisionedThroughput(b -> b
                            .readCapacityUnits(DB_RCU)
                            .writeCapacityUnits(DB_WCU)
                            .build())
            );
        } catch (ResourceInUseException e) {
            log.info(DB_TABLE_NAME + " already exists.");
        }

        // wait until table is created
        try (DynamoDbWaiter waiter = DynamoDbWaiter.create()) {
            ResponseOrException<DescribeTableResponse> response = waiter
                    .waitUntilTableExists(builder -> builder.tableName(DB_TABLE_NAME).build())
                    .matched();
            response.response().orElseThrow(() -> new RuntimeException(DB_TABLE_NAME + " table was not created."));
        }
    }

    public void deleteTableIfExists() {
        try {
            table.deleteTable();
        } catch (ResourceNotFoundException e) {
            log.info(DB_TABLE_NAME + " already exists.");
        }
    }

}
