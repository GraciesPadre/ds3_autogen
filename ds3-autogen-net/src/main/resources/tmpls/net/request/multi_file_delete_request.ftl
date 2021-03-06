<#include "../common/copyright.ftl" />

using Ds3.Models;
using Ds3.Runtime;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Xml.Linq;

namespace Ds3.Calls
{
    public class ${name} : Ds3Request
    {
        <#include "common/required_args.ftl" />

        <#include "common/optional_args.ftl" />

        <#list constructors as constructor>
        public ${name}(${netHelper.constructor(constructor.constructorArgs)})
        {
            <#list constructor.constructorArgs as arg>
            this.${arg.getName()?cap_first} = ${netHelper.paramAssignmentRightValue(arg)};
            </#list>
            <#if constructor.operation??>
            this.QueryParams.Add("operation", "${operation.toString()?lower_case}");
            </#if>
            <#include "common/add_query_params.ftl" />

        }
        </#list>

        internal override Stream GetContentStream()
        {
            return new XDocument()
                .AddFluent(
                    new XElement("Delete").AddAllFluent(
                        from curObject in this.Objects
                        select new XElement("Object").AddFluent(new XElement("Key").SetValueFluent(curObject.Name))
                    )
                )
                .WriteToMemoryStream();
        }

        internal override long GetContentLength()
        {
            return GetContentStream().Length;
        }

        internal override ChecksumType ChecksumValue
        {
            get { return ChecksumType.Compute; }
        }

        <#include "common/http_verb_and_path.ftl" />
    }
}