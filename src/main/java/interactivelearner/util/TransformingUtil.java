package interactivelearner.util;

import interactivelearner.data.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformingUtil {

    public static Map<String, Integer> mergeDocumentMaps(List<Document> documents) {
        Map<String, Integer> result = new HashMap<>();

        for(Document document : documents) {
            for (Map.Entry<String, Integer> entry : document.getMap().entrySet()) {
                if (result.get(entry.getKey()) == null) {
                    result.put(entry.getKey(), entry.getValue());
                } else {
                    Integer count = result.get(entry.getKey());
                    count += entry.getValue();
                    result.put(entry.getKey(), count);
                }
            }
        }

        return result;
    }

}
