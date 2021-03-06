/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3autogen.python.generators.client;

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.python.model.client.BaseClient;

import static com.spectralogic.ds3autogen.utils.ClientGeneratorUtil.toCommandName;
import static com.spectralogic.ds3autogen.utils.Helper.camelToUnderscore;
import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.toResponseName;

public class BaseClientGenerator implements ClientModelGenerator<BaseClient> {

    @Override
    public BaseClient generate(final Ds3Request ds3Request) {
        final String commandName = toPythonCommandName(ds3Request.getName());
        final String responseName = toResponseName(ds3Request.getName());

        return new BaseClient(commandName, responseName);
    }

    /**
     * Converts the Ds3Request name into the command name used in the client
     * ex: GetBucketRequest -> get_bucket
     */
    protected static String toPythonCommandName(final String requestName) {
        return camelToUnderscore(toCommandName(requestName));
    }
}
