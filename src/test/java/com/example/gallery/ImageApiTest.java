package com.example.gallery;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.example.gallery.domain.Image;
import com.example.gallery.serverless.domain.ImageServerless;
import com.example.gallery.serverless.repository.ImageServerlessRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ImageGalleryApplication.class)
@WebAppConfiguration
@Import(RepositoryTestConfiguration.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
public class ImageApiTest {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private ImageServerlessRepository imageServerlessRepository;

    @Before
    public void setup() {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(ImageServerless.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);
        dynamoDBMapper.batchDelete(imageServerlessRepository.findAll());
    }

    @Test
    public void shouldListAllImages_whenFindAll_givenNothing() {
        // given
        ImageServerless image = new ImageServerless(null, "Name", "https://www.recordnet.com/gcdn/presto/2021/03/22/NRCD/9d9dd9e4-e84a-402e-ba8f-daa659e6e6c5-PhotoWord_003.JPG", "Description");
        imageServerlessRepository.save(image);

        ImageServerless expectedImage = new ImageServerless("1", "Name", "https://www.recordnet.com/gcdn/presto/2021/03/22/NRCD/9d9dd9e4-e84a-402e-ba8f-daa659e6e6c5-PhotoWord_003.JPG", "Description");

        // when
        Iterable<ImageServerless> actual = imageServerlessRepository.findAll();

        // then
        assertThat(actual).hasSize(1).first().usingRecursiveComparison().isEqualTo(expectedImage);
    }

}
