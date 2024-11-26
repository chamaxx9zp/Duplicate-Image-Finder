import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
//      change this path to folder which contain duplicate images
        String directoryPath = "/sample";
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                Map<String, String> imageHashes = new HashMap<>();
                Map<String, List<String>> duplicateMap = new HashMap<>();
                ImagePHash imagePHash = new ImagePHash();

                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            String hash = imagePHash.getHash(new FileInputStream(file));
                            if (imageHashes.containsKey(hash)) {
                                String originalImage = imageHashes.get(hash);
                                if (duplicateMap.containsKey(originalImage)) {
                                    duplicateMap.get(originalImage).add(file.getName());
                                } else {
                                    List<String> duplicates = new ArrayList<>();
                                    duplicates.add(file.getName());
                                    duplicateMap.put(originalImage, duplicates);
                                }
                            } else {
                                imageHashes.put(hash, file.getName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                writeDuplicatesToCSV(duplicateMap);
            }
        }
    }

    private static void writeDuplicatesToCSV(Map<String, List<String>> duplicateMap) {
        String csvFileName = "duplicate_images.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFileName))) {
            writer.println("Original Image,Duplicate Images");

            for (Map.Entry<String, List<String>> entry : duplicateMap.entrySet()) {
                String originalImage = entry.getKey();
                List<String> duplicates = entry.getValue();
                writer.println(originalImage + "," + String.join(", ", duplicates));
            }

            System.out.println("Duplicate images' names written to " + csvFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
