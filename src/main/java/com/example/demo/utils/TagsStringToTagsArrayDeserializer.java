package com.example.demo.utils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagsStringToTagsArrayDeserializer extends JsonDeserializer<List<String>> {

  @Override
  public List<String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    String value = jsonParser.readValueAs(String.class);
    return Arrays.stream(value.split(" "))
        .map(tag -> tag.replace("#", ""))
        .collect(Collectors.toList());
  }
}