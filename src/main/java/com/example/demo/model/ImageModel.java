package com.example.demo.model;

import com.example.demo.utils.TagsStringToTagsArrayDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class ImageModel implements Serializable {

  private String id;
  private String author;
  private String camera;
  @JsonDeserialize(using = TagsStringToTagsArrayDeserializer.class)
  private List<String> tags;
  @JsonProperty("cropped_picture")
  private String croppedPicture;
  @JsonProperty("full_picture")
  private String fullPicture;

}