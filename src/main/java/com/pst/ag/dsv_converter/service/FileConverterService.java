package com.pst.ag.dsv_converter.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.pst.ag.dsv_converter.dto.Arguments;
import com.pst.ag.dsv_converter.utils.Constants;
import com.pst.ag.dsv_converter.utils.DELIMITER;
import com.pst.ag.dsv_converter.utils.Utils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class FileConverterService {

    private Logger logger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

    public void execute(String[] args) {
        Map<String, String> commands = parseArgs(args);
        Arguments arguments = new Arguments(commands);
        String date = Utils.getCurrentDate();
        if (arguments.isMoreThanOneInputFile()) {
            AtomicInteger index = new AtomicInteger();
            Arrays.asList(arguments.getInputFiles().split(","))
                    .forEach(inputFile -> convertAFile(inputFile, arguments.getOutputName(),
                            arguments.isShowProgress(), index.getAndIncrement(), date, arguments.getCustomDelimiter()));
        } else {
            convertAFile(arguments.getInputFiles(), arguments.getOutputName(),
                    arguments.isShowProgress(), 0, date, arguments.getCustomDelimiter());
        }
    }

    public Map<String, String> parseArgs(String[] args) {
        if (args != null && args.length >= 1) {
            Map<String, String> arguments = new HashMap<>();
            Arrays.asList(args).stream().forEach(arg -> {
                String[] keyValue = arg.split("=");
                if (keyValue.length == 2) {
                    arguments.put(keyValue[0], keyValue[1]);
                } else {
                    logger.info("Invalid argument: " + arg);
                }
            });
            return arguments;
        }
        return null;
    }

    public void convertAFile(String inputFile, String outputFilePath, boolean showProgress, int index, String date, String customDelimiter) {

        logger.info("Starting to read file : {} ", inputFile);

        Reader reader = null;
        FileWriter writer = null;
        try {
            reader = new FileReader(inputFile);
            String absoluteFilePath = outputFilePath + (index > 0? "-" + (index + 1)  + "-" + date + ".json": "-" + date + ".json");
            System.out.println("Writing to file : " + absoluteFilePath);
            writer = new FileWriter(absoluteFilePath);
            JsonFactory jsonFactory = new JsonFactory();
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(writer);
            jsonGenerator.writeStartArray();

            ObjectMapper mapper = new ObjectMapper();
            List<String> csvHeaders = new ArrayList<>();

            CSVFormat csvFormat = CSVFormat.DEFAULT;
            if (customDelimiter.equalsIgnoreCase(DELIMITER.TAB.toString())) {
                csvFormat = CSVFormat.TDF;
            } else if (customDelimiter.equalsIgnoreCase(DELIMITER.PIPE.toString())) {
                csvFormat = CSVFormat.newFormat('|');
            }

            CSVParser csvParser =   csvFormat.withFirstRecordAsHeader().parse(reader);;
            csvParser.forEach(csvLine ->{
                if (csvLine != null) {
                    try {

                        Map<String, String> jsonObject = null;
                        jsonGenerator.writeStartObject();
                        // Convert each field to JSON key-value pair
                        for (int i = 0; i < csvParser.getHeaderNames().size(); i++) {
                            String header = csvParser.getHeaderNames().get(i);
                            try {
                                String value = csvLine.get(i);
                                if (value != null && !value.isEmpty()) {
                                    jsonGenerator.writeStringField(header, value.trim());
                                }

                                if (showProgress) {
                                    if (jsonObject == null) {
                                        jsonObject = new HashMap();
                                    }
                                    if (value != null && !value.isEmpty()) {
                                        jsonObject.put(header, value.trim());
                                    }
                                }

                            } catch (ArrayIndexOutOfBoundsException a) {
                                jsonGenerator.writeStringField(header, null);
                            }
                        }

                        if (showProgress) {
                            if (jsonObject != null) {
                                String outJson = mapper.writeValueAsString(jsonObject);
                                logger.info(" Added Object to file : {} ", outJson);
                            }
                        }

                        // End JSON object
                        jsonGenerator.writeEndObject();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            jsonGenerator.writeEndArray();
            jsonGenerator.flush();
            jsonGenerator.close();
            writer.close();
            reader.close();
            logger.info(" Completed converting : {} to {} ", inputFile, absoluteFilePath);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(reader);
        }
    }

}
