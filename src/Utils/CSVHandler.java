/**
 * The {@code CSVHandler} class provides utility methods for reading from and writing to CSV files.
 * It includes methods to read the content of a CSV file and store it as a list of lists, 
 * as well as write data to a CSV file, with support for including headers.
 * This class assumes that the CSV file is comma-delimited and that lines are separated by newlines.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Utils;

import java.io.*;
import java.util.*;


public class CSVHandler {
    
    /**
     * The delimiter used to separate values in a CSV file (comma).
     */
    private static final String COMMA_DELIMITER = ",";

    /**
     * Reads a CSV file and returns its content as a list of lists of strings.
     * Each inner list represents a row from the CSV file, with each element being a column value.
     * 
     * @param filePath The path to the CSV file to read.
     * @return A list of lists of strings, where each list represents a row in the CSV file.
     *         Each element within a row list is a column value.
     *         Returns an empty list if the file doesn't exist or there is an error reading the file.
     */
    public static List<List<String>> readCSVLines(String filePath) {
        List<List<String>> records = new ArrayList<>();
        File file = new File(filePath);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(COMMA_DELIMITER);
                    records.add(Arrays.asList(values));
                }
            } catch (IOException e) {
                System.err.println("Error reading the CSV file: " + e.getMessage());
            }
        } else {
            System.out.println("File does not exist: " + filePath);
        }
        return records;
    }

    /**
     * Writes data to a CSV file. If headers are provided, they will be written as the first row,
     * followed by the lines of data.
     * The method overwrites the file if it already exists.
     * 
     * @param headers An array of strings representing the header row for the CSV file.
     *                Can be {@code null} or empty if no header is needed.
     * @param lines An array of strings, where each element represents a row to be written to the CSV file.
     *              Each string is expected to contain the column values for a row, delimited by commas.
     * @param filePath The path to the CSV file to write the data to.
     */
    public static void writeCSVLines(String[] headers, String[] lines, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) { // false = overwrite
            if (headers != null && headers.length > 0) {
                writer.write(String.join(",", headers));
                writer.newLine();
            }
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
        }
    }
}
