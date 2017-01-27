package interactivelearner.featureselection;

import interactivelearner.data.Document;

import java.util.List;

public interface DocumentTransformer {

    List<Document> run(List<Document> documents);

}
