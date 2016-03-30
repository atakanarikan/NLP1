# NLP1
A project for the lecture CMPE561: Natural Language Processing

The code uses a mixture of average word length feature and Multinomial Naive Bayes Algorithm to classify given text document among 69 authors.

The data is gathered from: http://www.kemik.yildiz.edu.tr/?id=28.

## How to run the code ##
compile each of the java classes by javac ArrangeInput.java and javac Main.java first.

### ArrangeInput.java ###
type the following command in the commandline: 

    java ArrangeInput input_folder_path   output_folder_path
the algorithm will scan the input folder and will create the folder and the files under output_folder_path. Then will divide the context into: output_folder_path/test and output_folder_path/training. 60% of the test documents for an author will be in the output_folder_path/training/authorname and its the same for test, only the ratio is 40%folder.

#### Main.java ###
type the following command in the commandline: 

    java Main training_folder_path test_folder_path

