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

package com.spectralogic.ds3autogen.net.model.common;

public class NullableVariable {

    final private String name;
    final private String type;

    /** Denotes if the type must be followed by a '?' in order for the type to be nullable */
    final private boolean questionMarkForNullable;

    public NullableVariable(final String name, final String type, final boolean questionMarkForNullable) {
        this.name = name;
        this.type = type;
        this.questionMarkForNullable = questionMarkForNullable;
    }

    /**
     * Retrieves the nullable version of a type
     */
    public String getNullableType() {
        if (questionMarkForNullable) {
            return type + "?";
        }
        return type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isQuestionMarkForNullable() {
        return questionMarkForNullable;
    }
}
