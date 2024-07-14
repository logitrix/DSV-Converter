Development: Sunday, July 14, 2024 - 10am

## Tools
Java 17

Apache Maven 3.8.7

## Library

jackson-databind

commons-csv


## Usage

```bash
# After cloning the repo, the code can be build using the following command
'mvn clean install' or 'mvn package'
        
# A runnable jar file was also included, incase maven tool is not available dsv-converter-1.jar
# Converting a CSV or DSV file using the jar in Java 17
'java -jar dsv-converter-1.jar '

# following argument can be added in the command

'inputs'
# Path and name of the dsv/csv file
# If agrument not added, automatically will find 'input.txt'
# input file can be multiple and comma delimited
'java -jar dsv-converter-1.jar inputs=input-1.txt,input-2.txt'

'output-filename'
# Path and name to create the json output file
# If agrument not added, automatically will name file in this format 'output-YYYY-MM-dd.json'
'java -jar dsv-converter-1.jar output-filename=sample_directory/json-output-file'

'show-progress'
# Can show how much json data added in the output file.
# If agrument not added, automatically will be false
'java -jar dsv-converter-1.jar show-progress=true'

'custom-delimiter'
# Change delimiter. TAB, PIPE or COMMA
# If agrument not added, COMMA is default
'java -jar dsv-converter-1.jar custom-delimiter=TAB'
```

## Limitations
-All values converter into json data will always be String

-Data fields will always be based on dsv/csv header

-Delimited values in a line beyond the number of header, are automatically ignored.

-Output file is always .json extension name


