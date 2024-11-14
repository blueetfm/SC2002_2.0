package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CSVHandler {

    private String[] readCSVLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath)).toArray(new String[0]);
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return new String[0];
        }
    }

    private void writeCSVLines(String[] headers, String[] lines, String filePath) {
        File file = new File(filePath);
        boolean headersAlreadyPresent = false;

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String firstLine = reader.readLine();
                if (firstLine != null) {
                    headersAlreadyPresent = true;
                }
            } catch (IOException e) {
                System.err.println("Error reading the CSV file: " + e.getMessage());
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {

            if (!headersAlreadyPresent && headers != null && headers.length > 0) {
                writer.write(String.join(",", headers));
                writer.newLine();
            }

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }

            System.out.println("CSV file updated successfully at " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
        }
    }
}
