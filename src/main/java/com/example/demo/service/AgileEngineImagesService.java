package com.example.demo.service;

import com.example.demo.client.AgileEngineImagesRestClient;
import com.example.demo.model.ImageModel;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AgileEngineImagesService {

  private static final Logger LOG = LoggerFactory.getLogger(AgileEngineImagesService.class);

  @Autowired
  private AgileEngineImagesRestClient agileEngineImagesRestClient;

  @Autowired
  private CacheService cacheService;

  public void populateLocalCache() {
    LOG.info("Populating local cache");

    final String token = agileEngineImagesRestClient.getAuthToken();

    final List<ImageModel> images = agileEngineImagesRestClient.getAllImages(token);
    images.forEach(image ->  {
     final ImageModel populatedImage =  agileEngineImagesRestClient.getImageById(image.getId(), token);
     cacheService.put(populatedImage, populatedImage.getId());
    });
  }

  public ImageModel getImageById(final String id) {
    ImageModel result = cacheService.get(id, ImageModel.class);
    if (result == null) {
      final String token = agileEngineImagesRestClient.getAuthToken();
      result = agileEngineImagesRestClient.getImageById(id, token);
    }

    return result;
  }

}
