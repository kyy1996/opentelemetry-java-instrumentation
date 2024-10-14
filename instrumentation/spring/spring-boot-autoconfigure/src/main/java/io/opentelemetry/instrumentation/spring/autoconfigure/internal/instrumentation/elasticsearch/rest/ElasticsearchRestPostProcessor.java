/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.spring.autoconfigure.internal.instrumentation.elasticsearch.rest;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.opentelemetry.api.OpenTelemetry;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

final class ElasticsearchRestPostProcessor implements BeanPostProcessor, Ordered {
  private final ObjectProvider<OpenTelemetry> openTelemetryProvider;

  ElasticsearchRestPostProcessor(ObjectProvider<OpenTelemetry> openTelemetryProvider) {
    this.openTelemetryProvider = openTelemetryProvider;
  }

  @CanIgnoreReturnValue
  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    // Exclude scoped proxy beans to avoid double wrapping
    if (bean instanceof RestHighLevelClient && !ScopedProxyUtils.isScopedTarget(beanName)) {
      RestHighLevelClient dataSource = (RestHighLevelClient) bean;
      return ElasticsearchRestInstrumentUtils.wrap(dataSource, openTelemetryProvider.getObject());
    }
    return bean;
  }

  // To be one of the first bean post-processors to be executed
  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE - 20;
  }
}
