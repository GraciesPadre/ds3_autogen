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

package com.spectralogic.ds3autogen.java.helpers;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Arguments;
import com.spectralogic.ds3autogen.api.models.Ds3ResponseCode;
import com.spectralogic.ds3autogen.api.models.Operation;
import com.spectralogic.ds3autogen.java.models.Constants;
import com.spectralogic.ds3autogen.java.models.Element;
import com.spectralogic.ds3autogen.java.models.EnumConstant;
import com.spectralogic.ds3autogen.utils.Helper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;

public class JavaHelper {
    private final static JavaHelper javaHelper = new JavaHelper();

    private final static List<String> bulkBaseClassArgs = Arrays.asList("Priority", "WriteOptimization", "BucketName");
    private final static String INDENT = "    ";

    private JavaHelper() {}

    public static JavaHelper getInstance() {
        return javaHelper;
    }

    public static boolean isBulkRequestArg(final String name) {
        return bulkBaseClassArgs.contains(name);
    }

    public static String createBulkVariable(final Arguments arg, final boolean isRequired) {
        if (isBulkRequestArg(arg.getName())) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        builder.append("private ");
        if (isRequired) {
            builder.append("final ");
        }
        builder.append(getType(arg)).append(" ").append(uncapFirst(arg.getName())).append(";");
        return builder.toString();
    }

    public static String createWithConstructor(final Arguments arg, final String requestName) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (arg.getType().equals("void")) {
            stringBuilder.append(voidWithConstructor(arg, requestName));
        } else {
            stringBuilder.append(withConstructor(arg, requestName));
        }

        stringBuilder.append(indent(2)).append("return this;\n").append(indent(1)).append("}\n");
        return stringBuilder.toString();
    }

    public static String createGetter(final String argName, final String argType) {
        return "public " + argType + " get" + capFirst(argName) + "() {\n"
                + indent(2) + "return this." + uncapFirst(argName) + ";\n"
                + indent(1) + "}\n";
    }

    public static String createWithConstructorBulk(final Arguments arg, final String requestName) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (bulkBaseClassArgs.contains(arg.getName())) {
            stringBuilder.append(indent(1)).append("@Override\n").append(withConstructorFirstLine(arg, requestName)).append(indent(2)).append("super.with").append(capFirst(arg.getName())).append("(").append(uncapFirst(arg.getName())).append(");\n");
        } else if (arg.getName().equals("MaxUploadSize")) {
            stringBuilder.append(maxUploadSizeWithConstructor(arg, requestName));
        } else if (arg.getType().equals("void")) {
            stringBuilder.append(voidWithConstructor(arg, requestName));
        } else {
            stringBuilder.append(withConstructor(arg, requestName));
        }

        stringBuilder.append(indent(2)).append("return this;\n").append(indent(1)).append("}\n");
        return stringBuilder.toString();
    }

    private static String withConstructor(final Arguments arg, final String requestName) {
        return withConstructorFirstLine(arg, requestName)
                + indent(2) + argAssignmentLine(arg.getName())
                + indent(2) + updateQueryParamLine(arg.getName(), argToString(arg));
    }

    private static String maxUploadSizeWithConstructor(final Arguments arg, final String requestName) {
        return withConstructorFirstLine(arg, requestName)
                + indent(2) + "if (" + uncapFirst(arg.getName()) + " > MIN_UPLOAD_SIZE_IN_BYTES) {\n"
                + indent(3) + putQueryParamLine(arg.getName(), argToString(arg)) + "\n"
                + indent(2) + "} else {\n"
                + indent(3) + putQueryParamLine(arg.getName(), "MAX_UPLOAD_SIZE_IN_BYTES") + "\n"
                + indent(2) + "}\n";
    }

    private static String voidWithConstructor(final Arguments arg, final String requestName) {
        return withConstructorFirstLine(arg, requestName)
                + indent(2) + "this." + uncapFirst(arg.getName()) + " = " + uncapFirst(arg.getName()) + ";\n"
                + indent(2) + "if (this." + uncapFirst(arg.getName()) + ") {\n"
                + indent(3) + putQueryParamLine(arg.getName(), "null") + "\n"
                + indent(2) + "} else {\n"
                + indent(3) + removeQueryParamLine(arg.getName())
                + indent(2) + "}\n";
    }

    private static String withConstructorFirstLine(final Arguments arg, final String requestName) {
        return indent(1) + "public " + requestName + " with" + capFirst(arg.getName()) + "(final " + getType(arg) + " " + uncapFirst(arg.getName()) + ") {\n";
    }

    private static String argAssignmentLine(final String name) {
        return "this." + uncapFirst(name) + " = " + uncapFirst(name) + ";\n";
    }

    private static String removeQueryParamLine(final String name) {
        return "this.getQueryParams().remove(\"" + Helper.camelToUnderscore(name) + "\");\n";
    }

    public static String putQueryParamLine(final Arguments arg) {
        return putQueryParamLine(arg.getName(), argToString(arg));
    }

    private static String putQueryParamLine(final String name, final String type) {
        return "this.getQueryParams().put(\"" + Helper.camelToUnderscore(name) + "\", " + type + ");";
    }

    private static String updateQueryParamLine(final String name, final String type) {
        return "this.updateQueryParam(\"" + Helper.camelToUnderscore(name) + "\", " + type + ");\n";
    }

    public static String toXmlLine(
            final String outputStringName,
            final String objectListName,
            final Operation operation) {
        final StringBuilder builder = new StringBuilder();
        builder.append("final String ").append(outputStringName).append(" = XmlOutput.toXml(").append(objectListName).append(", ");
        if (operation == Operation.START_BULK_PUT) {
            return builder.append("true);").toString();
        }
        return  builder.append("false);").toString();
    }

    private static String indent(final int depth) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append(INDENT);
        }
        return stringBuilder.toString();
    }

    public static String argToString(final Arguments arg) {
        switch (arg.getType().toLowerCase()) {
            case "boolean":
            case "void":
                return "null";
            case "string":
                return uncapFirst(arg.getName());
            case "double":
            case "integer":
            case "long":
                return capFirst(arg.getType()) + ".toString(" + uncapFirst(arg.getName()) + ")";
            case "int":
                return "Integer.toString(" + uncapFirst(arg.getName()) + ")";
            default:
                return uncapFirst(arg.getName()) + ".toString()";
        }
    }

    public static ImmutableList<Arguments> removeArgument(final List<Arguments> arguments, final String name) {
        final ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
        for (final Arguments arg : arguments) {
            if (!arg.getName().equals(name)) {
                builder.add(arg);
            }
        }
        return builder.build();
    }

    public static ImmutableList<Arguments> addArgument(
            final ImmutableList<Arguments> arguments,
            final ImmutableList<Arguments> additionalArguments) {
        final ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
        if (hasContent(arguments)) {
            builder.addAll(arguments);
        }
        if (hasContent(additionalArguments)) {
            builder.addAll(additionalArguments);
        }
        return builder.build();
    }

    public static ImmutableList<Arguments> addArgument(
            final ImmutableList<Arguments> arguments,
            final String argName,
            final String argType) {
        final ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
        if (hasContent(arguments)) {
            builder.addAll(arguments);
        }
        builder.add(new Arguments(argType, argName));
        return builder.build();
    }

    public static ImmutableList<Arguments> sortConstructorArgs(final ImmutableList<Arguments> arguments) {
        final List<Arguments> sortable = new ArrayList<>();
        sortable.addAll(arguments);
        Collections.sort(sortable, new CustomArgumentComparator());

        final ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
        builder.addAll(sortable);
        return builder.build();
    }

    public static String argTypeList(final ImmutableList<Arguments> arguments) {
        return sortConstructorArgs(arguments)
                .stream()
                .map(Arguments::getType).collect(Collectors.joining(", "));
    }

    public static String argsToList(final List<Arguments> arguments) {
        return arguments.stream().map(i -> uncapFirst(i.getName())).collect(Collectors.joining(", "));
    }

    public static String capFirst(final String str) {
        return StringUtils.capitalize(str);
    }

    public static String uncapFirst(final String str) {
        return StringUtils.uncapitalize(str);
    }

    public static String getType(final Arguments arg) {
        if (arg.getType() == null) {
            return "";
        }

        switch (arg.getType()) {
            case "void":
                return "boolean";
            case "Integer":
                return "int";
            case "ChecksumType":
                return arg.getType() + ".Type";
            default:
                return arg.getType();
        }
    }

    public static String constructorArgs(final ImmutableList<Arguments> requiredArguments) {
        return sortConstructorArgs(requiredArguments)
                .stream()
                .map(i -> "final " + getType(i) + " " + uncapFirst(i.getName()))
                .collect(Collectors.joining(", "));
    }

    /*
     * Removes all void arguments from the provided list.  This is used with the required params
     * list to prevent the unnecessary inclusion of void variables into variable list and constructors.
     */
    public static ImmutableList<Arguments> removeVoidArguments(
            final ImmutableList<Arguments> arguments) {
        return adjustVoidArguments(arguments, AdjustArgType.REMOVE_VOID);
    }

    /*
     * Gets all void arguments from the provided list.  This is used within constructors to add
     * query parameters that are always required.
     */
    public static ImmutableList<Arguments> getVoidArguments(
            final ImmutableList<Arguments> arguments) {
        return adjustVoidArguments(arguments, AdjustArgType.SELECT_VOID);
    }

    protected enum AdjustArgType { SELECT_VOID, REMOVE_VOID }

    protected static ImmutableList<Arguments> adjustVoidArguments(
            final ImmutableList<Arguments> arguments,
            final AdjustArgType adjustment) {
        if (isEmpty(arguments)) {
            return ImmutableList.of();
        }
        final ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
        for (final Arguments arg : arguments) {
            if (addVoidArgument(arg, adjustment)) {
                builder.add(arg);
            }
        }
        return builder.build();
    }

    protected static boolean addVoidArgument(final Arguments arg, final AdjustArgType adjustment) {
        if (adjustment.equals(AdjustArgType.REMOVE_VOID)) {
            return !arg.getType().equals("void");
        }
        return arg.getType().equals("void");
    }

    /*
     * Creates a comma separated list of argument names, while changing one argument name to a specified value
     */
    public static String modifiedArgNameList(
            final ImmutableList<Arguments> arguments,
            final String modifyArgName, final String toArgName) {
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (final Arguments arg : arguments) {
            if (arg.getName().equals(modifyArgName)) {
                builder.add(toArgName);
            } else {
                builder.add(uncapFirst(arg.getName()));
            }
        }
        return builder.build()
                .stream()
                .map(i -> i)
                .collect(Collectors.joining(", "));
    }

    public static String getResponseCodes(final ImmutableList<Ds3ResponseCode> responseCodes) {
        final List<String> sortable = new ArrayList<>();
        for (final Ds3ResponseCode responseCode : responseCodes) {
            sortable.add(Integer.toString(responseCode.getCode()));
        }
        Collections.sort(sortable);
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        builder.addAll(sortable);
        return builder.build()
                .stream()
                .map(i -> i)
                .collect(Collectors.joining(", "));
    }

    public static String getModelVariable(final Element element) {
        final StringBuilder builder = new StringBuilder();
        builder.append(indent(1)).append("@JsonProperty(\"").append(capFirst(element.getName())).append("\")\n");
        if (element.getComponentType() != null) {
            builder.append(indent(1)).append("@JacksonXmlElementWrapper\n");
        }
        builder.append(indent(1)).append("private ").append(convertType(element)).append(" ").append(uncapFirst(element.getName())).append(";");
        return builder.toString();
    }

    public static String convertType(final Element element) throws IllegalArgumentException {
        if (isEmpty(element.getComponentType())) {
            return stripPath(element.getType());
        }
        if (element.getType().equalsIgnoreCase("array")) {
            return "List<" + stripPath(element.getComponentType()) + ">";
        }
        throw new IllegalArgumentException("Unknown element type: " + element.getType());
    }

    public static String stripPath(final String str) {
        final String[] classparts = str.split("\\.");
        return classparts[classparts.length - 1];
    }

    public static String getModelConstructorArgs(final ImmutableList<Element> elements) {
        if (isEmpty(elements)) {
            return "";
        }
        return sortModelConstructorArgs(elements)
                .stream()
                .map(i -> "final " + convertType(i) + " " + uncapFirst(i.getName()))
                .collect(Collectors.joining(", "));
    }

    public static ImmutableList<Element> sortModelConstructorArgs(final ImmutableList<Element> elements) {
        if (isEmpty(elements)) {
            return ImmutableList.of();
        }
        final List<Element> sortable = new ArrayList<>();
        sortable.addAll(elements);
        Collections.sort(sortable, new CustomElementComparator());

        final ImmutableList.Builder<Element> builder = ImmutableList.builder();
        builder.addAll(sortable);
        return builder.build();
    }

    public static String getEnumValues(
            final ImmutableList<EnumConstant> enumConstants,
            final int indent) {
        if (isEmpty(enumConstants)) {
            return "";
        }
        return enumConstants
                .stream()
                .map(i -> indent(indent) + i.getName())
                .collect(Collectors.joining(",\n"));
    }

    public static ImmutableList<EnumConstant> addEnum(
            final ImmutableList<EnumConstant> enumConstants,
            final String newEnumValue) {
        final ImmutableList.Builder<EnumConstant> builder = ImmutableList.builder();
        if (hasContent(enumConstants)) {
            builder.addAll(enumConstants);
        }
        builder.add(new EnumConstant(newEnumValue));
        return builder.build();
    }

    public static boolean isSpectraDs3(final String packageName) {
        return packageName.contains(Constants.SPECTRA_DS3_PACKAGE);
    }

    public static boolean isSpectraDs3OrNotification(final String packageName) {
        return isSpectraDs3(packageName) || packageName.contains(Constants.NOTIFICATION_PACKAGE);
    }
}
