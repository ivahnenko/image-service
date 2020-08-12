package com.example.demo.config;

import com.example.demo.service.AgileEngineImagesService;
import com.example.demo.client.AgileEngineImagesRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ImageServiceConfiguration {

  @Value("${client.imagesUrl}")
  private String imagesUrl;
  @Value("${client.authUrl}")
  private String authUrl;
  @Value("${client.apikey}")
  private String apiKey;

  @Bean
  public RetryTemplate retryTemplate() {
    final RetryTemplate retryTemplate = new RetryTemplate();

    final FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
    fixedBackOffPolicy.setBackOffPeriod(5l);

    final SimpleRetryPolicy basicRetryPolicy = new SimpleRetryPolicy();
    basicRetryPolicy.setMaxAttempts(3);

    retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
    retryTemplate.setRetryPolicy(basicRetryPolicy);

    return retryTemplate;
  }

  @Bean
  public AgileEngineImagesRestClient restTemplateClient() {
    return new AgileEngineImagesRestClient(agileEngineClientConfigurationProps(), restTemplate(), retryTemplate());
  }

  @Bean
  public AgileEngineClientConfigurationProps agileEngineClientConfigurationProps() {
    final AgileEngineClientConfigurationProps properties = new AgileEngineClientConfigurationProps();
    properties.setApiKey(apiKey);
    properties.setImagesUrl(imagesUrl);
    properties.setAuthUrl(authUrl);

    return properties;
  }
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public AgileEngineImagesService agileEngineImagesClient() {
    return new AgileEngineImagesService();
  }

}
