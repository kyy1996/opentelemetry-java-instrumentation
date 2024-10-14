/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.spring.autoconfigure.internal.instrumentation.elasticsearch.rest;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.elasticsearch.rest.v7_0.ElasticsearchRest7Telemetry;
import java.util.Collections;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/** Internal use for replacing the RestClient with an enhanced one in RestHighLevelClient. */
class ElasticsearchRestInstrumentUtils {
  static RestHighLevelClient wrap(RestHighLevelClient originalClient, OpenTelemetry openTelemetry) {
    return new WrappedRestHighLevelClient(
        ElasticsearchRest7Telemetry.create(openTelemetry).wrap(originalClient.getLowLevelClient()));
  }

  private static class WrappedRestHighLevelClient extends RestHighLevelClient {
    protected WrappedRestHighLevelClient(RestClient restClient) {
      super(restClient, RestClient::close, Collections.emptyList());
    }
  }
}
