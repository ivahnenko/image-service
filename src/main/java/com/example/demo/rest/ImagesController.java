package com.example.demo.rest;

import com.example.demo.error.NotFoundException;
import com.example.demo.service.AgileEngineImagesService;
import com.example.demo.model.ImageModel;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/images")
public class ImagesController {

  private final AgileEngineImagesService imagesService;

  public ImagesController(@Autowired final AgileEngineImagesService imagesService) {
    this.imagesService = imagesService;
  }

  @RequestMapping("/{id}")
  public ImageModel getImageById(@PathVariable("id") final String id) {
    final ImageModel result =  imagesService.getImageById(id);
    if (Objects.isNull(result)) {
      throw new NotFoundException("Not found");
    }
    return result;
  }

}
