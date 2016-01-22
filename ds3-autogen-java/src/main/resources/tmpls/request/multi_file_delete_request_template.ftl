<#include "../copyright.ftl"/>

package ${packageName};

<#include "common/import_abstract_request.ftl"/>
import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.delete.Delete;
import com.spectralogic.ds3client.models.delete.DeleteObject;
import com.spectralogic.ds3client.serializer.XmlOutput;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
<#include "../imports.ftl"/>

public class ${name} extends AbstractRequest {

    // Variables
    private final List<String> objects;
<#include "common/variables.ftl"/>
    private boolean quiet = false;
    private long size;

    // Constructor
    public ${name}(${javaHelper.constructorArgs(
                     helper.addArgument(constructorArguments, "Objects", "List<String>"))}) {
        <#list constructorArguments as arg>
        this.${arg.getName()?uncap_first} = ${arg.getName()?uncap_first};
        </#list>
        this.objects = objects;
<#include "common/constructor_get_query_params.ftl"/>
    }

    public ${name}(${javaHelper.constructorArgs(
                     helper.addArgument(
                     helper.removeArgument(constructorArguments, "Delete"), "Objs", "Iterable<Contents>"))}) {
        this(${javaHelper.argsToList(
               helper.removeArgument(constructorArguments, "Delete"))}, contentsToString(objs));
    }

    private static List<String> contentsToString(final Iterable<Contents> objs) {
        final List<String> objKeyList = new ArrayList<>();
        for (final Contents obj : objs) {
            objKeyList.add(obj.getKey());
        }
        return objKeyList;
    }

    <#include "common/with_constructors.ftl"/>

    public ${name} withQuiet(final boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    @Override
    public InputStream getStream() {

        final Delete delete = new Delete();
        delete.setQuiet(quiet);
        final List<DeleteObject> deleteObjects = new ArrayList<>();

        for(final String objName : objects) {
            deleteObjects.add(new DeleteObject(objName));
        }

        delete.setDeleteObjectList(deleteObjects);

        final String xmlOutput = XmlOutput.toXml(delete);
        final byte[] stringBytes = xmlOutput.getBytes();
        this.size = stringBytes.length;

        return new ByteArrayInputStream(stringBytes);
    }

    <#include "common/getters_verb_path.ftl"/>

    ${javaHelper.createGetter("Objects", "List<String>")}

    ${javaHelper.createGetter("Quiet", "boolean")}

<#include "common/getters.ftl"/>

    @Override
    ${javaHelper.createGetter("Size", "long")}
}