package chtijug;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

    private final static Pattern FILENAME_PATTERN = Pattern.compile("slide_([^_]+)_([^_]+)\\.html");


    public static Consumer<? super Path> printLines(PrintWriter writer) {
        return p -> {
            try {
                List<String> lines = Files.readAllLines(p, Charset.forName("UTF-8"));
                writer.print("<section>");
                lines.forEach(line -> writer.print(line + "\n"));
                writer.print("</section>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }


    public static Path getResourcesPath(String folderName) {
        String filePath = Thread.currentThread().getContextClassLoader().getResource(folderName).getFile();
        filePath = convertPath(filePath);
        return Paths.get(filePath);
    }


    public static String convertPath(String filePath) {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            filePath = filePath.replaceAll("/", File.separator + File.separator + File.separator + File.separator);
            filePath = filePath.substring(2);
        }
        return filePath;
    }


    public static Map<String, List<Path>> listSourceFiles(String directory, String filePattern) throws IOException {
        Path dir = getResourcesPath(directory);
        Map<String, List<Path>> result = new TreeMap<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filePattern)) {
            for (Path entry : stream) {
                Matcher matcher = FILENAME_PATTERN.matcher(entry.getFileName().toString());
                matcher.find();
                String cle = matcher.group(1);
                if (!result.containsKey(cle)) {
                    result.put(cle, new ArrayList<>());
                }
                result.get(cle).add(entry);
            }
        } catch (DirectoryIteratorException ex) {
            throw ex.getCause();
        }
        return result;
    }
}
