package com.sap.bulletinboard.ads.controllers;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus; //enumeration for HTTP status codes
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.bulletinboard.ads.models.Advertisement;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
@RequestMapping(value=AdvertisementController.PATH) //TODO: specify path and optionally contentType 
@Validated
public class AdvertisementController {
    public static final String PATH = "/api/v1/ads";
    private static final Map<Long, Advertisement> ads = new HashMap<>(); //temporary data storage, key represents the ID
    
    private static int ID = 0;
   
    @GetMapping
    public AdvertisementList advertisements() {
        return new AdvertisementList(ads.values()); //TODO
    }

    @GetMapping("/{id}")
    public Advertisement advertisementById(@PathVariable("id") @Min(0) Long id) {
        if(!ads.containsKey(id)) {
            throw new NotFoundException("not found id");
        }
        
        return ads.get(id); //TODO
    }

    /**
     * @throws URISyntaxException 
     * @RequestBody is bound to the method argument. HttpMessageConverter resolves method argument depending on the
     *              content type.
     */
    @PostMapping
    public ResponseEntity<Advertisement> add(@Valid @RequestBody Advertisement advertisement,
            UriComponentsBuilder uriComponentsBuilder) throws URISyntaxException {
        long id=ID++;
        ads.put(id, advertisement);
        UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{id}").buildAndExpand(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(uriComponents.getPath()));
                
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(advertisement); //TODO return ResponseEntity with advertisement in the body, location header and HttpStatus.CREATED status code
    }
    @PutMapping("/{id}")
    public Advertisement update(@RequestBody Advertisement advertisement,
            @PathVariable("id") Long id) {
        throwIfNoneexisting(id);
        ads.put(id, advertisement);
        return advertisement; 
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        ads.clear();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id){
        if(!ads.containsKey(id)) {
            throw new NotFoundException("not found id");
        }
        ads.remove(id);
        //return ResponseEntity.noContent().build();
    }
    private void throwIfNoneexisting(Long id) {
        if(!ads.containsKey(id)) {
            throw new NotFoundException(id+"not found");
        }
    }
    public static class AdvertisementList {
        @JsonProperty("value")
        public List<Advertisement> advertisements = new ArrayList<>();

        public AdvertisementList(Iterable<Advertisement> ads) {
            ads.forEach(advertisements::add);
        }
    }
  
}

