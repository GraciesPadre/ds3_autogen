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

package com.spectralogic.ds3autogen.net.model.client;

public class PayloadCommand implements BaseCommand {

    private final String requestName;
    private final String commandName;
    private final String responseType;

    public PayloadCommand(final String requestName, final String commandName, final String responseType) {
        this.requestName = requestName;
        this.commandName = commandName;
        this.responseType = responseType;
    }

    @Override
    public String getRequestName() {
        return requestName;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public String getResponseType() {
        return responseType;
    }
}
