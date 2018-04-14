package com.sap.bulletinboard.ads.controllers;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.sap.bulletinboard.ads.models.AdvertisementRepository;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
@RequestMapping(value=AdvertisementController.PATH) //TODO: specify path and optionally contentType 
@Validated
public class AdvertisementController {
    public static final String PATH = "/api/v1/ads";
    //private static final Map<Long, Advertisement> ads = new HashMap<>(); //temporary data storage, key represents the ID
    public static final String PATH_PAGES = PATH + "/pages/";
    public static final int FIRST_PAGE_ID = 0;
    // allows server side optimization e.g. via caching
    public static final int DEFAULT_PAGE_SIZE = 20;
    
    
    private AdvertisementRepository adRepository;
   
    @Inject
    public AdvertisementController(AdvertisementRepository advertisementRepository) {
        // TODO Auto-generated constructor stub
        this.adRepository = advertisementRepository;
    }
    
    @GetMapping
    public ResponseEntity<AdvertisementList> advertisements() {
        return advertisementsForPage(FIRST_PAGE_ID);
        //return new AdvertisementList(ads.values()); //TODO
    }
    @GetMapping("/pages/{pageid}")
    public ResponseEntity<AdvertisementList> advertisementsForPage(@PathVariable("pageId") int pageId) {

        Page<Advertisement> page = adRepository.findAll(new PageRequest(pageId, DEFAULT_PAGE_SIZE));

        return new ResponseEntity<AdvertisementList>(new AdvertisementList(page.getContent()),
                buildLinkHeader(page, PATH_PAGES), HttpStatus.OK);
    } 
    @GetMapping("/{id}")
    public Advertisement advertisementById(@PathVariable("id") @Min(0) Long id) {
//        if(!ads.containsKey(id)) {
//            throw new NotFoundException("not found id");
//        }
        if(!adRepository.exists(id)) {
            throw new NotFoundException("not found id");
        }
        return adRepository.findOne(id); //TODO
    }

   
    /**
     * @throws URISyntaxException 
     * @RequestBody is bound to the method argument. HttpMessageConverter resolves method argument depending on the
     *              content type.
     */
    @PostMapping
    public ResponseEntity<Advertisement> add(@Valid @RequestBody Advertisement advertisement,
            UriComponentsBuilder uriComponentsBuilder) throws URISyntaxException {
//        long id=ID++;
//        ads.put(id, advertisement);
        Advertisement savedAdvertisement =  adRepository.save(advertisement);
        UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{id}").buildAndExpand(savedAdvertisement.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(uriComponents.getPath()));
                
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(savedAdvertisement); //TODO return ResponseEntity with advertisement in the body, location header and HttpStatus.CREATED status code
    }
    @PutMapping("/{id}")
    public Advertisement update(@RequestBody Advertisement advertisement,
            @PathVariable("id") Long id) {
        throwIfInconsistent(id,advertisement.getId());
        throwIfNoneexisting(id);
        return adRepository.save(advertisement);
         
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        adRepository.deleteAll();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id){
        throwIfNoneexisting(id);
        adRepository.delete(id);
    }
    public static HttpHeaders buildLinkHeader(Page<?> page, String path) {
        StringBuilder linkHeader = new StringBuilder();
        if (page.hasPrevious()) {
            int prevNumber = page.getNumber() - 1;
            linkHeader.append("<").append(path).append(prevNumber).append(">; rel=\"previous\"");
            if (!page.isLast())
                linkHeader.append(", ");
        }
        if (page.hasNext()) {
            int nextNumber = page.getNumber() + 1;
            linkHeader.append("<").append(path).append(nextNumber).append(">; rel=\"next\"");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LINK, linkHeader.toString());
        return headers;
    }
    private void throwIfInconsistent(Long expected, Long actual) {
        if (!expected.equals(actual)) {
            String message = String.format(
                    "bad request, inconsistent IDs between request and object: request id = %d, object id = %d",
                    expected, actual);
            throw new BadRequestException(message);
        }
    }
    
    private void throwIfNoneexisting(Long id) {
        if(!adRepository.exists(id)) {
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

