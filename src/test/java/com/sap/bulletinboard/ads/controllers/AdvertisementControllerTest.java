package com.sap.bulletinboard.ads.controllers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.bulletinboard.ads.config.WebAppContextConfig;
import com.sap.bulletinboard.ads.models.Advertisement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppContextConfig.class })
@WebAppConfiguration
//@formatter:off
public class AdvertisementControllerTest {
    
    private static final String LOCATION = "Location";
    private static final String SOME_TITLE = "MyNewAdvertisement";
    private static final String SOME_NEW_TITLE = "MyNEwUpdatedate";

    @Inject
    WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void create() throws Exception {
        mockMvc.perform(buildPostRequest(SOME_TITLE))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, is(not(""))))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.title", is(SOME_TITLE))); // requires com.jayway.jsonpath:json-path
    }

    @Test
    public void readAll() throws Exception {
        mockMvc.perform(buildPostRequest(SOME_TITLE))
        .andExpect(status().isCreated());
        // TODO create new advertisement using POST, then retrieve all advertisements using GET
        mockMvc.perform(get(AdvertisementController.PATH).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.value.length()",is(both(greaterThan(0)).and(lessThan(10)))));
    }


    @Test
    public void readByIdNotFound() throws Exception {
        // TODO try to retrieve object with nonexisting ID using GET request to /4711
        mockMvc.perform(buildGetRequest("123"))
                .andExpect(status().isNotFound());
        
    }

    @Test
    public void readById() throws Exception {
        String id = performPostAndGetId();
        
        mockMvc.perform(buildGetRequest(id))
        .andExpect(jsonPath("$.length()",is(1)))
        .andExpect(jsonPath("$.title",is(SOME_TITLE)));
        // TODO create new advertisement using POST, then retrieve it using GET {/id}
    }
    @Test
    public void updateById() throws Exception {
        //post
        String id = performPostAndGetId();
        //put
        mockMvc.perform(buildPutRequest(id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title",is(SOME_NEW_TITLE)));
       
       
    }
    private String performPostAndGetId() throws Exception {
        MockHttpServletResponse rep = mockMvc.perform(buildPostRequest(SOME_TITLE))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        return getIdFromLocation(rep.getHeader(LOCATION));
    }
    private MockHttpServletRequestBuilder buildGetRequest(String id) throws Exception {
        String pathbyId=AdvertisementController.PATH+"/"+id;
        
        return get(pathbyId).contentType(APPLICATION_JSON_UTF8);
    }
    
    private MockHttpServletRequestBuilder buildPostRequest(String adsTitle) throws Exception {
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(adsTitle);

        // post the advertisement as a JSON entity in the request body
        return post(AdvertisementController.PATH).content(toJson(advertisement)).contentType(APPLICATION_JSON_UTF8);
    }
    private MockHttpServletRequestBuilder buildPutRequest(String id) throws JsonProcessingException {
        Advertisement ad = new Advertisement();
        ad.setTitle(SOME_NEW_TITLE);
        return put(AdvertisementController.PATH+"/" + id).contentType(APPLICATION_JSON_UTF8).content(toJson(ad));
    }
    private String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private String getIdFromLocation(String location) {
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private <T> T convertJsonContent(MockHttpServletResponse response, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String contentString = response.getContentAsString();
        return objectMapper.readValue(contentString, clazz);
    }
}