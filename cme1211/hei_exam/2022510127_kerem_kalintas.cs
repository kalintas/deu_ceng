using System;

// Constants

// The correct answers for the exam are stored in the array, named key, as follows: 
char[] key = {
    'A', 'B', 'D', 'C', 'C',
    'C', 'A', 'D', 'B', 'C',
    'D', 'B', 'A', 'C', 'B',
    'A', 'C', 'D', 'C', 'D',
    'A', 'D', 'B', 'C', 'D'
};

const int MAX_CANDIDATE = 40;
const int MAX_DEPT = 10;
const int MAX_QUOTA = 8;

// Utility functions

int GetLongestLength(string[] names, int count) {

    int result = names[0].Length;

    for (int i = 1; i < count; ++i) {

        if (names[i].Length > result) {
            result = names[i].Length;
        }
    }

    return result;
}

void PrintSpace(int length) {

    for (int i = 0; i < length; ++i) {
        Console.Write(' ');
    }
}

// Variables for candidates
int[] noes = new int[MAX_CANDIDATE];
string[] names = new string[MAX_CANDIDATE];
int[] diploma_grades = new int[MAX_CANDIDATE];
string[,] dept_choices = new string[MAX_CANDIDATE, 3];
int[] grades = new int[MAX_CANDIDATE];

// Candidate count. candidates <= MAX_CANDIDATE
int candidates = 0;

// The candidate information is stored in a text file, named candidates.txt, in the following format:
// no,name surname,diploma-grade,dept-choice1,dept-choice2,dept-choice3,answer1,answer2,answer3,…,answer25
string[] lines = File.ReadAllLines("candidates.txt");


for (int line = 0; line < lines.Length; ++line) {

    if (candidates == MAX_CANDIDATE) {
        Console.WriteLine("ERROR: Maximum value for number of candidates is {0}", MAX_CANDIDATE);
        break;
    }

    // Split creates string arrays from lines
    // elements array contains strings between commas
    string[] elements = lines[line].Split(',');
    int index = 0;

    try {

        noes[candidates] = Convert.ToInt32(elements[index++]);
        names[candidates] = elements[index++];
        diploma_grades[candidates] = Convert.ToInt32(elements[index++]);

        for (int i = 0; i < 3; ++i) {

            dept_choices[candidates, i] = elements[index++];
        }

        int grade = 0;

        for (int i = 0; i < key.Length; ++i) {

            char answer = elements[index++][0];

            if (answer == key[i]) {
                // right answer
                grade += 4;
            } else if (answer == ' ') {
                // empty answer
            } else {
                // wrong answer
                grade -= 3;
            }
        }

        grades[candidates] = grade;

        // Throw an exception if there is more elements
        if (index != elements.Length) {
            throw new Exception("Answer count must be 25");
        }

        // There is no errors. Add this candidate
        candidates += 1;

    } catch (Exception e) {
        // Print error message

        Console.Write("Error: {0} -> ", e.Message);

        if (elements.Length > 31) {

            for (int i = index; i < elements.Length; ++i) {
                Console.Write("{0},", elements[i]);
            }
        } else {
            Console.Write(elements[index - 1]);
        }

        Console.WriteLine("\nLine: {0}", lines[line]);
    }
}

// Print the grades of all candidates
int longest = GetLongestLength(names, candidates);

Console.Write("Number   Name & Surname");
PrintSpace(longest - 8);
Console.WriteLine("Grade");

for (int i = 0; i < candidates; ++i) {

    Console.Write("{0}      {1}      ", noes[i], names[i]);

    PrintSpace(longest - names[i].Length);
    Console.WriteLine("{0}", grades[i]);
}

// Variables for departments
string[] dept_noes = new string[MAX_DEPT];
string[] dept_names = new string[MAX_DEPT];
int[] quotas = new int[MAX_DEPT];

// Department count. dept_count <= MAX_DEPT
int dept_count = 0;

// The quota information related to the departments is read from a text file,
// named departments.txt, in the following format:
// dept-no,dept-name,quota
lines = File.ReadAllLines("departments.txt");

for (int line = 0; line < lines.Length; ++line) {

    if (dept_count == MAX_DEPT) {
        Console.WriteLine("ERROR: Maximum value for number of departments is {0}", MAX_DEPT);
        break;
    }

    // Split creates string arrays from lines
    // elements array contains strings between commas
    string[] elements = lines[line].Split(',');
    int index = 0;

    try {

        dept_noes[dept_count] = elements[index++];
        dept_names[dept_count] = elements[index++];

        int quota = Convert.ToInt32(elements[index++]);

        // Check quota value
        if (quota < 0 || quota > MAX_QUOTA) {
            throw new Exception("The maximum quota for any department is 8");
        }

        quotas[dept_count] = quota;

        // Throw an exception if there is more elements
        if (index != elements.Length) {
            throw new Exception("Incorrect format for departments");
        }

        dept_count += 1;

    } catch (Exception e) {
        // Print error message

        Console.Write("Error: {0} -> ", e.Message);
        if (elements.Length > 3) {

            for (int i = index; i < elements.Length; ++i) {
                Console.Write("{0},", elements[i]);
            }
        } else {
            Console.Write(elements[index - 1]);
        }

        Console.WriteLine("\nLine: {0}", lines[line]);
    }
}

// Create a new int array called indices
// for sorting candidates based on their grades
int[] indices = new int[candidates];

// Set to default 
for (int i = 0; i < candidates; ++i) {
    indices[i] = i;
}

Random random = new Random();

// Selection sort candidates based on their grades
for (int i = 0; i < candidates; ++i) {

    int max = i;

    // Find the most successful candidate from unsorted part
    for (int t = i + 1; t < candidates; ++t) {
        int lhs = indices[t];
        int rhs = indices[max];

        if (grades[lhs] > grades[rhs]) {
            // Grade is bigger so new max is t
            max = t;
        } else if (grades[lhs] == grades[rhs]) {
            // Grades are equal check diploma grade

            if (diploma_grades[lhs] > diploma_grades[rhs]) {
                
                // Diploma grade is bigger so new max is t
                max = t;
            } else if (diploma_grades[lhs] == diploma_grades[rhs]) {
                // Diploma grade is equal too
                // Select one of them randomly
                if (random.Next(2) == 0) {
                    max = t;
                }
            }

        }
    }

    // Swap most successful with current
    int old_index = indices[i];
    indices[i] = indices[max];
    indices[max] = old_index;
}

// Array for students enrolled to departments
// Each department can have MAX_QUOTA students
int[,] students = new int[MAX_DEPT, MAX_QUOTA];

// Array for storing count of students in departments
int[] student_counts = new int[MAX_DEPT];

// Set student counts to zero
for (int i = 0; i < student_counts.Length; ++i) {
    student_counts[i] = 0;
}

// Function for placing candidates based on their choices
void PlaceCandidates(int candidate) {

    // Candidates are required to get a minimum score of 40
    // on the exam for enrolling in an undergraduate program. 
    if (grades[candidate] < 40) {
        return;
    }
    
    for (int i = 0; i < 3; ++i) {

        string choice = dept_choices[candidate, i];

        // Search candidates choice in the departments
        for (int dept = 0; dept < dept_count; ++dept) {
            
            // check if department noes are same and if there is quota left
            if (choice == dept_noes[dept] && quotas[dept] > 0) {

                students[dept, student_counts[dept]] = noes[candidate]; // place student at the end
                quotas[dept] -= 1; // decrease quota for that department
                student_counts[dept] += 1; // increase student count in that department
                return;
            }
        }
    }
}

// Place every candidate
for (int i = 0; i < candidates; ++i) {

    PlaceCandidates(indices[i]);
}

// Print departments and placed students
longest = GetLongestLength(dept_names, dept_count);

Console.WriteLine();
Console.Write("No      Department");
PrintSpace(longest - 4);
Console.WriteLine("Students");

for (int dept = 0; dept < dept_count; ++dept) {
    Console.Write("{0}      {1}      ", dept_noes[dept], dept_names[dept]);

    PrintSpace(longest - dept_names[dept].Length);

    for (int i = 0; i < student_counts[dept]; ++i) {
        Console.Write("{0} ", students[dept, i]);
    }

    Console.WriteLine();
}

Console.ReadLine();