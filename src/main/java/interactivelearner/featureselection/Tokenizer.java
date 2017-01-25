package interactivelearner.featureselection;

import interactivelearner.data.Document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tokenizer {

    public static Document processString(String text) {
        String[] words = text
                .replace("\n", " ")
                .replaceAll("\r", " ")
                .split(" ");

        List<String> result = Arrays.stream(words)
                .map(s -> s.toLowerCase().replaceAll("[^a-zA-Z]", ""))
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        return new Document(result);
    }

}
