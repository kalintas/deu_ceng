// 2022510127_kerem_kalintas.cs

string[] stop_words = {
    "a", "after", "again", "all", "am", "and",
    "any", "are", "as", "at", "be", "been",
    "before", "between", "both", "but",
    "by", "can", "could", "for", "from", "had",
    "has", "he", "her", "here", "him", "in", "into",
    "I", "is", "it", "me", "my", "of", "on", "our",
    "she", "so", "such", "than", "that", "the", "then", "they",
    "this", "to", "until", "we", "was", "were", "with", "you"
};

// Utility functions

// Searches element in the array and returns the index
// Returns -1 if array doesnt contain element
int FindInArray(string[] array, string element) {

    for (int i = 0; i < array.Length; ++i) {
        if (array[i] == element) {
            return i;
        }
    }

    return -1;
}

// Returns true if word is a stop word
bool IsStopWord(string word) {

    return FindInArray(stop_words, word) >= 0;
}

// Returns if c is a delimiter
bool is_delimiter(char c) {
    return c == '.' || c == ',' || c == ';' || c == '?' ||
        c == ' ' || c == '\n' || c == '\t' || c == '\r';
}
// Returns if c is a operator
bool is_operator(char c) {
    return c == '+' || c == '-' || c == '*' || c == '/';
}

// Create a new string array and add the value parameter to end of the array
string[] AppendToStringArray(string[] array, string value) {

    string[] result = new string[array.Length + 1];

    for (int i = 0; i < array.Length; ++i) {
        result[i] = array[i];
    }

    result[array.Length] = value;

    return result;
}

// Create a new int array and add the value parameter to end of the array
int[] AppendToIntArray(int[] array, int value) {

    int[] result = new int[array.Length + 1];

    for (int i = 0; i < array.Length; ++i) {
        result[i] = array[i];
    }

    result[array.Length] = value;

    return result;
}

// Returns words in the line separated by a delimiter
string[] GetWords(string line) {

    string[] words = { };
    int word_start = -1;

    for (int i = 0; i < line.Length; ++i) {

        if (!is_delimiter(line[i])) {

            if (word_start < 0) {
                word_start = i;
            }
        } else if (word_start >= 0) {
            string word = line.Substring(word_start, i - word_start);

            words = AppendToStringArray(words, word);
            word_start = -1;
        }
    }

    return words;
}

// Returns true if word corresponds to the pattern
// The symbol “-“ corresponds to only one letter
bool MatchesToPattern(string word, string pattern) {

    if (word.Length != pattern.Length) {
        return false;
    }

    for (int i = 0; i < word.Length; ++i) {
        if (pattern[i] != '-' && pattern[i] != word[i]) {
            return false;
        }
    }

    return true;
}


// Returns arguments inside the question string in a string array
string[] ParseQuestion(string question, string format) {

    int index = 0;

    string[] result = { };

    for (int i = 0; i < question.Length; ++i) {

        if (format[index] == '{' && format[index + 1] == '}') {

            int start = i;

            while (i < question.Length && format[index + 2] != question[++i]) { }

            string expression = question.Substring(start, i - start);

            result = AppendToStringArray(result, expression);

            index += 2;

        } else if (format[index] != question[i]) {
            return new string[] { };
        }

        index += 1;
    }

    return result;
}

// Program start

string[] corpus_lines = File.ReadAllLines("corpus.txt");

// Words in the corpus.txt file separated by a delimiter
// There is a empty string after every line
string[] corpus_words = { };

for (int i = 0; i < corpus_lines.Length; ++i) {

    string[] words = GetWords(corpus_lines[i]);

    for (int t = 0; t < words.Length; ++t) {
        corpus_words = AppendToStringArray(corpus_words, words[t]);
    }

    // Add a empty string after every line
    corpus_words = AppendToStringArray(corpus_words, "");
}

// Calculate and return result of an expression
// Assume that the expression will include a single type of operator (only + or only - or only * or only /).
// Example:
// expression = "2x3+5678x2+4x1234+21x71+5x2"
// x_value = 1
// return value = 5710
double CalculateExpression(string expression, double x_value) {

    double result = 0.0;

    int operand_start = 0;
    int x_index = 0;

    char operator_type = '\0';

    for (int i = 0; i < expression.Length; ++i) {

        if (expression[i] == 'x' || expression[i] == 'X') {
            x_index = i;

        } else if (is_operator(expression[i]) || i == expression.Length - 1) {

            if (operand_start == 0) {
                operator_type = expression[i];

            } else if (i != expression.Length - 1 && operator_type != expression[i]) {
                // Give an error
                Console.Write("Error: Operators are not same throughout expression. ");
                return 0.0;
            }

            double cofactor = Convert.ToDouble(expression.Substring(operand_start, x_index - operand_start));
            double power = Convert.ToDouble(expression.Substring(x_index + 1, i - x_index - 1));

            double value = cofactor * Math.Pow(x_value, power);

            if (operand_start == 0) {
                // Set first operand to result value
                result = value;
            } else {
                switch (operator_type) {
                    case '+':
                        result += value;
                        break;
                    case '-':
                        result -= value;
                        break;
                    case '*':
                        result *= value;
                        break;
                    case '/':
                        result /= value;
                        break;
                }
            }

            operand_start = i + 1;
        }

    }

    return result;
}

// Returns top 10 words corresponding to pattern in the corpus.txt file
string[] FindTop10(string pattern) {

    pattern = pattern.ToLower();

    string[] matching_words = { };
    int[] word_counts = { };

    for (int i = 0; i < corpus_words.Length; ++i) {

        string word = corpus_words[i].ToLower();

        if (MatchesToPattern(word, pattern)) {

            int index = FindInArray(matching_words, word);

            if (index >= 0) {
                // Array contains word. Increase count
                word_counts[index] += 1;
            } else {
                // Array doesnt contain word. Append it to array
                matching_words = AppendToStringArray(matching_words, word);
                word_counts = AppendToIntArray(word_counts, 0);
            }
        }
    }

    // Selection sort elements based on their frequency
    for (int i = 0; i < word_counts.Length; ++i) {

        int max = i;

        for (int t = i + 1; t < word_counts.Length; ++t) {

            if (word_counts[t] > word_counts[max]) {
                max = t;
            }
        }
        
        // Swap values in the i and max indices
        int old_count = word_counts[i];
        word_counts[i] = word_counts[max];
        word_counts[max] = old_count;

        string old_word = matching_words[i];
        matching_words[i] = matching_words[max];
        matching_words[max] = old_word;
    }

    // Get top10 words from the array if it contains more
    if (matching_words.Length > 10) {
        string[] array = new string[10];
        for (int i = 0; i < array.Length; ++i) {
            array[i] = matching_words[i];
        }

        matching_words = array;
    }

    return matching_words;
}

void AnswerQuestion(string question) {
    
    // RULE 1
    // If the question starts with "What is the result of expression", the program will find the result of
    // mathematical expression and print it on the screen.
    string[] args = ParseQuestion(question, "What is the result of expression{}for x={}?");

    // Check if it contains expression and x_value arguments
    if (args.Length == 2) {

        string expression = args[0];
        double x_value = Convert.ToDouble(args[1]);

        double result = CalculateExpression(expression, x_value);

        Console.WriteLine("The result is {0}", result);

        return;
    }

    // RULE 2
    // If the user asks a question to the computer with the phrase "What are the top10 words in the pattern", the
    // program will find and print the words that corresponds to this pattern.

    args = ParseQuestion(question, "What are the top10 words in the pattern {}?");

    // Check if it contains pattern argument
    if (args.Length == 1) {

        string pattern = args[0];

        string[] top10 = FindTop10(pattern);

        for (int i = 0; i < top10.Length; ++i) {
            Console.Write("{0} ", top10[i]);
        }

        Console.WriteLine();

        return;
    }

    // RULE 3
    // The program should compare the words in the question with the words in the text in the corpus, and find the most
    // similar(probable) answer.The number of common words between question and text is the fundamental criteria.If two or
    // more texts have the same maximum count, the program will print all of them. 

    string[] question_words = GetWords(question);

    int most_similarity = 0;
    int[] similar_indices = { };

    int corpus_index = 0;

    for (int i = 0; i < corpus_lines.Length; ++i) {

        string[] common_words = { };
        
        while (corpus_index < corpus_words.Length) {

            string current_word = corpus_words[corpus_index];

            if (!IsStopWord(current_word)) {

                int index = FindInArray(question_words, current_word);
                
                if (index >= 0) {
                    // question_words contains current_word

                    index = FindInArray(common_words, current_word);

                    // Add current_word to common_words if it doesnt contain it
                    if (index < 0) {
                        common_words = AppendToStringArray(common_words, current_word);
                    }
                }
            }

            corpus_index += 1;
            if (corpus_words[corpus_index] == "") {
                // end of the line
                corpus_index += 1;
                break;
            }
        }

        // To answer a question, at least two words should match, except stop words.
        if (common_words.Length < 2) {
            continue;
        }

        if (common_words.Length > most_similarity) {

            most_similarity = common_words.Length;
            similar_indices = new int[]{ i };

        } else if (common_words.Length == most_similarity) {
            similar_indices = AppendToIntArray(similar_indices, i);
        }
    }

    if (most_similarity == 0) {
        Console.WriteLine("No answer.");
    }
    else {

        for (int i = 0; i < similar_indices.Length; ++i) {

            Console.WriteLine(corpus_lines[similar_indices[i]]);
        }
    }
}

string[] questions = File.ReadAllLines("questions.txt");

for (int i = 0; i < questions.Length; ++i) {
    AnswerQuestion(questions[i]);
}

Console.ReadLine();