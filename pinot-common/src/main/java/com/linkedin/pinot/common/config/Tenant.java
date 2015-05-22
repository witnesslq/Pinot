/**
 * Copyright (C) 2014-2015 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.common.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.linkedin.pinot.common.utils.TenantRole;


public class Tenant {

  private static final Logger LOGGER = LoggerFactory.getLogger(Tenant.class);

  private String role;
  private String name;
  private int numberOfInstances;
  private int offlineInstances;
  private int realtimeInstances;

  public void setRole(String role) {
    this.role = role;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNumberOfInstances(int numberOfInstances) {
    this.numberOfInstances = numberOfInstances;
  }

  public void setOfflineInstances(int offlineInstances) {
    this.offlineInstances = offlineInstances;
  }

  public void setRealtimeInstances(int realtimeInstances) {
    this.realtimeInstances = realtimeInstances;
  }

  public TenantRole getTenantRole() {
    return TenantRole.valueOf(role.toUpperCase());
  }

  public String getTenantName() {
    return name;
  }

  public int getNumberOfInstances() {
    return numberOfInstances;
  }

  public int getOfflineInstances() {
    return offlineInstances;
  }

  public int getRealtimeInstances() {
    return realtimeInstances;
  }

  public boolean isColoated() {
    return (realtimeInstances + offlineInstances > numberOfInstances);
  }

  /**
   *  returns true if all properties are the same
   */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Tenant)) {
      return false;
    }

    final Tenant toCompare = (Tenant) object;

    if ((toCompare.getTenantRole().equals(getTenantName())) && (toCompare.getTenantName().equals(getTenantName()))
        && (toCompare.getNumberOfInstances() == getNumberOfInstances())) {
      if (getTenantRole() == TenantRole.SERVER) {
        if (toCompare.getOfflineInstances() == getOfflineInstances()
            && toCompare.getRealtimeInstances() == getRealtimeInstances()) {
          return true;
        } else {
          return false;
        }
      } else {
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (getTenantRole() == TenantRole.SERVER) {
      return Objects.hashCode(getTenantRole(), getTenantName(), getNumberOfInstances(), getOfflineInstances(),
          getRealtimeInstances());
    } else {
      return Objects.hashCode(getTenantRole(), getTenantName(), getNumberOfInstances());
    }
  }

  @Override
  public String toString() {
    String ret = null;
    try {
      ret = new ObjectMapper().writeValueAsString(this);
    } catch (Exception e) {
      LOGGER.error("error toString for tenant ", e);
    }
    return ret;
  }

  public JSONObject toJSON() throws JSONException {
    return new JSONObject(toString());
  }

  public static class TenantBuilder {
    Tenant tenant;

    public TenantBuilder(String name) {
      tenant = new Tenant();
      tenant.setName(name);
    }

    public TenantBuilder setType(TenantRole role) {
      tenant.setRole(role.toString());
      return this;
    }

    public TenantBuilder setTotalInstances(int totalInstances) {
      tenant.setNumberOfInstances(totalInstances);
      return this;
    }

    public TenantBuilder setOfflineInstances(int totalInstances) {
      tenant.setOfflineInstances(totalInstances);
      return this;
    }

    public TenantBuilder setRealtimeInstances(int totalInstances) {
      tenant.setRealtimeInstances(totalInstances);
      return this;
    }

    public Tenant build() {
      return tenant;
    }
  }
}
