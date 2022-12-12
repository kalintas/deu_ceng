# 2022510127_kerem_kalintas.py

# List containing operators sorted according to operator precedence
OPERATOR_PRECEDENCE = [ 
    [ "**" ], 
    [ "*" , "/", "//", "%" ], # Has same precedence. Operate left to right
    [ "+", "-" ], 
    [ "<", "<=", ">", ">=", "!=", "==" ] 
]

FILE_INPUT = "input.txt"

FILE_OUTPUT = "2022510127_kerem_kalintas_output.txt"

# Open output file in write mode
output_file = open(FILE_OUTPUT, "w")

# Write a line to end of the file
def write_line(string):
    output_file.write(string.upper() + "\n")

# Returns true if c is a valid operator character
def is_operator(c):
    return c == "+" or c == "-" or c == "*" or c == "/" or c == "%" or c == "<" or c == ">" or c == "=" 

# Returns true if c is a whitespace character
def is_space(c):
    return c == " " or c == "\t" or c == "\n" or c == "\r"

def parse_number(line, start, end, inc):

    for i in range(start, end, inc):

        # return index if next value is an operator
        if is_operator(line[i + inc]):
            return i
    
    return end

# Returns the index and type of first operator seen in the operators list
def find_operator(line, operators):
    
    for i in range(len(line)):
        
        for operator in operators:
            
            if line[i:i + len(operator)] == operator:
                
                # Continue searching if its not exactly same
                if len(operator) == 1 and is_operator(line[i + 1]):
                    # Example: operator = "/", line = "10 // 5"
                    continue
                else: 
                    return i, operator
    
    # not found
    return -1, ""

# Calculates operation with given parameters and returns the result
def operate(lhs, rhs, operator):

    if operator == "**":
        return lhs ** rhs
    elif operator == "*":
        return lhs * rhs
    elif operator == "/":
        return lhs / rhs
    elif operator == "//":
        return lhs // rhs
    elif operator == "%":
        return lhs % rhs
    elif operator == "+":
        return lhs + rhs
    elif operator == "-":
        return lhs - rhs
    elif operator == "<":
        return lhs < rhs
    elif operator == ">":
        return lhs > rhs
    elif operator == "<=":
        return lhs <= rhs
    elif operator == ">=":
        return lhs >= rhs
    elif operator == "!=":
        return lhs != rhs
    elif operator == "==":
        return lhs == rhs
    else:
        raise

# Executes operations with the given operators parameter
def execute_operations(line, operators):

    # i = index to start of the operator (-1 if there is none)
    i, operator = find_operator(line, operators)
    
    while i > 0:

        # indices to numbers that are in the left and right side of the operator
        left = parse_number(line, i, 0, -1)
        right = parse_number(line, i + len(operator) - 1, len(line) - 1, 1)

        # cast the numbers to float type
        lhs = float(line[left:i])
        rhs = float(line[i + len(operator):right + 1])
        
        # get operation result 
        result = operate(lhs, rhs, operator)

        # create a new string and add the operation result
        line = line[:left] + str(result) + line[right + 1:]

        # search operator again
        i, operator = find_operator(line, operators)

    return line


# Open input file in read mode
input_file = open(FILE_INPUT, "r")

for line in input_file:
    try:
        old_line = line

        # execute every operation according to operator precedence
        for operators in OPERATOR_PRECEDENCE:
            line = execute_operations(line, operators)

        # Check if the line is changed
        if old_line == line:
            
            # Give an error if there is a non whitespace character in the line
            for char in line:
                if not is_space(char):
                    raise
            
            # Line contains just whitespaces
            write_line("")
        else:
            write_line(line)
    
    except:
        write_line("ERROR")


input_file.close()
output_file.close()