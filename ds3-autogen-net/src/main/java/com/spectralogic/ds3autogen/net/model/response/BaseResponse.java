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

package com.spectralogic.ds3autogen.net.model.response;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Arguments;
import com.spectralogic.ds3autogen.net.NetHelper;

public class BaseResponse {

    private final NetHelper netHelper;
    private final String name;
    private final ImmutableList<Arguments> arguments;

    public BaseResponse(
            final NetHelper netHelper,
            final String name,
            final ImmutableList<Arguments> arguments) {
        this.netHelper = netHelper;
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public ImmutableList<Arguments> getArguments() {
        return arguments;
    }

    public NetHelper getNetHelper() {
        return netHelper;
    }
}

