/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.spring.autoconfigure.internal.instrumentation.elasticsearch.rest;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.spring.autoconfigure.internal.ConditionalOnEnabledInstrumentation;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is internal and is hence not for public use. Its APIs are unstable and can change at
 * any time.
 */
@ConditionalOnEnabledInstrumentation(module = "elasticsearch")
@AutoConfiguration(after = ElasticsearchRestClientAutoConfiguration.class)
@ConditionalOnBean({RestHighLevelClient.class, RestClient.class})
@Configuration(proxyBeanMethods = false)
public class ElasticsearchRestInstrumentationAutoConfiguration {
  @Bean
  ElasticsearchRestPostProcessor elasticsearchRestPostProcessor(
      ObjectProvider<OpenTelemetry> openTelemetryProvider) {
    return new ElasticsearchRestPostProcessor(openTelemetryProvider);
  }
}
