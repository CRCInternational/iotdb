/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.iotdb.itbase.runtime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ParallelRequestDelegate will handle requests in serial. It's more efficient when the requests are
 * just local computation.
 */
public class SerialRequestDelegate<T> extends RequestDelegate<T> {

  public SerialRequestDelegate(List<String> endpoints) {
    super(endpoints);
  }

  @Override
  public List<T> requestAll() throws SQLException {
    List<T> results = new ArrayList<>(getEndpoints().size());
    for (int i = 0; i < getEndpoints().size(); i++) {
      try {
        results.add(getRequests().get(i).call());
      } catch (Exception e) {
        throw new SQLException(
            String.format("Request %s error: %s", getEndpoints().get(i), e.getMessage()), e);
      }
    }
    return results;
  }
}
