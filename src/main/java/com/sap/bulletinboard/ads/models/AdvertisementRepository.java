package com.sap.bulletinboard.ads.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
//extends CrudRepository<Advertisement, Long>
public interface AdvertisementRepository extends PagingAndSortingRepository<Advertisement, Long>{

}
