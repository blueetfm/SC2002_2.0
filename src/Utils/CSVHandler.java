package Utils;

import java.io.*;
import java.util.*;

public class CSVHandler {
    private static final String COMMA_DELIMITER = ",";

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