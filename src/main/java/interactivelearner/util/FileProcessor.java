package interactivelearner.util;

import interactivelearner.data.Document;
import interactivelearner.featureselection.Tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileProcessor {

    /**
     * Returns the contents of a single file located at a path as a String
     *
     * @param path the location of the file to read
     * @return a String containing the contents of the file
     * @throws IOException if anything went wrong during file reading
     */
    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    /**
     * Returns the contents of all files located at a path as a List of String
     *
     * @param path the folder containing the files to be read
     * @return a List containing the contents of all files
     * @throws IOException if anything went wrong during file reading
     */
    public static List<String> readFolder(String path) throws IOException {
        return Files.list(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(FileProcessor::readLines)
                .collect(Collectors.toList());
    }

    /**
     * Returns a Document created from the tokenized contents of the file located at a path
     *
     * @param path the location of the file to read and tokenize
     * @return a Document containing the tokenized contents of a file
     * @throws IOException if anything went wrong during file reading
     */
    public static Document tokenizeFile(String path) throws IOException {
        return Tokenizer.processString(readFile(path));
    }

    /**
     * Returns a List of Document created from the tokenized contents of the files located at a path
     *
     * @param path the location of the files to read and tokenize
     * @return a List of Document containing the tokenized contents of a file
     * @throws IOException if anything went wrong during file reading
     */
    public static List<Document> tokenizeFolder(String path) throws IOException {
        List<String> files = readFolder(path);
        return files.stream()
                .map(Tokenizer::processString)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to read all lines of a file to a single String
     *
     * @param path the Path of the file to read
     * @return  String containing the contents of the file
     */
    private static String readLines(Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException ignored) {
            // Swallow the exception, if it fails to read it is not a File we are interested in.
            return null;
        }
    }
}
