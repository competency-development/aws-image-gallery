package com.example.gallery;

import com.example.gallery.domain.Image;
import com.example.gallery.repository.ImagesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImageGalleryApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
public class ImageGalleryApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ImagesRepository repository;

    @Before
    public void setUp() {
        repository.createTableIfNotExists();
    }

    @After
    public void cleanUp() {
        repository.deleteTableIfExists();
    }

    @Test
    public void shouldReturnAllImages_whenImages_givenImagesRequest() throws Exception {
        // given
        RequestBuilder givenRequest = get("/images").contentType(MediaType.APPLICATION_JSON);

        Image expectedImage = new Image("Name",
                "https://www.recordnet.com/gcdn/presto/2021/03/22/NRCD/9d9dd9e4-e84a-402e-ba8f-daa659e6e6c5-PhotoWord_003.JPG",
                "Description");
        String expectedBody = getExpectedImageJson(expectedImage);

        // when
        MockHttpServletResponse actualResult = mvc.perform(givenRequest).andReturn().getResponse();

        // then
        assertThat(actualResult.getStatus()).isEqualTo(200);
        assertThat(actualResult.getContentAsString()).isEqualTo(expectedBody);
    }

    private static String getExpectedImageJson(Image image) throws Exception {
        return String.format("[%s]", new ObjectMapper().writer().writeValueAsString(image));
    }

}
