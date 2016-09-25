/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications for Halti usage made by Emblica Ltd.
 *
 */
package emblica.halti.domain.solver;

import java.io.Serializable;

import emblica.halti.domain.Service;

public class ServiceDependency implements Serializable {

    private Service fromService;
    private Service toService;

    public Service getFromService() {
        return fromService;
    }

    public void setFromService(Service fromService) {
        this.fromService = fromService;
    }

    public Service getToService() {
        return toService;
    }

    public void setToService(Service toService) {
        this.toService = toService;
    }

}
