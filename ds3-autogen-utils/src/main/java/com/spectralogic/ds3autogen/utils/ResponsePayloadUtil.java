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

package com.spectralogic.ds3autogen.utils;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Ds3ResponseCode;
import com.spectralogic.ds3autogen.api.models.Ds3ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;

/**
 * Contains utility functions for categorizing and retrieving a
 * request handler's response payloads
 */
public final class ResponsePayloadUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ResponsePayloadUtil.class);

    /**
     * Determines if a given response code denotes a non-error response
     */
    public static boolean isNonErrorCode(final int responseCode) {
        return !isErrorCode(responseCode);
    }

    /**
     * Determines if a given response code denotes an error response
     */
    public static boolean isErrorCode(final int responseCode) {
        return responseCode >= 300;
    }

    /**
     * Determines if a list of Ds3Response codes has at least one response payload.
     * Note: Some commands can have multiple non-error codes with different response payloads.
     */
    public static boolean hasResponsePayload(final ImmutableList<Ds3ResponseCode> responseCodes) {
        final ImmutableList<String> responseTypes = getAllResponseTypes(responseCodes);
        for (final String responseType : responseTypes) {
            if (!responseType.equals("null")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the list of Response Types from a list of Ds3ResponseCodes
     */
    public static ImmutableList<String> getAllResponseTypes(final ImmutableList<Ds3ResponseCode> responseCodes) {
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        if (isEmpty(responseCodes)) {
            //No response codes is logged as an error instead of throwing an error
            //because some test may not contain response codes
            LOG.error("There are no Response Codes associated with this request");
            return ImmutableList.of();
        }
        for (final Ds3ResponseCode responseCode : responseCodes) {
            if (isNonErrorCode(responseCode.getCode())) {
                builder.add(getResponseType(responseCode.getDs3ResponseTypes()));
            }
        }
        return builder.build();
    }

    /**
     * Gets the Response Type associated with a Ds3ResponseCode. This assumes that all component
     * response types have already been converted into encapsulating types, which is done within
     * the parser module.
     */
    public static String getResponseType(final ImmutableList<Ds3ResponseType> responseTypes) {
        if (isEmpty(responseTypes)) {
            throw new IllegalArgumentException("Response code does not contain any associated types");
        }
        switch (responseTypes.size()) {
            case 1:
                return responseTypes.get(0).getType();
            default:
                throw new IllegalArgumentException("Response code has multiple associated types");
        }
    }
}
