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

package com.spectralogic.ds3autogen.java.generators.requestmodels;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Arguments;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.java.models.RequestConstructor;
import com.spectralogic.ds3autogen.java.models.Variable;

public interface RequestGeneratorUtils {

    /**
     * Gets all the class variables to properly generate the variables and their
     * getter functions.
     */
    ImmutableList<Variable> toClassVariableArguments(final Ds3Request ds3Request);

    /**
     * Gets all the required imports that the Request will need in order to properly
     * generate the Java request code
     */
    ImmutableList<String> getAllImports(final Ds3Request ds3Request, final String packageName);

    /**
     * Returns the import for the parent class for the request
     */
    String getParentImport(final Ds3Request ds3Request);

    /**
     * Gets the list of Arguments needed to create the request constructor
     */
    ImmutableList<Arguments> toConstructorArgumentsList(final Ds3Request ds3Request);

    /**
     * Gets the list of required Arguments from a Ds3Request
     */
    ImmutableList<Arguments> toRequiredArgumentsList(final Ds3Request ds3Request);

    /**
     * Gets the list of constructor models from a Ds3Request
     */
    ImmutableList<RequestConstructor> toConstructorList(final Ds3Request ds3Request);

    /**
     * Gets the list of Arguments that the constructor must add to the query param list
     */
    ImmutableList<Arguments> toQueryParamsList(final Ds3Request ds3Request);
}
