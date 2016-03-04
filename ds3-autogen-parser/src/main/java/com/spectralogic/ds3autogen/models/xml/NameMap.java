/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3autogen.models.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spectralogic.ds3autogen.api.models.NameMapperType;

public class NameMap {

    @JsonProperty("ContractName")
    private String contractName;

    @JsonProperty("SdkName")
    private String sdkName;

    @JsonProperty("Type")
    private NameMapperType type = NameMapperType.NONE;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(final String contractName) {
        this.contractName = contractName;
    }

    public String getSdkName() {
        return sdkName;
    }

    public void setSdkName(final String sdkName) {
        this.sdkName = sdkName;
    }

    public NameMapperType getType() {
        return type;
    }

    public void setType(final NameMapperType type) {
        this.type = type;
    }
}
