    <#list helper.removeVoidArguments(
               helper.removeArgument(requiredArguments, "NotificationEndPoint")) as arg>
    private final ${javaHelper.getType(arg)} ${arg.getName()?uncap_first};
    </#list>

    <#list optionalArguments as arg>
    private ${javaHelper.getType(arg)} ${arg.getName()?uncap_first};
    </#list>