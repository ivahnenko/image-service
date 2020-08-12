package com.example.demo.client;

import com.example.demo.utils.JsonUtils;
import com.example.demo.error.AuthorizationFailedException;
import com.example.demo.model.ImageModel;
import com.example.demo.config.AgileEngineClientConfigurationProps;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

public class AgileEngineImagesRestClient {

  private static final Logger LOG = LoggerFactory.getLogger(AgileEngineImagesRestClient.class);

  private static final String PICTURES_PATH = "pictures";
  private static final String HAS_MORE_PAGES_PATH = "hasMore";
  private static final String TOKEN_PATH = "token";

  private final AgileEngineClientConfigurationProps properties;

  private final RestTemplate restTemplate;
  private final RetryTemplate retryTemplate;

  public AgileEngineImagesRestClient(final AgileEngineClientConfigurationProps properties,
      final RestTemplate restTemplate,
      final RetryTemplate retryTemplate) {
    this.properties = properties;
    this.restTemplate = restTemplate;
    this.retryTemplate = retryTemplate;
  }

  public String getAuthToken() {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    final Auth auth = new Auth();
    auth.setApiKey(properties.getApiKey());

    final HttpEntity<String> request = new HttpEntity<>(JsonUtils.toJsonString(auth), headers);

    final JsonNode jsonResponse = retryTemplate.execute((context) ->
            executeInternal(properties.getAuthUrl(), HttpMethod.POST, request,
                new ParameterizedTypeReference<JsonNode>() {
                })
        ).getBody();

    return jsonResponse.get(TOKEN_PATH).asText();
  }

  public List<ImageModel> getAllImages() {
   return getAllImages(getAuthToken());
  }

  public List<ImageModel> getAllImages(final String token) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(token);

    final HttpEntity<String> request = new HttpEntity<>(headers);

    final List<ImageModel> results = new ArrayList<>();

    JsonNode jsonResponse;
    int pageNumber = 1;
    do {
      final String url = getNextPageUrl(pageNumber++);
      jsonResponse = retryTemplate.execute((context) ->
          executeInternal(url, HttpMethod.GET, request, new ParameterizedTypeReference<JsonNode>() {
          })).getBody();

      results.addAll(JsonUtils.parseJson(jsonResponse.get(PICTURES_PATH).toString(),
          new TypeReference<List<ImageModel>>() {
          }));
    } while (jsonResponse.get(HAS_MORE_PAGES_PATH).asBoolean());

    return results;
  }

  private String getNextPageUrl(int pageNumber) {
    return properties.getImagesUrl() + "?page=" + pageNumber + 1;
  }

  public ImageModel getImageById(final String id) {
    return getImageById(id, getAuthToken());
  }

  public ImageModel getImageById(final String id, final String token) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(token);

    final HttpEntity<String> request = new HttpEntity<>(headers);

    return retryTemplate.execute((context) -> executeInternal(properties.getImagesUrl() +
            "/" + id,
        HttpMethod.GET, request, new ParameterizedTypeReference<ImageModel>() {
        })).getBody();
  }

  private <T> ResponseEntity<T> executeInternal(final String url,
      final HttpMethod method, final HttpEntity entity, final ParameterizedTypeReference<T> type) {
    ResponseEntity<T> response;
    try {
      response = restTemplate.exchange(url, method, entity, type);
    } catch (final Exception ex) {
      LOG.error("while executing request [url='{}', method='{}'", url, method, ex);
      throw ex;
    }

    if (response.getStatusCode() == HttpStatus.OK) {
      return response;
    }

    if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
      throw new AuthorizationFailedException("auth token is invalid");
    }

    throw new RuntimeException("Failed to execute request");
  }

}
