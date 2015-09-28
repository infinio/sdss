package com.bitwisekaizen.sdss.agent.service;

import com.bitwisekaizen.sdss.agentclient.AccessibleIscsiTarget;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static com.bitwisekaizen.sdss.agentclient.AccessibleIscsiTargetBuilder.anAccessibleIscsiTarget;
import static com.bitwisekaizen.sdss.agentclient.IscsiTargetBuilder.anIscsiTarget;
import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NodeFileGeneratorTest {

    private IscsiChefNodeFileGenerator nodeFileGenerator;

    @BeforeMethod
    public void setup() {
        nodeFileGenerator = new IscsiChefNodeFileGenerator();
    }

    @Test
    public void canGenerateFiles() throws Exception {
        AccessibleIscsiTarget target01 = anAccessibleIscsiTarget().withIscsiTarget(
                anIscsiTarget().withTargetName("target01").withCapacityInMb(10).withIqns("iqn01", "iqn02")).build();
        AccessibleIscsiTarget target02 = anAccessibleIscsiTarget().withIscsiTarget(
                anIscsiTarget().withTargetName("target02").withCapacityInMb(20).withIqns("iqn03")).build();
        AccessibleIscsiTarget target03 = anAccessibleIscsiTarget().withIscsiTarget(
                anIscsiTarget().withTargetName("target03").withCapacityInMb(30)).build();

        File createNodeFile = nodeFileGenerator.generateCreateFile(asList(target01, target02, target03));
        File deleteNodeFile = nodeFileGenerator.generateDeleteFile(asList(target01, target02, target03));

        String actualCreateNodeFileText = readFileToString(createNodeFile);
        assertThat(isValidJson(actualCreateNodeFileText), is(true));
        assertThat(getJsonNode(actualCreateNodeFileText), equalTo(getJsonNode(expectedCreateNodeFile())));

        String actualDeleteNodeFileText = readFileToString(deleteNodeFile);
        assertThat(isValidJson(actualDeleteNodeFileText), is(true));
        assertThat(getJsonNode(actualDeleteNodeFileText), equalTo(getJsonNode(expectedDeleteNodeFile())));
    }

    private String expectedCreateNodeFile() throws IOException {
        return IOUtils.toString(getClass().getClassLoader().getResourceAsStream("nodefiles/sample_create_node.json"));
    }

    private String expectedDeleteNodeFile() throws IOException {
        return IOUtils.toString(getClass().getClassLoader().getResourceAsStream("nodefiles/sample_delete_node.json"));
    }

    public boolean isValidJson(String test) {
        try {
            getJsonNode(test);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private JsonNode getJsonNode(String test) throws IOException {
        return new ObjectMapper().readTree(test);
    }
}