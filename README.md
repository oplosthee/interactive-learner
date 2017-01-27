# interactive-learner
An interactive system that learns to classify documents from feedback given by the user. Project for the sixth module of the Computer Engineering course at the University of Twente.

---------

### System Requirements
#### Compilation
- Newest Oracle JDK for compatability with JavaFX (OpenJDK does not support JavaFX)
- Instructions: run `gradlew jar` in order to build a runnable jar.

#### Usage
- 2GB RAM is recommended, but depending on the size of the corpus RAM usage might vary (the provided mail corpus requires 1GB RAM).
- Same Java requirements for compilation hold.

---------

### Installation/Usage

- The application can be started by executing the jar file.
- The application expects the training corpus to be divided into classes through folders, e.g. blog/{tech,cooking,knitting}/blog1.txt or mail/{spam,ham}/mail1.txt. Folder names will be used for class names. Location of this folder can be selected through the GUI.
- The training corpus does not need to contain any files aside from the structure. This allows for the creation of a newly annotated corpus.
- Test documents can be selected through the GUI, the location of these files does not matter.
- Location of the file containing stop words/articles can be selected through the GUI. Each word is expected to be on a new line.
- Options for classifying can be selected in the initial screen, such as whether to remove stop words/articles.
- After selecting a training folder and setting up the classifier, the train button can be pressed to start the training progress.
- Pressing the "Go to classifier" button will open up a new menu in which the user can select files for classification.
- After classifying a new file the user is asked whether the predicted classification is correct, and the user can correct the classification if necessary. This can be used for creating a new corpus.
- After classification the classifier will be retrained with the newly acquired information.