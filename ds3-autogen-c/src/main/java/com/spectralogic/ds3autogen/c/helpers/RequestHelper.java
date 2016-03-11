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

package com.spectralogic.ds3autogen.c.helpers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3autogen.c.models.Parameter;
import com.spectralogic.ds3autogen.c.models.Request;
import com.spectralogic.ds3autogen.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;

public final class RequestHelper {
    private static final Logger LOG = LoggerFactory.getLogger(RequestHelper.class);
    private final static RequestHelper requestHelper = new RequestHelper();

    public static RequestHelper getInstance() {
        return requestHelper;
    }

    public static String getNameRoot(final String name) {
        return Helper.removeTrailingRequestHandler(Helper.unqualifiedName(name));
    }

    public static String getNameRootUnderscores(final String name) {
        return Helper.camelToUnderscore(getNameRoot(name));
    }

    public static String getAmazonS3InitParams(final ImmutableMap<String, String> requiredArgs) {
        if (!requiredArgs.containsKey("bucketName")) {
            return "void";
        } else if (requiredArgs.containsKey("objectName")) {
            return "const char* bucket_name, const char* object_name";
        }
        return "const char* bucket_name";
    }

    public static String getSpectraS3InitParams(final boolean isResourceIdRequired) {
        if (isResourceIdRequired) {
            return "const char* resource_id";
        }
        return "void";
    }

    public static String paramListToString(final ImmutableList<Parameter> paramList) {
        if (isEmpty(paramList)) {
            return "";
        }
        return paramList
                .stream()
                .map(parm -> parm.toString())
                .collect(Collectors.joining(", "));
    }

    public static String generateInitRequestFunctionSignature(final Request request) {
        return "ds3_request* init_" + getNameRootUnderscores(request.getName()) + "(" + paramListToString(request.getParamList()) + ")";
    }

    public static String generateRequestFunctionSignature(final Request request) {
        return "ds3_request* " + getNameRootUnderscores(request.getName()) + "(" + paramListToString(request.getParamList()) + ")";
    }

    /**
     * FreeMarker work around
     * @param immutableMap
     * @param key
     * @return
     */
    public static boolean containsKey(final ImmutableMap<String, String> immutableMap, final String key) {
        return immutableMap.containsKey(key);
    }
}
