using System;

// DNA strands
char[] DNA1 = { };
char[] DNA2 = { };
char[] DNA3 = { };

// information strings for all of the operations
string[] OPERATION_INFO = {
    "Load a DNA sequence from a file",
    "Load a DNA sequence from a string",
    "Generate random DNA sequence of a BLOB",
    "Check DNA gene structure",
    "Check DNA of BLOB organism",
    "Produce complement of a DNA sequence",
    "Determine amino acids",
    "Delete codons (delete n codons, starting from mth codon)",
    "Insert codons (insert a codon sequence, starting from mth codon)",
    "Find codons (find a codon sequence, starting from mth codon)",
    "Reverse codons (reverse n codons, starting from mth codon)",
    "Find the number of genes in a DNA strand (BLOB or not)",
    "Find the shortest gene in a DNA strand",
    "Find the longest gene in a DNA strand",
    "Find the most repeated n-nucleotide sequence in a DNA strand (STR - Short Tandem Repeat)",
    "Hydrogen bond statistics for a DNA strand",
    "Simulate BLOB generations using DNA strand 1 and 2 (DNA strand 3 is for the newborn)",
};

// |------------------| input output functions |------------------|

// Reads input from console. Check operation name and give error if its not equal to parameter
string GetOperationInput(string operation_name) {

    // Remove white spaces
    string input = RemoveWhiteSpace(Console.ReadLine());

    if (input.Substring(0, operation_name.Length).ToLower() != operation_name) {
        throw new Exception("Wrong operation name.");
    }

    return input;
}

// Returns a integer input from console
int GetInput(string var_name) {

    Console.Write("{0}: ", var_name);

    return Convert.ToInt32(Console.ReadLine());
}

// Prints DNA strand to console
void PrintStrand(string name, char[] strand) {

    char? gender = GetGenderOfStrand(strand);
    int codon_count = strand.Length / 3;

    // Print name, codon count and gender (if it has a gender)
    Console.Write("{0} (n = {1}, g = {2}):", name, codon_count, gender);

    // Colors to iterate through
    var colors = new ConsoleColor[] { ConsoleColor.Cyan, ConsoleColor.Green, ConsoleColor.DarkMagenta };
    int index = 0;

    var default_color = Console.ForegroundColor;
    Console.ForegroundColor = colors[0];

    for (int i = 0; i < codon_count; ++i) {

        // Print whitespace between codons for readability
        Console.Write(' ');

        char[] codon = GetCodon(strand, i);

        // Change console color
        // before start codon and
        // after stop codon

        if (IsStartCodon(codon)) {
            index = (index + 1) % 3;
            Console.ForegroundColor = colors[index];
        }

        Console.Write(new string(codon));

        if (IsStopCodon(codon)) {
            index = (index + 1) % 3;
            Console.ForegroundColor = colors[index];
        }

    }

    Console.Write(' ');

    for (int i = strand.Length - strand.Length % 3; i < strand.Length; ++i) {
        Console.Write(strand[i]);
    }

    // Reset console color to default color
    Console.ForegroundColor = default_color;
    Console.WriteLine();
}

// |--------------------| utility functions |--------------------|

// Remove a character from string
string RemoveCharacter(string input, char c) {

    int count = 0;

    for (int i = 0; i < input.Length; ++i) {
        if (input[i] != c) {
            count += 1;
        }
    }

    // Create a new char array with a new size
    char[] result = new char[count];
    int index = 0;

    for (int i = 0; i < input.Length; ++i) {
        if (input[i] != c) {
            result[index] = input[i];
            index += 1;
        }
    }

    // result doesnt contain c parameter
    return new string(result);
}

// Remove any white spaces from string
string RemoveWhiteSpace(string input) {


    input = RemoveCharacter(input, ' ');
    input = RemoveCharacter(input, '\t');
    input = RemoveCharacter(input, '\n');
    input = RemoveCharacter(input, '\r');

    return input;
}

// Throw an exception if strand contains
// unvalid nuclietide type
void CheckStrand(char[] strand) {

    for (int i = 0; i < strand.Length; ++i) {

        switch (strand[i]) {
            case 'A':
            case 'T':
            case 'G':
            case 'C':
                break;
            default:
                // Unvalid nucleotide
                throw new Exception("Wrong strand. Unvalid nucleotide type: " + strand[i]);
        }

    }
}

// Return true if codon equals "ATG"
bool IsStartCodon(char[] c) {
    return c[0] == 'A' && c[1] == 'T' && c[2] == 'G';
}

// Return true if codon equals "TAA", "TGA" or "TAG"
bool IsStopCodon(char[] codon) {

    switch (new string(codon)) {
        case "TAA":
        case "TGA":
        case "TAG":
            return true;
        default:
            return false;
    }
}

// Returns dna type from input
// Example: load 1 "ATG AAA TTT TAG"
// 1 is dna type and stands for DNA1 strand
int GetDnaType(string input, int index) {

    int dna_type = Convert.ToInt32(input[index].ToString());

    // dna_type correct values -> 1, 2, 3
    if (dna_type < 0 || dna_type > 3) {
        throw new Exception("Incorrect dna type. Correct values: 1, 2, 3");
    }

    return dna_type;
}

// Copy string contents to strand starting at start parameter
char[] CopyToStrand(char[] strand, string str, int start) {

    for (int i = 0; i < str.Length; ++i) {

        strand[start + i] = str[i];
    }

    return strand;
}

// Copy strand to DNA1, DNA2 or DNA3
void SetDNA(char[] strand, int dna_type) {

    // Check if strand is valid
    CheckStrand(strand);

    switch (dna_type) {
        case 1:
            DNA1 = strand;
            break;
        case 2:
            DNA2 = strand;
            break;
        case 3:
            DNA3 = strand;
            break;
        default:
            // Unvalid parameter
            throw new Exception();
    }
}

// Return codon from strand at index
// Index parameter correct range: [0, (strand.Length / 3) - 1]
char[] GetCodon(char[] strand, int index) {
    
    int i = index * 3;
    return new char[] { strand[i], strand[i + 1], strand[i + 2] };
}

// Copy codon parameter to codon from strand at index
// Index parameter correct values: [0, (strand.Length / 3) - 1]
char[] SetCodon(char[] strand, int index, char[] codon) {
    
    for (int i = 0; i < 3; ++i) {
        strand[index * 3 + i] = codon[i];
    }

    return strand;
}

// Returns char array containing next gene starting from index
// Returns null if there is no gene after index
// Index correct values: [0, (DNA1.Length / 3) - 1]
char[]? GetNextGene(char[] strand, ref int index) {
    
    int start = -1;

    for (; index < strand.Length / 3; ++index) {

        var codon = GetCodon(strand, index);

        if (IsStartCodon(codon)) {

            start = index;
        } else if (IsStopCodon(codon)) {

            if (start >= 0) {

                index += 1;

                return new string(strand).Substring(start * 3, (index - start) * 3).ToCharArray();
                //return strand[(start * 3)..(index * 3)];
            }
        }
    }

    return null;
}

// Creates a new DNA sequence of BLOB
// - Number of genes: [2, 7]
// - Number of codons in a gene: [3, 8] (including start and stop codons)
// - Codons in the genes are random codons (except for start and stop kodons)
// - The first gene(gender gene) is not random. It contains 4 codons
void GenerateRandomDNA(char gender, int dna_type) {

    Random rng = new Random();

    // dont include the first gene. first gene is gender gene
    int gene_count = rng.Next(1, 7); // gets value in range [1, 6] 
    int[] codon_counts = new int[gene_count];
    int total_codons = 0;

    for (int i = 0; i < gene_count; ++i) {
        int random = rng.Next(3, 9);
        codon_counts[i] = random;
        total_codons += random;
    }

    // gender gene has 4 codons
    // each codon has 3 nucleotides
    char[] strand = new char[(total_codons + 4) * 3];

    char[] x_nucleotides = { 'A', 'T' };
    char[] y_nucleotides = { 'G', 'C' };

    strand = CopyToStrand(strand, "ATG", 0);

    if (gender == 'f') {
        // gender is female

        for (int i = 0; i < 2; ++i) {

            char x = x_nucleotides[rng.Next(0, 2)];

            strand = SetCodon(strand, i + 1, new char[] { x, x, x });
        }
    } else if (gender == 'm') {
        // gender is male

        char x = x_nucleotides[rng.Next(0, 2)];
        char y = y_nucleotides[rng.Next(0, 2)];

        strand = SetCodon(strand, 1, new char[] { x, x, x });
        strand = SetCodon(strand, 2, new char[] { y, y, y });

    } else {
        throw new Exception("Wrong kind of gender.");
    }

    strand = CopyToStrand(strand, "TAG", 9);

    char[] nucleotides = { 'A', 'C', 'G', 'T' };

    int index = 12;

    for (int i = 0; i < gene_count; ++i) {

        strand = CopyToStrand(strand, "ATG", index);
        index += 3;

        for (int t = 0; t < codon_counts[i] - 2; ++t) {

            char[] codon = new char[3];
            for (int j = 0; j < 3; ++j) {
                codon[j] = nucleotides[rng.Next(0, 4)];
            }

            if (IsStartCodon(codon) || IsStopCodon(codon)) {
                // ATG or TAA or TGA or TAG
                // change codon to be unique
                codon[0] = 'C';
            }

            strand = SetCodon(strand, index / 3, codon);
            index += 3;
        }

        int random = rng.Next(0, 4);
        string end_codon;

        if (random == 0) {
            end_codon = "TAA";
        } else if (random == 1) {
            end_codon = "TGA";
        } else {
            end_codon = "TAG";
        }

        strand = CopyToStrand(strand, end_codon, index);
        index += 3;
    }

    SetDNA(strand, dna_type);
}

// returns gender of given strand
char? GetGenderOfStrand(char[] strand) {

    if (strand.Length >= 12) {

        // check validity of gender strand
        if (!IsStartCodon(GetCodon(strand, 0)) || !IsStopCodon(GetCodon(strand, 3))) {
            return null;
        }

        var codon1 = new string(GetCodon(strand, 1));
        var codon2 = new string(GetCodon(strand, 2));

        bool first_x = codon1 == "AAA" || codon1 == "TTT";
        bool second_x = codon2 == "AAA" || codon2 == "TTT";

        bool first_y = codon1 == "GGG" || codon1 == "CCC";
        bool second_y = codon2 == "GGG" || codon2 == "CCC";

        if (first_x && second_x) {
            // XX
            return 'f';
        }
        if ((first_x && second_y) || (first_y && second_x)) {
            // XY or YX
            return 'm';
        }
    }

    // gender not defined
    return null;
}

// Procreate a new BLOB from BLOB1 and BLOB2
// - For the gender gene; second codon comes from BLOB1, third codon comes from 
// - For other genes; 1 gene comes from BLOB1 (if any), and 1 gene comes from BLOB2 (if any).
char[] Procreate() {
    
    // create a char array with maximum possible length
    // resulting BLOB.Length <= strand.Length
    char[] strand = new char[DNA1.Length + DNA2.Length - 12];
    
    strand = CopyToStrand(strand, "ATG", 0);
    strand = SetCodon(strand, 1, GetCodon(DNA1, 1));
    strand = SetCodon(strand, 2, GetCodon(DNA2, 2));
    strand = CopyToStrand(strand, "TAG", 9);

    // nucleotide count in the resulting strand
    // gender gene has 12 nucleotides
    int length = 12; 

    int index1 = 4;
    int index2 = 4;

    char[]? gene1, gene2;

    while (true) {

        gene1 = GetNextGene(DNA1, ref index1);

        if (gene1 == null) {

            for (int i = index2 * 3; i < DNA2.Length; ++i) {
                strand[length++] = DNA2[i];
            }
            break;
        }

        for (int i = 0; i < gene1.Length; ++i) {
            strand[length++] = gene1[i];
        }

        GetNextGene(DNA2, ref index2); // skip next gene
        gene2 = GetNextGene(DNA2, ref index2);

        if (gene2 == null) {

            for (int i = index1 * 3; i < DNA1.Length; ++i) {
                strand[length++] = DNA1[i];
            }
            break;
        }

        for (int i = 0; i < gene2.Length; ++i) {
            strand[length++] = gene2[i];
        }

        GetNextGene(DNA1, ref index1); // skip next gene
    }

    char[] BLOB = new char[length];

    for (int i = 0; i < length; ++i) {
        BLOB[i] = strand[i];
    }

    return BLOB;
}

// Returns faulty codon count in DNA3
// If 3 or more consecutive codons contain all 3-hydrogen ("GGG CCC GCG" or "GGC CCG GCC CGC" ...),
// this condition is unhealthy, and those codons are called faulty codons.
int GetFaultyCodonCount() {

    int result = 0;

    int start = -1;

    for (int i = 0; i < DNA3.Length / 3; ++i) {

        var codon = GetCodon(DNA3, i);

        bool all_3hydrogen = true;

        for (int t = 0; t < 3; ++t) {
            if (codon[t] == 'A' || codon[t] == 'T') {
                all_3hydrogen = false;
            }
        }

        if (all_3hydrogen) {

            if (start < 0) {
                start = i;
            }
        } else {

            int length = i - start;

            if (start >= 0 && length >= 3) {

                result += length;
            }

            start = -1;
        }

    }

    return result;
}

// checks DNA structure and returns a string with info
string CheckDNAStructure() {

    if (DNA1.Length % 3 != 0) {
        return "Codon structure error.";
    }

    bool in_gene = false;

    var codon_count = DNA1.Length / 3;

    for (int i = 0; i < codon_count; ++i) {

        var codon = GetCodon(DNA1, i);

        if (IsStartCodon(codon)) {

            if (in_gene) {
                return "Gene structure error.";
            }

            in_gene = true;

        } else if (IsStopCodon(codon)) {
            in_gene = false;
        } else if (in_gene && i == codon_count - 1) {
            return "Gene structure error.";
        }
    }

    return "Gene structure is OK.";
}

// |------------------| operation functions |------------------|

// Load a DNA sequence from a file 
// Command: Load 1 dna1.txt (load dna1.txt file content to DNA strand 1 (main strand)) 
void Operation1() {
    
    string input = GetOperationInput("load");

    int dna_type = GetDnaType(input, 4);

    string text = File.ReadAllText(input[5..]);

    // remove white spaces
    text = RemoveWhiteSpace(text);

    // make it all upper case for storing same type of characters
    var strand = text.ToUpper().ToCharArray();

    SetDNA(strand, dna_type);
}

// Load a DNA sequence from a string 
// Command: Load 1 "ATGACTGATGAGAGATATTGA" (load a string to DNA strand 1 (main strand))
void Operation2() {
    
    string input = GetOperationInput("load");
    // remove " with RemoveCharacter function
    input = RemoveCharacter(input, '\"');
    
    // remove white spaces
    input = RemoveWhiteSpace(input);

    int dna_type = GetDnaType(input, 4);

    var strand = input.Substring(5, input.Length - 5).ToUpper().ToCharArray();

    SetDNA(strand, dna_type);
}

// Generate random DNA sequence of a BLOB
// Command: generate f 1 (Generate random DNA of a female BLOB for DNA strand 1) 
void Operation3() {

    string input = GetOperationInput("generate");

    char gender = input[8];

    int dna_type = GetDnaType(input, 9);

    GenerateRandomDNA(gender, dna_type);
}


// Check DNA gene structure 
void Operation4() {
    
    Console.WriteLine(CheckDNAStructure());
}

// Check DNA of BLOB organism 
void Operation5() {

    char[]? gene;
    int index = 0;

    int gene_count = 0;

    bool is_ok = true;

    while ((gene = GetNextGene(DNA1, ref index)) != null) {

        gene_count += 1;

        if (gene_count == 1) {
            // gender gene
            if (gene.Length != 12 || GetGenderOfStrand(gene) == null) {
                Console.Write("Gender error. ");
                is_ok = false;
            }
        }

        int codon_count = gene.Length / 3;

        if (codon_count < 3 || codon_count > 8) {
            Console.Write("Number of codons error. ");
            is_ok = false;
        }

    }

    if (gene_count < 2 || gene_count > 7) {
        Console.Write("Number of genes error. ");
        is_ok = false;
    }

    if (is_ok) {
        Console.Write("BLOB is ok. ");
    }

    Console.WriteLine();
}

//  Produce complement of a DNA sequence 
void Operation6() {

    char[] complement = new char[DNA1.Length];

    for (int k = 0; k < DNA1.Length; k++) {

        switch (DNA1[k]) {
            case 'A':
                complement[k] = 'T';
                break;
            case 'T':
                complement[k] = 'A';
                break;
            case 'G':
                complement[k] = 'C';
                break;
            case 'C':
                complement[k] = 'G';
                break;
            default:
                throw new Exception();
        }

    }

    PrintStrand("DNA strand", DNA1);
    PrintStrand("Complement", complement);
}


// Determine amino acids 
void Operation7() {
    //Amino acids 

    PrintStrand("DNA strand", DNA1);

    Console.Write("Amino acids:         ");

    for (int i = 0; i < DNA1.Length / 3; ++i) {
        string codon = new string(GetCodon(DNA1, i));

        switch (codon) {
            case "GCT":
            case "GCC":
            case "GCA":
            case "GCG":
                Console.Write("Ala ");
                break;

            case "CGT":
            case "CGC":
            case "CGA":
            case "CGG":
            case "AGA":
            case "AGG":
                Console.Write("Arg ");
                break;

            case "AAT":
            case "AAC":
                Console.Write("Asn ");
                break;

            case "GAT":
            case "GAC":
                Console.Write("Asp ");
                break;

            case "TGT":
            case "TGC":
                Console.Write("Cys ");
                break;

            case "CAA":
            case "CAG":
                Console.Write("Gln ");
                break;

            case "GAA":
            case "GAG":
                Console.Write("Glu ");
                break;

            case "GGT":
            case "GGC":
            case "GGA":
            case "GGG":
                Console.Write("Gly ");
                break;

            case "CAT":
            case "CAC":
                Console.Write("His ");
                break;

            case "ATT":
            case "ATC":
            case "ATA":
                Console.Write("Ile ");
                break;

            case "CTT":
            case "CTC":
            case "CTA":
            case "CTG":
            case "TTA":
            case "TTG":
                Console.Write("Leu ");
                break;

            case "AAA":
            case "AAG":
                Console.Write("Lys ");
                break;

            case "ATG":
                Console.Write("Met ");
                break;

            case "TTT":
            case "TTC":
                Console.Write("Phe ");
                break;

            case "CCT":
            case "CCC":
            case "CCA":
            case "CCG":
                Console.Write("Pro ");
                break;

            case "TCT":
            case "TCC":
            case "TCA":
            case "TCG":
            case "AGT":
            case "AGC":
                Console.Write("Ser ");
                break;

            case "ACT":
            case "ACC":
            case "ACA":
            case "ACG":
                Console.Write("Thr ");
                break;

            case "TGG":
                Console.Write("Trp ");
                break;

            case "TAT":
            case "TAC":
                Console.Write("Tyr ");
                break;

            case "GTT":
            case "GTC":
            case "GTA":
            case "GTG":
                Console.Write("Val ");
                break;

            case "TAA":
            case "TGA":
            case "TAG":
                Console.Write("END ");
                break;

            default:
                throw new Exception();
        }
    }

}

// Delete codons (delete n codons, starting from mth codon) 
void Operation8() {

    int n = GetInput("Number of codons to be deleted");
    int m = GetInput("Starting from codon") - 1;

    PrintStrand("DNA strand (Stage 1)", DNA1);

    if ((m + n) * 3 > DNA1.Length) {
        throw new Exception("Wrong parameters.");
    }

    char[] strand = new char[DNA1.Length - n * 3];

    // copy left side
    for (int i = 0; i < m * 3; ++i) {
        strand[i] = DNA1[i];
    }

    // copy right side
    for (int i = (m + n) * 3; i < DNA1.Length; ++i) {
        strand[i - n * 3] = DNA1[i];
    }

    DNA1 = strand;

    PrintStrand("DNA strand (Stage 2)", DNA1);
}

// Insert codons (insert a codon sequence, starting from mth codon) 
void Operation9() {

    PrintStrand("DNA strand (stage 1)", DNA1);

    Console.Write("Codon sequence: ");

    char[] codons = RemoveWhiteSpace(Console.ReadLine().ToUpper()).ToCharArray();

    CheckStrand(codons);

    int start = (GetInput("Starting from") - 1) * 3;

    if (codons.Length % 3 != 0) {

        throw new Exception("Unvalid codons");
    }

    if (start < 0 || start + codons.Length > DNA1.Length) {
     
        throw new Exception("Please enter a number in the correct range");
    }

    char[] strand = new char[DNA1.Length + codons.Length];

    // copy from DNA1 in range of [0, start)
    for (int i = 0; i < start; ++i) {
        strand[i] = DNA1[i];
    }

    // copy from codons in range of [0, codons.Length)
    for (int i = 0; i < codons.Length; ++i) {
        strand[start + i] = codons[i];
    }

    // copy from DNA1 in range of [start, DNA1.Length)
    for (int i = start; i < DNA1.Length; ++i) {
        strand[codons.Length + i] = DNA1[i];
    }


    DNA1 = strand;

    PrintStrand("DNA strand (stage 2)", DNA1);
}

// Find codons (find a codon sequence, starting from mth codon) 
void Operation10() {

    Console.Write("Codon sequence: ");
    char[] codons = RemoveWhiteSpace(Console.ReadLine().ToUpper()).ToCharArray();

    CheckStrand(codons);

    int m = GetInput("Starting from") - 1;

    Console.Write("Result: ");

    int count = 0;

    for (int i = m; i <= (DNA1.Length - codons.Length) / 3; ++i) {

        bool equal = true;

        for (int t = 0; t < codons.Length / 3; ++t) {

            var lhs = new string(GetCodon(DNA1, i + t));
            var rhs = new string(GetCodon(codons, t));

            if (lhs != rhs) {
                equal = false;
                break;
            }
        }

        if (equal) {

            if (count > 0) {
                Console.Write(", ");
            }

            Console.Write("{0}", i + 1);
            count += 1;
        }
    }

    if (count == 0) {
        Console.Write("-1");
    }

    Console.WriteLine();
}

// Reverse codons (reverse n codons, starting from mth codon) 
void Operation11() {

    int n = GetInput("Number of codons to be reversed");
    int m = GetInput("Starting from codon") - 1;

    if (m < 0 || n < 0 || (m + n) * 3 > DNA1.Length) {
        throw new Exception("Out of bounds parameters for n or m.");
    }

    for (int i = 0; i < n / 2; ++i) {

        int left_index = m + i;
        int right_index = m + n - 1 - i;

        var left_codon = GetCodon(DNA1, left_index);
        var right_codon = GetCodon(DNA1, right_index);

        DNA1 = SetCodon(DNA1, left_index, right_codon);
        DNA1 = SetCodon(DNA1, right_index, left_codon);
    }
}

// Find the number of genes in a DNA strand (BLOB or not) 
void Operation12() {

    char[]? gene;
    int index = 0;
    int gene_count = 0;

    while ((gene = GetNextGene(DNA1, ref index)) != null) {

        gene_count += 1;
    }

    Console.WriteLine("Number of genes: {0}", gene_count);
}

// Find the shortest gene in a DNA strand 
void Operation13() {

    char[]? gene;
    int index = 0;

    int shortest_pos = 0;

    char[]? shortest = GetNextGene(DNA1, ref index);

    if (shortest == null) {
        Console.WriteLine("No valid genes in the strand.");
        return;
    }

    while ((gene = GetNextGene(DNA1, ref index)) != null) {

        if (shortest.Length > gene.Length) {
            shortest = gene;
            shortest_pos = index - gene.Length / 3;
        }
    }

    PrintStrand("Shortest gene", shortest);

    Console.WriteLine("Number of codons in the gene: {0}", shortest.Length / 3);
    Console.WriteLine("Position of the gene: {0}", shortest_pos + 1);
}

// Find the longest gene in a DNA strand 
void Operation14() {

    char[]? gene;
    int index = 0;

    int longest_pos = 0;

    char[]? longest = GetNextGene(DNA1, ref index);

    if (longest == null) {
        Console.WriteLine("No valid genes in the strand.");
        return;
    }

    while ((gene = GetNextGene(DNA1, ref index)) != null) {

        if (gene.Length > longest.Length) {
            longest = gene;
            longest_pos = index - gene.Length / 3;
        }
    }

    PrintStrand("Longest gene", longest);

    Console.WriteLine("Number of codons in the gene: {0}", longest.Length / 3);
    Console.WriteLine("Position of the gene: {0}", longest_pos + 1);
}

// Find the most repeated n-nucleotide sequence in a DNA strand
void Operation15() {

    int count = GetInput("Enter number of nucleotide: ");

    char[] most_repeated = new char[count];
    int biggest_frequency = 0;

    int length = DNA1.Length - count + 1;

    for (int i = 0; i < length; ++i) {

        // get sequence in [i, i + count)
        char[] sequence1 = new char[count];

        for (int j = 0; j < count; ++j) {
            sequence1[j] = DNA1[i + j];
        }

        int frequency = 0;

        for (int t = 0; t < length; ++t) {
            
            // get sequence from [t, t + count)
            char[] sequence2 = new char[count];

            for (int j = 0; j < count; ++j) {
                sequence2[j] = DNA1[t + j];
            }

            // compare two sequence 
            if (new string(sequence1) == new string(sequence2)) {
                frequency += 1;
            }
        }
        
        if (frequency > biggest_frequency) {
            
            // set frequency to biggest frequency
            // and store sequence1
            most_repeated = sequence1;
            biggest_frequency = frequency;
        }

    }

    PrintStrand("Most repeated sequence", most_repeated);

    Console.WriteLine("Frequency: {0}", biggest_frequency);
}

// Hydrogen bond statistics for a DNA strand 
// A-T 2-hydrogen bond
// C-G 3-hydrogen bond
void Operation16() {

    int hydrogen2 = 0;
    int hydrogen3 = 0;

    for (int i = 0; i < DNA1.Length; ++i) {

        switch (DNA1[i]) {
            case 'A':
            case 'T':
                hydrogen2 += 1;
                break;
            case 'C':
            case 'G':
                hydrogen3 += 1;
                break;
            default:
                throw new Exception();
        }
    }

    int hydrogen_bonds = hydrogen2 * 2 + hydrogen3 * 3;

    Console.WriteLine("Number of pairing with 2-hydrogen bonds: " + hydrogen2);
    Console.WriteLine("Number of pairing with 3-hydrogen bonds: " + hydrogen3);
    Console.WriteLine("Number of hydrogen bonds: " + hydrogen_bonds);

}

// Simulate BLOB generations using DNA strand 1 and 2
void Operation17() {

    char? dna1_gender = GetGenderOfStrand(DNA1);
    char? dna2_gender = GetGenderOfStrand(DNA2);

    if (!((dna1_gender == 'm' && dna2_gender == 'f') || (dna1_gender == 'f' && dna2_gender == 'm'))) {
        throw new Exception("Procreation needs 1 male BLOB and 1 female BLOB");
    }

    for (int i = 1; i <= 10; ++i) {

        DNA3 = Procreate();

        Console.WriteLine("Generation {0}:", i);
        PrintStrand("BLOB1", DNA1);
        PrintStrand("BLOB2", DNA2);
        PrintStrand("BLOB3", DNA3);

        int faulty_codons = GetFaultyCodonCount();

        double ratio = ((double)faulty_codons / (double)(DNA3.Length / 3)) * 100.0;

        Console.WriteLine("BLOB3 faulty codons ratio = {0}/{1} = {2}%", faulty_codons, DNA3.Length / 3, ratio);

        if (ratio > 10.0) {
            Console.WriteLine("Newborn dies. Generations are over.");
            break;
        }

        Console.WriteLine();

        DNA1 = DNA3;

        dna1_gender = GetGenderOfStrand(DNA1);

        char new_gender;

        if (dna1_gender == 'f') {
            new_gender = 'm';
        } else if (dna1_gender == 'm') {
            new_gender = 'f';
        } else {
            throw new Exception();
        }

        // generate random dna for DNA2 with specified gender
        GenerateRandomDNA(new_gender, 2);
    }
}


// Print information for all of the operations
Console.WriteLine("Life On Mars");

var old_color = Console.ForegroundColor;

for (int i = 0; i < OPERATION_INFO.Length; ++i) {

    Console.ForegroundColor = ConsoleColor.DarkCyan;
    Console.Write("Operation {0}: ", i + 1);

    Console.ForegroundColor = ConsoleColor.Cyan;
    Console.WriteLine(OPERATION_INFO[i]);

}
Console.ForegroundColor = old_color;

Console.WriteLine();

while (true) {

    try {
        int operation = GetInput("Operation");

        switch (operation) {
            case 1:
                Operation1();
                break;
            case 2:
                Operation2();
                break;
            case 3:
                Operation3();
                break;
            case 4:
                Operation4();
                break;
            case 5:
                Operation5();
                break;
            case 6:
                Operation6();
                break;
            case 7:
                Operation7();
                break;
            case 8:
                Operation8();
                break;
            case 9:
                Operation9();
                break;
            case 10:
                Operation10();
                break;
            case 11:
                Operation11();
                break;
            case 12:
                Operation12();
                break;
            case 13:
                Operation13();
                break;
            case 14:
                Operation14();
                break;
            case 15:
                Operation15();
                break;
            case 16:
                Operation16();
                break;
            case 17:
                Operation17();
                break;
            default:
                throw new Exception("Select a valid operator: operator = [1, 17]");
        }

        Console.WriteLine();

        PrintStrand("DNA1", DNA1);
        PrintStrand("DNA2", DNA2);
        PrintStrand("DNA3", DNA3);

        Console.WriteLine();
    } catch (Exception e) {
        Console.WriteLine("Error: {0}", e.Message);
    }
}
