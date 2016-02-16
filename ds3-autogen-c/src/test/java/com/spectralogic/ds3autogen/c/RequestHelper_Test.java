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

package com.spectralogic.ds3autogen.c;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Ds3ResponseCode;
import com.spectralogic.ds3autogen.api.models.Ds3ResponseType;
import com.spectralogic.ds3autogen.c.helpers.RequestHelper;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RequestHelper_Test {

    @Test
    public void testHasResponsePayload() {
        final Ds3ResponseType responseType = new Ds3ResponseType("com.spectralogic.s3.server.domain.BucketObjectsApiBean", null);
        final Ds3ResponseCode responseCode = new Ds3ResponseCode(200, ImmutableList.of(responseType));
        assertTrue(RequestHelper.hasResponsePayload(ImmutableList.of(responseCode)));
    }

    @Test
    public void testEmptyResponsePayload() {
        final Ds3ResponseType responseTypeNull = new Ds3ResponseType("null", null);
        final Ds3ResponseCode responseCode200 = new Ds3ResponseCode(200, ImmutableList.of(responseTypeNull));

        final Ds3ResponseType responseTypeError = new Ds3ResponseType("com.spectralogic.s3.server.domain.HttpErrorResultApiBean", null);
        final Ds3ResponseCode responseCode400 = new Ds3ResponseCode(400, ImmutableList.of(responseTypeError));

        final Ds3ResponseCode responseCode403 = new Ds3ResponseCode(403, ImmutableList.of(responseTypeError));

        final Ds3ResponseCode responseCode409 = new Ds3ResponseCode(409, ImmutableList.of(responseTypeError));
        assertFalse(RequestHelper.hasResponsePayload(ImmutableList.of(responseCode200, responseCode400, responseCode403, responseCode409)));
    }
}