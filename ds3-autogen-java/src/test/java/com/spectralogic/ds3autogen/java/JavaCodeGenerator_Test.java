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

package com.spectralogic.ds3autogen.java;

import com.spectralogic.d3autogen.Ds3SpecParserImpl;
import com.spectralogic.d3autogen.Ds3TypeMapperParserImpl;
import com.spectralogic.ds3autogen.api.*;
import com.spectralogic.ds3autogen.api.models.Ds3ApiSpec;
import com.spectralogic.ds3autogen.api.models.Ds3TypeMapper;
import com.spectralogic.ds3autogen.java.utils.TestFileUtilImpl;
import com.spectralogic.ds3autogen.java.utils.TestHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.spectralogic.ds3autogen.java.utils.TestHelper.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JavaCodeGenerator_Test {

    private static final Logger LOG = LoggerFactory.getLogger(JavaCodeGenerator_Test.class);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void singleRequestHandler() throws IOException, ParserException {
        final FileUtils fileUtils = mock(FileUtils.class);
        final Path requestPath = Paths.get("./ds3-sdk/src/main/java/com/spectralogic/ds3client/commands/GetObjectRequestHandler.java");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);

        when(fileUtils.getOutputFile(requestPath)).thenReturn(outputStream);

        final Ds3SpecParser parser = new Ds3SpecParserImpl();
        final Ds3ApiSpec spec = parser.getSpec(JavaCodeGenerator_Test.class.getResourceAsStream("/input/singleRequestHandler.xml"));
        final CodeGenerator codeGenerator = new JavaCodeGenerator();

        final Ds3TypeMapperParser typeParser = new Ds3TypeMapperParserImpl();
        final Ds3TypeMapper typeMapper = typeParser.getMap();

        codeGenerator.generate(spec, typeMapper, fileUtils, Paths.get("."));

        final String generatedCode = new String(outputStream.toByteArray());
        LOG.info("Generated code:\n" + generatedCode);

        assertTrue(extendsClass("GetObjectRequestHandler", "AbstractRequest", generatedCode));
    }

    @Test
    public void getBucket() throws IOException, ParserException {
        final FileUtils fileUtils = mock(FileUtils.class);
        final Path requestPath = Paths.get("./ds3-sdk/src/main/java/com/spectralogic/ds3client/commands/GetBucketRequestHandler.java");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);

        when(fileUtils.getOutputFile(requestPath)).thenReturn(outputStream);

        final Ds3TypeMapperParser typeParser = new Ds3TypeMapperParserImpl();
        final Ds3TypeMapper typeMapper = typeParser.getMap();

        final Ds3SpecParser parser = new Ds3SpecParserImpl();
        final Ds3ApiSpec spec = parser.getSpec(JavaCodeGenerator_Test.class.getResourceAsStream("/input/singleRequestMissingParamTypes.xml"));
        final CodeGenerator codeGenerator = new JavaCodeGenerator();

        codeGenerator.generate(spec, typeMapper, fileUtils, Paths.get("."));

        final String generatedCode = new String(outputStream.toByteArray());
        LOG.info("Generated code:\n" + generatedCode);

        final String requestName = "GetBucketRequestHandler";
        assertTrue(extendsClass(requestName, "AbstractRequest", generatedCode));
        assertTrue(isOptParamOfType("Delimiter", "String", requestName, generatedCode, true));
        assertTrue(isOptParamOfType("Marker", "String", requestName, generatedCode, true));
        assertTrue(isOptParamOfType("MaxKeys", "int", requestName, generatedCode, true));
        assertTrue(isOptParamOfType("Prefix", "String", requestName, generatedCode, true));

        assertTrue(hasImport("com.spectralogic.ds3client.HttpVerb", generatedCode));
    }

    @Test
    public void bulkRequestHandler() throws IOException, ParserException {
        final FileUtils fileUtils = mock(FileUtils.class);
        final Path requestPath = Paths.get("./ds3-sdk/src/main/java/com/spectralogic/ds3client/commands/CreatePutJobRequestHandler.java");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);

        when(fileUtils.getOutputFile(requestPath)).thenReturn(outputStream);

        final Ds3TypeMapperParser typeParser = new Ds3TypeMapperParserImpl();
        final Ds3TypeMapper typeMapper = typeParser.getMap();

        final Ds3SpecParser parser = new Ds3SpecParserImpl();
        final Ds3ApiSpec spec = parser.getSpec(JavaCodeGenerator_Test.class.getResourceAsStream("/input/bulkRequestHandler.xml"));
        final CodeGenerator codeGenerator = new JavaCodeGenerator();

        codeGenerator.generate(spec, typeMapper, fileUtils, Paths.get("."));

        final String generatedCode = new String(outputStream.toByteArray());
        LOG.info("Generated code:\n" + generatedCode);

        //TODO: check out writeOptimization

        final String requestName = "CreatePutJobRequestHandler";
        assertTrue(extendsClass(requestName, "BulkRequest", generatedCode));
        assertTrue(hasMethod("getCommand", "BulkCommand", TestHelper.Scope.PUBLIC, generatedCode));
        assertTrue(isOptParamOfType("IgnoreNamingConflicts", "boolean", requestName, generatedCode, false));
        assertTrue(isOptParamOfType("MaxUploadSize", "long", requestName, generatedCode, true));
        assertTrue(isOptParamOfType("Priority", "Priority", requestName, generatedCode, true));
        assertTrue(isReqParamOfType("Operation", "RestOperationType", requestName, generatedCode, true));

        assertTrue(hasImport("com.spectralogic.s3.server.request.rest.RestOperationType", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.BulkCommand", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.models.bulk.Priority", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.models.bulk.Ds3Object", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.serializer.XmlProcessingException", generatedCode));
        //assertTrue(hasImport("com.spectralogic.ds3client.models.bulk.WriteOptimization", generatedCode));
    }

    @Test
    public void physicalPlacementRequestHandler() throws IOException, ParserException {
        final FileUtils fileUtils = mock(FileUtils.class);
        final Path requestPath = Paths.get("./ds3-sdk/src/main/java/com/spectralogic/ds3client/commands/VerifyPhysicalPlacementForObjectsRequestHandler.java");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);

        when(fileUtils.getOutputFile(requestPath)).thenReturn(outputStream);

        final Ds3TypeMapperParser typeParser = new Ds3TypeMapperParserImpl();
        final Ds3TypeMapper typeMapper = typeParser.getMap();

        final Ds3SpecParser parser = new Ds3SpecParserImpl();
        final Ds3ApiSpec spec = parser.getSpec(JavaCodeGenerator_Test.class.getResourceAsStream("/input/verifyPhysicalPlacementRequest.xml"));
        final CodeGenerator codeGenerator = new JavaCodeGenerator();

        codeGenerator.generate(spec, typeMapper, fileUtils, Paths.get("."));

        final String generatedCode = new String(outputStream.toByteArray());
        LOG.info("Generated code:\n" + generatedCode);

        final String requestName = "VerifyPhysicalPlacementForObjectsRequestHandler";
        assertTrue(extendsClass(requestName, "AbstractRequest", generatedCode));
        assertTrue(isReqParamOfType("Operation", "RestOperationType", requestName, generatedCode, false));
        assertTrue(isOptParamOfType("FullDetails", "boolean", requestName, generatedCode, false));
        assertTrue(isOptParamOfType("StorageDomainId", "UUID", requestName, generatedCode, false));

        assertTrue(hasImport("com.spectralogic.s3.server.request.rest.RestOperationType", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.HttpVerb", generatedCode));
        assertTrue(hasImport("java.util.UUID", generatedCode));
    }

    @Test
    public void multiFileDeleteHandler() throws IOException, ParserException {
        final FileUtils fileUtils = mock(FileUtils.class);
        final Path requestPath = Paths.get("./ds3-sdk/src/main/java/com/spectralogic/ds3client/commands/DeleteObjectsRequestHandler.java");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);

        when(fileUtils.getOutputFile(requestPath)).thenReturn(outputStream);

        final Ds3TypeMapperParser typeParser = new Ds3TypeMapperParserImpl();
        final Ds3TypeMapper typeMapper = typeParser.getMap();

        final Ds3SpecParser parser = new Ds3SpecParserImpl();
        final Ds3ApiSpec spec = parser.getSpec(JavaCodeGenerator_Test.class.getResourceAsStream("/input/multiFileDeleteRequestHandler.xml"));
        final CodeGenerator codeGenerator = new JavaCodeGenerator();

        codeGenerator.generate(spec, typeMapper, fileUtils, Paths.get("."));

        final String generatedCode = new String(outputStream.toByteArray());
        LOG.info("Generated code:\n" + generatedCode);

        final String requestName = "DeleteObjectsRequestHandler";
        assertTrue(extendsClass(requestName, "AbstractRequest", generatedCode));
        assertTrue(isOptParamOfType("RollBack", "boolean", requestName, generatedCode, false));
        assertTrue(isOptParamOfType("Quiet", "boolean", requestName, generatedCode, false));
        assertTrue(isReqParamOfType("Objects", "List<String>", requestName, generatedCode, false));
        assertFalse(isReqParamOfType("Delete", "boolean", requestName, generatedCode, false));

        assertTrue(hasImport("com.spectralogic.ds3client.HttpVerb", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.models.Contents", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.models.delete.Delete", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.models.delete.DeleteObject", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.serializer.XmlOutput", generatedCode));
        assertTrue(hasImport("java.io.ByteArrayInputStream", generatedCode));
        assertTrue(hasImport("java.io.InputStream", generatedCode));
        assertTrue(hasImport("java.util.ArrayList", generatedCode));
        assertTrue(hasImport("java.util.List", generatedCode));
    }

    @Test
    public void createObjectHandlers() throws IOException, ParserException {
        final FileUtils fileUtils = mock(FileUtils.class);
        final Path requestPath = Paths.get("./ds3-sdk/src/main/java/com/spectralogic/ds3client/commands/CreateObjectRequestHandler.java");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);

        when(fileUtils.getOutputFile(requestPath)).thenReturn(outputStream);

        final Ds3TypeMapperParser typeParser = new Ds3TypeMapperParserImpl();
        final Ds3TypeMapper typeMapper = typeParser.getMap();

        final Ds3SpecParser parser = new Ds3SpecParserImpl();
        final Ds3ApiSpec spec = parser.getSpec(JavaCodeGenerator_Test.class.getResourceAsStream("/input/createObjectRequestHandler.xml"));
        final CodeGenerator codeGenerator = new JavaCodeGenerator();

        codeGenerator.generate(spec, typeMapper, fileUtils, Paths.get("."));

        final String generatedCode = new String(outputStream.toByteArray());
        LOG.info("Generated code:\n" + generatedCode);

        final String requestName = "CreateObjectRequestHandler";
        assertTrue(extendsClass(requestName, "AbstractRequest", generatedCode));
        assertTrue(isOptParamOfType("Job", "UUID", requestName, generatedCode, false));
        assertTrue(isOptParamOfType("Offset", "long", requestName, generatedCode, false));
        assertTrue(isReqParamOfType("BucketName", "String", requestName, generatedCode, false));
        assertTrue(isReqParamOfType("ObjectName", "String", requestName, generatedCode, false));
        assertTrue(isReqParamOfType("Channel", "SeekableByteChannel", requestName, generatedCode, false));
        assertTrue(isReqParamOfType("Size", "long", requestName, generatedCode, false));

        assertTrue(hasImport("com.spectralogic.ds3client.HttpVerb", generatedCode));
        assertTrue(hasImport("com.spectralogic.ds3client.models.Checksum", generatedCode));
        assertTrue(hasImport("java.io.InputStream", generatedCode));
        assertTrue(hasImport("java.nio.channels.SeekableByteChannel", generatedCode));
        assertTrue(hasImport("java.util.UUID", generatedCode));
    }

    @Test
    public void getObjectHandlers() throws IOException, ParserException {
        final FileUtils fileUtils = mock(FileUtils.class);
        final Path requestPath = Paths.get("./ds3-sdk/src/main/java/com/spectralogic/ds3client/commands/GetObjectRequestHandler.java");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);

        when(fileUtils.getOutputFile(requestPath)).thenReturn(outputStream);

        final Ds3TypeMapperParser typeParser = new Ds3TypeMapperParserImpl();
        final Ds3TypeMapper typeMapper = typeParser.getMap();

        final Ds3SpecParser parser = new Ds3SpecParserImpl();
        final Ds3ApiSpec spec = parser.getSpec(JavaCodeGenerator_Test.class.getResourceAsStream("/input/getObjectRequestHandler.xml"));
        final CodeGenerator codeGenerator = new JavaCodeGenerator();

        codeGenerator.generate(spec, typeMapper, fileUtils, Paths.get("."));

        final String generatedCode = new String(outputStream.toByteArray());
        LOG.info("Generated code:\n" + generatedCode);

        final String requestName = "GetObjectRequestHandler";
        assertTrue(extendsClass(requestName, "AbstractRequest", generatedCode));
        assertTrue(hasStaticMethod("buildRangeHeaderText", "String", TestHelper.Scope.PRIVATE, generatedCode));
        assertTrue(isOptParamOfType("Job", "UUID", requestName, generatedCode, false));
        assertTrue(isOptParamOfType("Offset", "long", requestName, generatedCode, false));
        assertTrue(isOptParamOfType("ByteRange", "Range", requestName, generatedCode, false));
        assertTrue(isReqParamOfType("BucketName", "String", requestName, generatedCode, false));
        assertTrue(isReqParamOfType("ObjectName", "String", requestName, generatedCode, false));
        assertTrue(isReqParamOfType("Channel", "WritableByteChannel", requestName, generatedCode, false));

        assertTrue(hasImport("com.spectralogic.ds3client.HttpVerb", generatedCode));
        assertTrue(hasImport("org.apache.http.entity.ContentType", generatedCode));
        assertTrue(hasImport("java.nio.channels.WritableByteChannel", generatedCode));
        assertTrue(hasImport("java.util.UUID", generatedCode));
    }

    @Test
    public void wholeXmlDoc() throws IOException, ParserException {
        final FileUtils fileUtils = new TestFileUtilImpl(tempFolder);
        final Ds3SpecParser parser = new Ds3SpecParserImpl();
        final Ds3ApiSpec spec = parser.getSpec(JavaCodeGenerator_Test.class.getResourceAsStream("/input/fullXml.xml"));
        final CodeGenerator codeGenerator = new JavaCodeGenerator();

        final Ds3TypeMapperParser typeParser = new Ds3TypeMapperParserImpl();
        final Ds3TypeMapper typeMapper = typeParser.getMap();

        codeGenerator.generate(spec, typeMapper, fileUtils, Paths.get("."));

    }
}
