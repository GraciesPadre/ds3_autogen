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

package com.spectralogic.ds3autogen.test.helpers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseType;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;

import static com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures.createPopulatedDs3ResponseCodeList;
import static com.spectralogic.ds3autogen.testutil.Ds3ModelPartialDataFixture.createDs3RequestTestData;
import static com.spectralogic.ds3autogen.testutil.Ds3ModelPartialDataFixture.createEmptyDs3Type;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * This class provides utilities for testing the Response Type Converter utility
 */
public class ResponseTypeConverterHelper {

    private ResponseTypeConverterHelper() { }

    /**
     * Creates a populated Ds3Request. This request is empty except for having
     * a uniquely populated response code list which has unique response type names.
     * The unique names are created by appending the user supplied variations to
     * the template names.
     */
    public static Ds3Request createPopulatedDs3Request(
            final String firstVariation,
            final String secondVariation
    ) {
        return createDs3RequestTestData(
                false,
                createPopulatedDs3ResponseCodeList(firstVariation, secondVariation),
                null, null);
    }

    /**
     * Creates a populated list of Ds3Requests. These requests are empty except for
     * having uniquely populated response code lists based on the user specified
     * variations to be added to the type names.
     */
    public static ImmutableList<Ds3Request> createPopulatedDs3RequestList(
            final String firstVariation,
            final String secondVariation,
            final String thirdVariation,
            final String fourthVariation
    ) {
        return ImmutableList.of(
                createPopulatedDs3Request(firstVariation, secondVariation),
                createPopulatedDs3Request(thirdVariation, fourthVariation));
    }

    /**
     * Creates a populated Ds3Type map which contains TapePartition and TapeDriveType keys,
     * but where the associated types are both empty
     */
    public static ImmutableMap<String, Ds3Type> createPopulatedDs3TypeMap() {
        return ImmutableMap.of(
                "com.spectralogic.s3.common.dao.domain.tape.TapePartition", createEmptyDs3Type(),
                "com.spectralogic.s3.common.dao.domain.tape.TapeDriveType", createEmptyDs3Type());
    }

    /**
     * Verifies that a list of ResponseTypes that were generated by this utility have been
     * properly updated.
     * @param updatedResponseTypes The list of updated Response Types
     * @param originalResponseTypes The list of original Response Types that needed updating
     * @param variation The name variation used to auto populate unique Response Type names
     */
    public static void verifyPopulatedResponseTypeList(
            final ImmutableList<Ds3ResponseType> updatedResponseTypes,
            final ImmutableList<Ds3ResponseType> originalResponseTypes,
            final String variation) {
        assertThat(updatedResponseTypes.size(), is(4));
        assertThat(updatedResponseTypes.get(0), is(originalResponseTypes.get(0)));
        assertThat(updatedResponseTypes.get(2), is(originalResponseTypes.get(2)));

        assertThat(updatedResponseTypes.get(1).getType(),
                is("SimpleComponentType" + variation + "List"));
        assertThat(updatedResponseTypes.get(1).getComponentType(), is(nullValue()));
        assertThat(updatedResponseTypes.get(3).getType(),
                is("com.spectralogic.s3.common.dao.domain.ds3.BucketAcl" + variation + "List"));
        assertThat(updatedResponseTypes.get(3).getComponentType(), is(nullValue()));
    }
}
