package com.example.demo.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public final class JsonUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper()
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
      .setSerializationInclusion(Include.NON_NULL);

  static {
    MAPPER.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
  }

  public static JsonNode parseJson(final String data) {
    try {
      return MAPPER.readTree(data);
    } catch (final IOException ex) {
      throw new RuntimeException(
          String.format("while converting '%s' to JsonNode type", data), ex);
    }
  }

  public static <T> T parseJson(final String data, final Class<T> classType) {
    try {
      return MAPPER.readerWithView(classType).forType(classType).readValue(data);
    } catch (final IOException ex) {
      throw new RuntimeException(
          String.format("while converting '%s' to '%s' type", data, classType), ex);
    }
  }

  public static <T> T parseJson(final String data, final TypeReference<T> typeReference) {
    try {
      return MAPPER.readValue(data, typeReference);
    } catch (final IOException ex) {
      throw new RuntimeException(
          String.format("while converting '%s' to '%s' type", data,
              typeReference.getType().getTypeName()), ex);
    }
  }

  public static String toJsonString(final Object data) {
    try {
      return MAPPER.writeValueAsString(data);
    } catch (final IOException ex) {
      throw new RuntimeException(
          String.format("while converting '%s' to JSON String", data), ex);
    }
  }

}
