<#include "../copyright.ftl"/>

package ${packageName};

<#if javaHelper.isSpectraDs3(packageName)>
import com.spectralogic.ds3client.commands.BulkRequest;
</#if>
import java.util.List;
import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
<#include "../imports.ftl"/>

public class ${name} extends BulkRequest {

    <#list optionalArguments as arg>
    <#if arg.getName() == "MaxUploadSize">
    private static final String MAX_UPLOAD_SIZE_IN_BYTES = "100000000000";
    public static final int MIN_UPLOAD_SIZE_IN_BYTES = 10485760;
    </#if>
    </#list>

    <#list constructorArguments as arg>
    ${javaHelper.createBulkVariable(arg, true)}
    </#list>
    <#list optionalArguments as arg>
    ${javaHelper.createBulkVariable(arg, false)}
    </#list>

    // Constructor
    public ${name}(${javaHelper.constructorArgs(
            helper.addArgument(
                constructorArguments, "Objects", "List<Ds3Object>"))}) throws XmlProcessingException {
        super(bucketName, objects);
        <#if operation??>
        this.getQueryParams().put("operation", "${operation.toString()?lower_case}");
        </#if>
<#include "common/constructor_get_query_params.ftl"/>

    }

    <#list optionalArguments as arg>
${javaHelper.createWithConstructorBulk(arg, name)}
    </#list>

    <#list constructorArguments as arg>
    <#if javaHelper.isBulkRequestArg(arg.getName()) == false>
    public ${javaHelper.getType(arg)} get${arg.getName()?cap_first}() {
        return this.${arg.getName()?uncap_first};
    }
    </#if>
    </#list>

    <#list helper.removeArgument(optionalArguments, "MaxUploadSize") as arg>
    <#if javaHelper.isBulkRequestArg(arg.getName()) == false>
    public ${javaHelper.getType(arg)} get${arg.getName()?cap_first}() {
        return this.${arg.getName()?uncap_first};
    }

    </#if>
    </#list>
    @Override
    public BulkCommand getCommand() {
        return BulkCommand.${helper.getBulkVerb(operation)};
    }
}