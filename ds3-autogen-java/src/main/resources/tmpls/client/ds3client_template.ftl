<#include "../copyright.ftl"/>

package ${packageName};

import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.notifications.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.commands.spectrads3.notifications.*;
import com.spectralogic.ds3client.models.bulk.Node;
import com.spectralogic.ds3client.networking.ConnectionDetails;

import java.io.Closeable;
import java.io.IOException;
import java.security.SignatureException;

public interface Ds3Client extends Closeable {

    ConnectionDetails getConnectionDetails();

    <#list commands as cmd>
    ${cmd.getResponseName()} ${cmd.getName()?uncap_first}(${cmd.getRequestName()} request)
            throws IOException, SignatureException;
    </#list>

    Ds3Client newForNode(Node node);
}