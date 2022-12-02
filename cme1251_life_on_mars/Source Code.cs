using System;

char[] DNA1 = { };
char[] DNA2 = { };
char[] DNA3 = { };

string RemoveCharacter(string array, char c) {

    int count = 0;

    for (int i = 0; i < array.Length; ++i) {
        if (array[i] != c) {
            count += 1;
        }
    }

    char[] result = new char[count];
    int index = 0;

    for (int i = 0; i < array.Length; ++i) {
        if (array[i] != c) {
            result[index] = array[i];
            index += 1;
        }
    }

    return new string(result);
}

bool IsStartCodon(char[] c) {
    return c[0] == 'A' && c[1] == 'T' && c[2] == 'G';
}

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

// |------------------| input output functions |------------------|
string GetOperationInput(string operation_name) {
    // read input from console and remove whitespaces
    // check operation name and give error if its incorrect
    // return user input

    // remove white space
    string input = RemoveCharacter(Console.ReadLine(), ' ');

    if (input[..operation_name.Length].ToLower() != operation_name) {
        throw new Exception("Wrong operation name.");
    }
    
    return input;
}

int GetInput(string var_name) {
    // get a integer value from user and return it

    Console.Write("{0}: ", var_name);

    return Convert.ToInt32(Console.ReadLine());
}

void PrintStrand(string name, char[] strand) {

    Console.Write("{0} (n = {1}):", name, strand.Length / 3);

    for (int i = 0; i < strand.Length; ++i) {

        if (i % 3 == 0) {
            // print whitespace for readability
            Console.Write(' ');
        }

        Console.Write(strand[i]);
    }

    Console.WriteLine();
}

// |--------------------| utility functions |--------------------|
int GetDnaType(string input, int index) {
    // return dna_type from input
    // dna_type correct values -> 1, 2, 3

    int dna_type = Convert.ToInt32(input[index].ToString());

    if (dna_type < 0 || dna_type > 3) {
        throw new Exception("Incorrect dna type. Correct values: 1, 2, 3");
    }

    return dna_type;
}

char[] CopyToStrand(char[] strand, string str, int start) {
    // copy string contents to strand starting at index

    for (int i = 0; i < str.Length; ++i) {

        strand[start + i] = str[i];
    }

    return strand;
}

void SetDNA(char[] strand, int dna_type) {
    // copy strand to DNA1, DNA2 or DNA3

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
            throw new Exception();
    }
}

char[] GetCodon(char[] strand, int index) {
    // return codon from strand at index
    // index correct values: 0, (strand.Length / 3) - 1

    int i = index * 3;

    return new char[] { strand[i], strand[i + 1], strand[i + 2] };
}

char[] SetCodon(char[] strand, int index, char[] codon) {
    // copy codon parameter to codon from strand at index
    // index correct values: 0, (strand.Length / 3) - 1

    for (int i = 0; i < 3; ++i) {
        strand[index * 3 + i] = codon[i];
    }

    return strand;
}

char[]? GetNextGene(char[] strand, ref int index) {
    // return next gene starting from parameter index
    // increment index for later use
    // index correct values: 0, (DNA1.Length / 3) - 1

    int start = -1;

    for (;  index < strand.Length / 3; ++index) {
        
        var codon = GetCodon(strand, index);

        if (IsStartCodon(codon)) {
            
            start = index;
        } else if (IsStopCodon(codon)) {
            
            if (start >= 0) {

                char[] gene = new char[(index - start + 1) * 3];

                for (int t = 0; t < gene.Length; ++t) {
                    gene[t] = strand[start * 3 + t];
                }

                index += 1;

                return gene;
            }
        }
    }

    return null;
}

void GenerateRandomDNA(char gender, int dna_type) {
    
    Random rng = new Random();

    // dont include the first gene
    // which is gender gene
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

            strand = SetCodon(strand, i + 1, new char[] { x, x ,x });
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

char? GetGenderOfStrand(char[] strand) {

    if (strand.Length >= 12) {
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
        if ((first_x || second_x) && (first_y || second_y)) {
            // XY
            return 'm';
        }
    }

    // gender not defined
    return null;    
}

char[] Procreate() {

    int total_length = 4 * 3;

    int index1 = 4;
    int index2 = 4;

    char[]? gene1, gene2;

    while (true) {

        gene1 = GetNextGene(DNA1, ref index1);

        if (gene1 == null) {
            total_length += DNA2.Length - index2 * 3;
            break;
        }

        total_length += gene1.Length;

        GetNextGene(DNA2, ref index2); // skip next gene
        gene2 = GetNextGene(DNA2, ref index2);

        if (gene2 == null) {
            total_length += DNA1.Length - index1 * 3;
            break;
        }
    
        total_length += gene2.Length;

        GetNextGene(DNA1, ref index1); // skip next gene
    }

    char[] strand = new char[total_length];

    strand = CopyToStrand(strand, "ATG", 0);
    strand = SetCodon(strand, 1, GetCodon(DNA1, 1));
    strand = SetCodon(strand, 2, GetCodon(DNA2, 2));
    strand = CopyToStrand(strand, "TAG", 9);

    int index = 12; // end of gender gene

    index1 = 4;
    index2 = 4;

    while (true) {

        gene1 = GetNextGene(DNA1, ref index1);

        if (gene1 == null) {
            
            for (int i = index2 * 3; i < DNA2.Length; ++i) {
                strand[index++] = DNA2[i];
            }
            break;
        }

        for (int i = 0; i < gene1.Length; ++i) {
            strand[index++] = gene1[i];
        }

        GetNextGene(DNA2, ref index2); // skip next gene
        gene2 = GetNextGene(DNA2, ref index2);

        if (gene2 == null) {

            for (int i = index1 * 3; i < DNA1.Length; ++i) {
                strand[index] = DNA1[i];
                index += 1;
            }
            break;
        }

        for (int i = 0; i < gene2.Length; ++i) {
            strand[index++] = gene2[i];
        }

        GetNextGene(DNA1, ref index1); // skip next gene
    }

    return strand;
}

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

string CheckDNAStructure(char[] strand) {
    
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

    return "Gene structure is OK";
}

// |------------------| operation functions |------------------|

void Operation1() {
    // load a DNA sequance from a file

    string file_path = GetOperationInput("load");

    int dna_type = GetDnaType(file_path, 4);

    file_path = file_path[5..];

    string text = File.ReadAllText(file_path);

    // make it all upper case for storing same type of characters
    var strand = text.ToUpper().ToCharArray();

    SetDNA(strand, dna_type);
}

void Operation2() {
    // load a DNA sequance from user input

    string input = GetOperationInput("load");
    // remove " with RemoveCharacter function
    input = RemoveCharacter(input, '\"');

    int dna_type = GetDnaType(input, 4);
   
    var strand = input[5..].ToUpper().ToCharArray();
        
    SetDNA(strand, dna_type);
}

void Operation3() {
    // generate a random DNA sequance

    // command: generate <gender> <dna_type>
    // <gender> -> f(female) or m(male)
    // <dna_type> -> 1, 2 or 3

    string input = GetOperationInput("generate");

    char gender = input[8];

    int dna_type = GetDnaType(input, 9);

    GenerateRandomDNA(gender, dna_type);
}

void Operation4() {

    Console.WriteLine(CheckDNAStructure(DNA1));
}

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

void Operation6() {
    // DNA complement
    char[] complement = new char[DNA1.Length];

    for (int i = 0; i < DNA1.Length; i++) {
        
        complement[i] = DNA1[i];
    }

    for (int k = 0; k < complement.Length; k++) {
        
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

void Operation7() {
    //Amino acids 

    PrintStrand("DNA strand", DNA1);

    Console.Write("Amino acids:         ");

    for (int i = 0; i < DNA1.Length / 3; i++) {
        string codon = new string(GetCodon(DNA1, i)); //****

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

void Operation9() {

    PrintStrand("DNA strand (stage 1)", DNA1);

    Console.Write("Codon sequence: ");    

    char[] codons = RemoveCharacter(Console.ReadLine().ToUpper(), ' ').ToCharArray();

    int n = GetInput("Starting from") - 1;

    if (codons.Length % 3 != 0) {

        Console.WriteLine("Error , Try again. Please enter the full number of codons");
    }

    if ((n < 0) && (n > DNA1.Length + 1)) {
        Console.WriteLine("Please enter a number in the correct range");
    } else {
        Console.WriteLine("Starting from :" + n);
    }

    char[] strand = new char[DNA1.Length + codons.Length];

    for (int i = 0; i < n * 3; ++i) {
        strand[i] = DNA1[i];
    }
    
    for (int i = 0; i < codons.Length; ++i) {
        strand[n * 3 + i] = codons[i];
    }

    for (int i = n * 3; i < DNA1.Length; ++i) {
        strand[n * 3 + i] = DNA1[i];
    }

    DNA1 = strand;

    PrintStrand("DNA strand (stage 2)", DNA1);
}

void Operation10() {

    Console.Write("Codon sequence: ");
    char[] codons = RemoveCharacter(Console.ReadLine().ToUpper(), ' ').ToCharArray();

    int m = GetInput("Starting from") - 1;

    Console.Write("Result: ");

    int count = 0;

    for (int i = m; i <= (DNA1.Length - codons.Length) / 3; ++i) {

        bool equal = true;

        for (int t = 0; t < codons.Length / 3; ++t) {

            var a = new string(GetCodon(DNA1, i + t));
            var b = new string(GetCodon(codons, t));

            if (a != b) {
                equal = false;
                break;
            }
        }

        if (equal) {

            if (count > 0) {
                Console.Write(", ");
            }
            
            Console.WriteLine("{0}", i + 1);
            count += 1;
        }
    }

    if (count == 0) {
        Console.Write("-1");
    }

    Console.WriteLine();
}

void Operation11() {
    // Reverse codons 

    int n = GetInput("n"); // reverse n codons
    int m = GetInput("m") - 1; // starting from m

    if ((m + n) * 3 > DNA1.Length || m < 0 || n < 0) {
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

void Operation12() {
    // Find the number of genes in the DNA strand 1

    char[]? gene;
    int index = 0;
    int gene_count = 0;

    while ((gene = GetNextGene(DNA1, ref index)) != null) {

        gene_count += 1;
    }

    Console.WriteLine("Number of genes: {0}", gene_count);
}

void Operation13() {
    // Find the shortest gene in the DNA strand 1

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

void Operation14() {
    // Find the longest gene in the DNA strand 1

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

void Operation15() {
    // Find the most repeated n-nucleotide sequence in a DNA strand

    int count = GetInput("Enter number of nucleotide: ");

    char[] most_repeated = new char[count];
    int biggest_frequency = 0;

    int length = DNA1.Length - count + 1;

    for (int i = 0; i < length; ++i) {

        char[] codon1 = new char[count];
 
        for (int j = 0; j < count; ++j) {
            codon1[j] = DNA1[i + j];
        }

        int frequency = 0;

        for (int t = 0; t < length; ++t) {

            char[] codon2 = new char[count];

            for (int j = 0; j < count; ++j) {
                codon2[j] = DNA1[t + j];
            }

            if (new string(codon1) == new string(codon2)) {
                frequency += 1;
            }
        }

        if (frequency > biggest_frequency) {
            most_repeated = codon1;
            biggest_frequency = frequency;
        }

    }

    PrintStrand("Most repeated sequance", most_repeated);

    Console.WriteLine("Frequency: {0}", biggest_frequency);
}

void Operation16() {
    
    // A-T 2-hydrogen bond
    // C-G 3-hydrogen bond

    int hydrogen2 = 0;
    int hydrogen3 = 0;

    for (int i = 0; i < DNA1.Length; ++i) {

        if (DNA1[i] == 'A' || DNA1[i] == 'T') {

            hydrogen2 += 1;
        } else if (DNA1[i] == 'C' || DNA1[i] == 'G') {

            hydrogen3 += 1;
        }
    }

    int hydrogen_bonds = hydrogen2 * 2 + hydrogen3 * 3;

    Console.WriteLine("Number of pairing with 2-hydrogen bonds: " + hydrogen2);
    Console.WriteLine("Number of pairing with 3-hydrogen bonds: " + hydrogen3);
    Console.WriteLine("Number of hydrogen bonds: " + hydrogen_bonds);

}

void Operation17() {

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

        DNA1 = DNA3;

        char? gender_of_dna1 = GetGenderOfStrand(DNA1);
        char gender;

        if (gender_of_dna1 == 'f') {
            gender = 'm';
        } else if (gender_of_dna1 == 'm') {
            gender = 'f';
        } else {
            throw new Exception();
        }

        // generate random dna for DNA2 with specified gender
        GenerateRandomDNA(gender, 2);
    }

}

while (true) {
    
    try {
        int operation = GetInput("operation");

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
