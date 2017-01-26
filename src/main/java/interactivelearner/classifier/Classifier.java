package interactivelearner.classifier;

import interactivelearner.data.Category;
import interactivelearner.data.Corpus;
import interactivelearner.data.Document;

public interface Classifier {

    public void train(Corpus corpus);

    public Category classify(Corpus corpus, Document document);

}
