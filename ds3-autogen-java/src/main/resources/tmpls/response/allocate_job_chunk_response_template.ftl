<#include "../copyright.ftl"/>

package ${packageName};

<#include "common/response_imports.ftl"/>
import com.spectralogic.ds3client.exceptions.RetryAfterExpectedException;

public class ${name} extends AbstractResponse {

${javaHelper.createAllResponseResultClassVars(responseCodes)}

    public enum Status {
        ALLOCATED, RETRYLATER
    }

<#include "common/allocated_retrylater_status_types_getters.ftl"/>

<#include "common/response_constructor.ftl"/>

    @Override
    protected void processResponse() throws IOException {
        try (final WebResponse response = this.getResponse()) {
            this.checkStatusCode(200, 307, 503);

            switch (this.getStatusCode()) {
            case 200:
                try (final InputStream content = response.getResponseStream()) {
                    this.objectsResult = XmlOutput.fromXml(content, Objects.class);
                    this.status = Status.ALLOCATED;
                }
                break;
            case 307:
            case 503:
                this.status = Status.RETRYLATER;
                this.retryAfterSeconds = parseRetryAfter(response);
                break;
            default:
                assert false : "checkStatusCode should have made it impossible to reach this line.";
            }
        } finally {
            this.getResponse().close();
        }
    }

<#include "common/parse_retry_after.ftl"/>

${javaHelper.createAllResponseResultGetters(responseCodes)}

}