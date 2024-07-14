package com.pst.ag.dsv_converter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pst.ag.dsv_converter.utils.Utils;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.TypeReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FileConverterServiceTest {

    private static FileConverterService fileConverterService;
    private static ObjectMapper mapper;

    @BeforeAll
    public static void setUp(){
        fileConverterService = new FileConverterService();
        mapper = new ObjectMapper();
    }

    @Test
    public void service_convert_happypath() throws IOException {
        String inputFile = "src/test/resources/inputs/input.txt";
        String outputFilePath = "src/test/resources/outputs/output";
        String finalOutput = outputFilePath + "-" + Utils.getCurrentDate() + ".json";
        String[] args = {"inputs=" + inputFile,
                "output-filename=" + outputFilePath,
                "show-progress=true",
                "custom-delimiter=COMMA"};
        fileConverterService.execute(args);
        File outputJsonFile = new File(finalOutput);
        assertNotNull(outputJsonFile);

        JsonNode jsonNode = mapper.readTree(Files.readString(Path.of(finalOutput)));
        assertNotNull(jsonNode.get(0).get("middleName"));
        assertNull(jsonNode.get(1).get("middleName"));
        assertEquals(jsonNode.size(), 3);
    }

    @Test
    public void service_convert_tab_delimited() throws IOException {
        String inputFile = "src/test/resources/inputs/input-1.txt";
        String outputFilePath = "src/test/resources/outputs/output-tab";
        String finalOutput = outputFilePath + "-" + Utils.getCurrentDate() + ".json";
        String[] args = {"inputs=" + inputFile,
                "output-filename=" + outputFilePath,
                "custom-delimiter=TAB"};
        fileConverterService.execute(args);
        File outputJsonFile = new File(finalOutput);
        assertNotNull(outputJsonFile);

        JsonNode jsonNode = mapper.readTree(Files.readString(Path.of(finalOutput)));
        assertNotNull(jsonNode.get(0).get("middleName"));
        assertNull(jsonNode.get(1).get("middleName"));
        assertEquals(jsonNode.size(), 3);
    }

    @Test
    public void service_convert_pipe_delimited() throws IOException {
        String inputFile = "src/test/resources/inputs/input-2.txt";
        String outputFilePath = "src/test/resources/outputs/output";
        String finalOutput = outputFilePath + "-" + Utils.getCurrentDate() + ".json";
        String[] args = {"inputs=" + inputFile,
                "output-filename=" + outputFilePath,
                "show-progress=true",
                "custom-delimiter=PIPE"};
        fileConverterService.execute(args);
        File outputJsonFile = new File(finalOutput);
        assertNotNull(outputJsonFile);

        JsonNode jsonNode = mapper.readTree(Files.readString(Path.of(finalOutput)));
        assertNotNull(jsonNode.get(0).get("middleName"));
        assertNull(jsonNode.get(1).get("middleName"));
        assertEquals(jsonNode.size(), 3);
    }
}
