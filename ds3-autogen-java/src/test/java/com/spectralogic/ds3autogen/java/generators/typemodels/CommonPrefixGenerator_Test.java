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

package com.spectralogic.ds3autogen.java.generators.typemodels;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Ds3Element;
import com.spectralogic.ds3autogen.java.models.Element;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class CommonPrefixGenerator_Test {

    private static final CommonPrefixGenerator generator = new CommonPrefixGenerator();

    @Test
    public void toElementList_NullList_Test() {
        final ImmutableList<Element> result = generator.toElementList(null);
        assertThat(result.size(), is(0));
    }

    @Test
    public void toElementList_EmptyList_Test() {
        final ImmutableList<Element> result = generator.toElementList(ImmutableList.of());
        assertThat(result.size(), is(0));
    }

    @Test
    public void toElementList_List_Test() {
        final ImmutableList<Ds3Element> elements = ImmutableList.of(
                new Ds3Element("CommonPrefixes", "array", "java.lang.String"),
                new Ds3Element("CreationDate", "java.util.Date", null));

        final ImmutableList<Element> result = generator.toElementList(elements);
        assertThat(result.size(), is(2));
        assertThat(result.get(0).getName(), is("CommonPrefixes"));
        assertThat(result.get(0).getType(), is("array"));
        assertThat(result.get(0).getComponentType(), is("CommonPrefixes"));

        assertThat(result.get(1).getName(), is("CreationDate"));
        assertThat(result.get(1).getType(), is("java.util.Date"));
        assertThat(result.get(1).getComponentType(), is(nullValue()));
    }
}