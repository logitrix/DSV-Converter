package com.pst.ag.dsv_converter.dto;


import com.pst.ag.dsv_converter.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Arguments {
    private String inputFiles;
    private String outputName;
    private String absoluteOutputPath;
    private boolean showProgress;
    private boolean moreThanOneInputFile;
    private String customDelimiter;

    public Arguments(Map<String, String> commands) {
        if (commands != null) {
            inputFiles = commands.get(Constants.INPUT_FILES) != null ? commands.get(Constants.INPUT_FILES) : Constants.DEFAULT_INPUT_FILE_NAME ;
            outputName = commands.get(Constants.OUTPUT_FILE_NAME) != null ? commands.get(Constants.OUTPUT_FILE_NAME) : Constants.DEFAULT_OUTPUT_FILE_NAME;
            showProgress = commands.get(Constants.SHOW_PROGRESS) != null ? Boolean.valueOf(commands.get(Constants.SHOW_PROGRESS)) : false;
            customDelimiter = commands.get(Constants.CUSTOM_DELIMITER) != null ? commands.get(Constants.CUSTOM_DELIMITER): Constants.DEFAULT_DELIMITER;
            moreThanOneInputFile = inputFiles.contains(",");
        } else {
            inputFiles = Constants.DEFAULT_INPUT_FILE_NAME ;
            outputName = Constants.DEFAULT_OUTPUT_FILE_NAME;
            absoluteOutputPath = outputName;
            showProgress = false;
            moreThanOneInputFile = false;
            customDelimiter = Constants.DEFAULT_DELIMITER;
        }

    }


}
