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

import org.junit.Test;

import static com.spectralogic.ds3autogen.python.generators.client.BaseClientGenerator.toPythonCommandName;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BaseClientGenerator_Test {

    @Test
    public void toPythonCommandName_Test() {
        assertThat(toPythonCommandName(null), is(""));
        assertThat(toPythonCommandName(""), is(""));
        assertThat(toPythonCommandName("SimpleTestRequest"), is("simple_test"));
        assertThat(toPythonCommandName("com.test.HandlerWithPathRequest"), is("handler_with_path"));
    }
}
