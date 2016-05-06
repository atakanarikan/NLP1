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

# NLP2
Second project of the semester for the class CMPE561: Natural Language Processing

This is an implementation of the Viterbi Algorithm on MetuBank dataset using Java.

## How to run the code ##
Assuming all files are compiled using: 
    `javac <filename>`

1. Runner.java
--------------
This will run each of the programs one by one. The commandline should look like this:

    java Runner train_data_path train_serialized train_pos_type viterbi_test_path viterbi_output_path viterbi_train_path testgold_prog_input testgold_gold_input testgold_pos_type
    
> Program may crush if the listed arguments aren't given properly.

> The arguments should be consistent. For instance, `train_serialized` and `viterbi_train_path` should have the same value.

2. Oldschool (One by one)
--------------
* DataTrainer: `java DataTrainer train_data_path train_serialized train_pos_type`
* Viterbi: `java Viterbi viterbi_test_path viterbi_output_path viterbi_train_path`
* TestGold: `java TestGold testgold_prog_input testgold_gold_input testgold_pos_type`
 

## Table of argument names ##
| Abbreviation  | Example Value| Explanation  |
| ------------- |:-------------:| :-----: |
|   train_data_path   | "NLP2/train/turkish_metu_sabanci_train.conll" | Path to the training data file |
|   train_serialized  | "NLP2/trained_data.ser" | Path to the serialized version of the training data. |
|   train_pos_type    | "--cpostag" | Type of the PosTag. --postag by default |
|   viterbi_test_path | "NLP2/test/turkish_metu_sabanci_test_blind_sample.conll.txt" | Path to the test data file |
|   viterbi_output_path | "NLP2/viterbi_output.txt" | Path to the output of the Viterbi Algorithm. |
|   viterbi_train_path | "NLP2/trained_data.ser" | Path to the serialized data, output of DataTrainer. |
|   testgold_prog_input | "NLP2/viterbi_output.txt" | Path to the output of the Viterbi Algorithm. |
|   testgold_gold_input | "NLP2/test/turkish_metu_sabanci_test_gold_sample.conll.txt" | Path to the gold standart file. |
|   testgold_pos_type    | "--cpostag" | Type of the PosTag. --postag by default |
