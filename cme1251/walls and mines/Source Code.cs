// Game constants
const int WIDTH = 53;
const int HEIGHT = 23;

const char WALL = '#';
const char SPACE = ' ';
const char PLAYER = 'P';
const char MINE = '+';
const char ENEMY_X = 'X';
const char ENEMY_Y = 'Y';

// Field of the game containing walls, player, X and Y enemies, mines and numbers
char[,] field = new char[HEIGHT, WIDTH];

// Properties of the player
int player_x;
int player_y;

int energy;
int mine;
int score;

int time_units;

int old_player_x;
int old_player_y;

int time_player_moved;

int enemy_x_count;
int enemy_y_count;

int[,] enemy_x_positions = new int[1000, 2];
int[,] enemy_y_positions = new int[1000, 2];

int animation_count = 0;
int[,] animation_positions = new int[1000, 2];
int[] animation_states = new int[1000];

bool game_over = false;
bool exit_game = false;

Random random = new Random();

// Create a core in the given x and y coordinates
// Game area consists of 4*10 cores of walls.
//     #### <-- Cores are blueprints of 4 walls which are square shaped
//     #  #     In the game, a core must have min. 1 wall, max. 3 walls.
//     #  #     Each wall is randomly generated with 50% probability.
//     ####
void CreateCore(int x, int y) {

    int wall_count = 0;

    // Create upper wall with 50% probability
    // # # # #
    if (random.Next(2) == 0) {
        for (int i = 0; i < 4; ++i) {
            field[y, x + i] = WALL;
        }

        wall_count += 1;
    }

    // Create left wall with 50% probability
    // #
    // #
    // #
    // # 
    if (random.Next(2) == 0) {
        for (int i = 0; i < 4; ++i) {
            field[y + i, x] = WALL;
        }

        wall_count += 1;
    }

    // Create lower wall with 50% probability
    // # # # # 
    if (random.Next(2) == 0) {
        for (int i = 0; i < 4; ++i) {
            field[y + 3, x + i] = WALL;
        }

        wall_count += 1;
    }

    // Create right wall with 50% probability if all of the other walls are not created
    // If the core is empty until this point create the right wall
    //       #
    //       #
    //       #
    //       #
    if (wall_count == 0 || (wall_count != 3 && random.Next(2) == 0)) {
        for (int i = 0; i < 4; ++i) {
            field[y + i, x + 3] = WALL;
        }
    }
}

// Returns true if there is a wall in the core at position x and y
// Index refers to side of the core
bool IsWall(int x, int y, int index) {

    switch (index) {
        case 0: // top
            return field[y, x + 1] == WALL;
        case 1: // right
            return field[y + 1, x + 3] == WALL;
        case 2: // bottom
            return field[y + 3, x + 1] == WALL;
        case 3: // left
            return field[y + 1, x] == WALL;
        default:
            return false;
    }
}

// Set
// Index refers to side of the core
void SetWall(int x, int y, int index, char cell) {

    switch (index) {
        case 0: // top

            for (int i = 0; i < 4; ++i) {
                if (field[y, x + i] != SPACE && field[y, x + i] != WALL) {
                    return;
                }

            }

            for (int i = 0; i < 4; ++i) {
                SetCell(x + i, y, cell);
            }
            break;
        case 1: // right

            for (int i = 0; i < 4; ++i) {
                if (field[y + i, x + 3] != SPACE && field[y + i, x + 3] != WALL) {
                    return;
                }

            }

            for (int i = 0; i < 4; ++i) {
                SetCell(x + 3, y + i, cell);
            }
            break;
        case 2: // bottom

            for (int i = 0; i < 4; ++i) {
                if (field[y + 3, x + i] != SPACE && field[y + 3, x + i] != WALL) {
                    return;
                }

            }

            for (int i = 0; i < 4; ++i) {
                SetCell(x + i, y + 3, cell);
            }
            break;
        case 3: // left

            for (int i = 0; i < 4; ++i) {
                if (field[y + i, x] != SPACE && field[y + i, x] != WALL) {
                    return;
                }

            }

            for (int i = 0; i < 4; ++i) {
                SetCell(x, y + i, cell);
            }
            break;
        default:
            break;
    }
}

// Add one wall to core in given position randomly.
void AddWallToCore(int x, int y, int wall_count) {

    // Empty side index that we will add the wall
    int side = random.Next(4 - wall_count);

    for (int i = 0; i < 4; ++i) {

        if (!IsWall(x, y, i)) {
            // Add this side a wall
            if (side == 0) {
                SetWall(x, y, i, WALL);
                break;
            }

            side -= 1;
        }

    }
}

// Remove one wall to core in given position randomly.
void RemoveWallFromCore(int x, int y, int wall_count) {

    // Empty side index that we will add the wall
    int side = random.Next(wall_count);

    for (int i = 0; i < 4; ++i) {

        if (IsWall(x, y, i)) {
            // Add this side a wall
            if (side == 0) {
                SetWall(x, y, i, SPACE);
                break;
            }

            side -= 1;
        }

    }

    for (int i = 0; i < 4; ++i) {
        if (IsWall(x, y, i)) {
            SetWall(x, y, i, WALL);
        }
    }

}


// Place numbers 1, 2 or 3 randomly in a empty space in the field
void PlaceNumber() {

    double rand = random.NextDouble();

    char number;

    if (rand > 0.4) {
        // 0.6
        number = '1';
    } else if (rand > 0.1) {
        // 0.3
        number = '2';
    } else {
        // 0.1
        number = '3';
    }

    int x;
    int y;

    do {

        x = random.Next(1, WIDTH - 1);
        y = random.Next(1, HEIGHT - 1);

    } while (field[y, x] != SPACE);

    SetCell(x, y, number);
}

// Place enemy X randomly in a empty space in the field
void PlaceEnemyX() {
    int x;
    int y;

    do {

        x = random.Next(1, WIDTH - 1);
        y = random.Next(1, HEIGHT - 1);

    } while (field[y, x] != SPACE);

    SetCell(x, y, ENEMY_X);

    enemy_x_positions[enemy_x_count, 0] = y;
    enemy_x_positions[enemy_x_count, 1] = x;

    enemy_x_count += 1;
}

// Place enemy Y randomly in a empty space in the field
void PlaceEnemyY() {
    int x;
    int y;

    do {

        x = random.Next(1, WIDTH - 1);
        y = random.Next(1, HEIGHT - 1);

    } while (field[y, x] != SPACE);

    SetCell(x, y, ENEMY_Y);

    enemy_y_positions[enemy_y_count, 0] = y;
    enemy_y_positions[enemy_y_count, 1] = x;

    enemy_y_count += 1;
}

// Change console color based on cell
void ChangeColor(char cell) {

    switch (cell) {
        case PLAYER:
            Console.ForegroundColor = ConsoleColor.Green;
            break;
        case ENEMY_X:
        case ENEMY_Y:
            Console.ForegroundColor = ConsoleColor.Red;
            break;
        case MINE:
            Console.ForegroundColor = ConsoleColor.Yellow;
            break;
        case '1':
            Console.ForegroundColor = ConsoleColor.Cyan;
            break;
        case '2':
            Console.ForegroundColor = ConsoleColor.DarkCyan;
            break;
        case '3':
            Console.ForegroundColor = ConsoleColor.Blue;
            break;
        default:
            break;
    }
}

// Set cell at locations of x and y and print the cell
void SetCell(int x, int y, char cell) {

    field[y, x] = cell;

    ChangeColor(cell);

    Console.SetCursorPosition(x * 2, y);
    Console.Write(cell);

    Console.ForegroundColor = ConsoleColor.White;
}

// Moves player if it can move.
// player_x -> player_x + add_x
// player_y -> player_y + add_y
void MovePlayer(int add_x, int add_y) {

    // Move in every two time units if there is no energy left
    if (energy == 0 && time_units <= time_player_moved + 1) {
        return;
    }

    char cell = field[player_y + add_y, player_x + add_x];

    // These can be collected by the human player. They have some functions for the human player.
    //   1: 10 points.
    //   2: 30 points and 50 energy.
    //   3: 90 points, 200 energy and 1 mine.
    // Mine: +
    //   Fatal for any game character(P, X, Y) in the same square with the mine.

    switch (cell) {
        case WALL:
            return;
        case '1':
            score += 10;
            break;
        case '2':
            score += 30;
            energy += 50;
            break;
        case '3':
            score += 90;
            energy += 200;
            mine += 1;
            break;
        case ENEMY_X:
        case ENEMY_Y:
        case MINE:
            game_over = true;
            return;
        default:
            break;
    }

    SetCell(player_x, player_y, SPACE);
    SetCell(player_x + add_x, player_y + add_y, PLAYER);

    old_player_x = player_x;
    old_player_y = player_y;

    player_x += add_x;
    player_y += add_y;

    if (energy > 0) {
        energy -= 1;
    }

    time_player_moved = time_units;
}

// enemy X' prioritie is X axis
// first it try to move in the x axis until they are equal
// moves in the y axis if x positions are equal
void MoveEnemyX(int index) {

    int y = enemy_x_positions[index, 0];
    int x = enemy_x_positions[index, 1];

    int distance_x = player_x - x;
    int distance_y = player_y - y;

    int add_x = 0;
    int add_y = 0;

    if (distance_x > 0) {
        add_x = 1;
    } else if (distance_x < 0) {
        add_x = -1;
    } else {
        if (distance_y > 0) {
            add_y = 1;
        } else if (distance_y < 0) {
            add_y = -1;
        }
    }

    char new_cell = field[y + add_y, x + add_x];

    switch (new_cell) {
        case WALL:
        case ENEMY_X:
        case ENEMY_Y:
            return;
        case PLAYER:
            game_over = true;
            return;
        case MINE:

            // Remove enemy from array

            SetCell(x, y, SPACE);
            SetCell(x + add_x, y + add_y, SPACE);

            for (int i = index; i < enemy_x_count - 1; ++i) {
                enemy_x_positions[i, 0] = enemy_x_positions[i + 1, 0];
                enemy_x_positions[i, 1] = enemy_x_positions[i + 1, 1];
            }
            enemy_x_count -= 1;

            score += 300;

            // Add animation
            animation_positions[animation_count, 0] = y + add_y;
            animation_positions[animation_count, 1] = x + add_x;

            animation_states[animation_count] = 0;

            animation_count += 1;

            return;
        default:
            break;
    }

    SetCell(x, y, SPACE);
    SetCell(x + add_x, y + add_y, ENEMY_X);

    enemy_x_positions[index, 0] += add_y;
    enemy_x_positions[index, 1] += add_x;
}

// enemy Y' prioritie is Y axis
// first it try to move in the y axis until they are equal
// moves in the x axis if y positions are equal
void MoveEnemyY(int index) {

    int y = enemy_y_positions[index, 0];
    int x = enemy_y_positions[index, 1];

    int distance_x = player_x - x;
    int distance_y = player_y - y;

    int add_x = 0;
    int add_y = 0;

    if (distance_y > 0) {
        add_y = 1;
    } else if (distance_y < 0) {
        add_y = -1;
    } else {
        if (distance_x > 0) {
            add_x = 1;
        } else if (distance_x < 0) {
            add_x = -1;
        }
    }

    char new_cell = field[y + add_y, x + add_x];

    switch (new_cell) {
        case WALL:
        case ENEMY_X:
        case ENEMY_Y:
            return;
        case PLAYER:
            game_over = true;
            return;
        case MINE:

            // Remove enemy from array
            SetCell(x, y, SPACE);
            SetCell(x + add_x, y + add_y, SPACE);

            for (int i = index; i < enemy_y_count - 1; ++i) {
                enemy_y_positions[i, 0] = enemy_y_positions[i + 1, 0];
                enemy_y_positions[i, 1] = enemy_y_positions[i + 1, 1];
            }

            enemy_y_count -= 1;

            score += 300;

            // Add animation
            animation_positions[animation_count, 0] = y + add_y;
            animation_positions[animation_count, 1] = x + add_x;

            animation_states[animation_count] = 0;

            animation_count += 1;

            return;
        default:
            break;
    }

    SetCell(x, y, SPACE);
    SetCell(x + add_x, y + add_y, ENEMY_Y);

    enemy_y_positions[index, 0] += add_y;
    enemy_y_positions[index, 1] += add_x;
}



// Game Initialization
void Initialize() {

    // Set everywhere space in the field
    for (int i = 0; i < HEIGHT; ++i) {
        for (int t = 0; t < WIDTH; ++t) {
            field[i, t] = SPACE;
        }
    }

    // Create upper and lower walls
    for (int i = 0; i < WIDTH; ++i) {
        field[0, i] = WALL;
        field[HEIGHT - 1, i] = WALL;
    }

    // Create left and right walls
    for (int i = 0; i < HEIGHT; ++i) {
        field[i, 0] = WALL;
        field[i, WIDTH - 1] = WALL;
    }

    // The walls in the game area are generated randomly
    // Create 40 cores 
    for (int i = 0; i < 4; ++i) {
        for (int t = 0; t < 10; ++t) {

            // Position of the core
            int core_x = 2 + t * 5;
            int core_y = 2 + i * 5;

            // Create core on the field
            CreateCore(core_x, core_y);
        }
    }

    // Human player P is located randomly. Energy of P is 200 at the beginning.
    do {

        player_x = random.Next(1, WIDTH - 1);
        player_y = random.Next(1, HEIGHT - 1);

    } while (field[player_y, player_x] != SPACE);

    field[player_y, player_x] = PLAYER;

    energy = 200;

    // 2 X and 2 Y enemies are located randomly.

    enemy_x_count = 0;
    enemy_y_count = 0;
    mine = 0;

    PlaceEnemyX();
    PlaceEnemyX();
    PlaceEnemyY();
    PlaceEnemyY();
    // 20 numbers (1, 2, or 3) are placed at random positions.
    //  o Generation probability of 1: 60%
    //  o Generation probability of 2: 30%
    //  o Generation probability of 3: 10%
    for (int i = 0; i < 20; ++i) {

        PlaceNumber();
    }

    // Reset game variables

    time_units = 0;
    old_player_x = 0;
    old_player_y = 0;

    time_player_moved = 0;

    score = 0;
}



void HandleInput() {

    if (!Console.KeyAvailable) {
        return;
    }

    // Read key from user
    ConsoleKeyInfo key_info = Console.ReadKey(true);

    switch (key_info.Key) {
        case ConsoleKey.Escape:
            exit_game = true;
            return;
        case ConsoleKey.RightArrow:
            MovePlayer(1, 0);
            break;
        case ConsoleKey.LeftArrow:
            MovePlayer(-1, 0);
            break;
        case ConsoleKey.UpArrow:
            MovePlayer(0, -1);
            break;
        case ConsoleKey.DownArrow:
            MovePlayer(0, 1);
            break;
        case ConsoleKey.Spacebar:

            if (mine > 0 && (old_player_x != player_x || old_player_y != player_y)) {

                if (field[old_player_y, old_player_x] == SPACE) {

                    SetCell(old_player_x, old_player_y, MINE);
                    mine -= 1;
                }
            }

            break;
        default:
            break;
    }
}

// Animate explosions based on animation_positions array
void AnimateExplosions() {

    const string STATES = "$@%&. ";

    for (int i = 0; i < animation_count; ++i) {

        int y = animation_positions[i, 0];
        int x = animation_positions[i, 1];

        for (int j = -1; j <= 1; ++j) {
            for (int t = -1; t <= 1; ++t) {

                int current_y = y + j;
                int current_x = x + t;

                if (field[current_y, current_x] == SPACE) {

                    int state = Math.Min(animation_states[i] + Math.Abs(t) + Math.Abs(j), STATES.Length - 1);

                    char cell = STATES[state];

                    Console.ForegroundColor = ConsoleColor.DarkRed;

                    Console.SetCursorPosition(current_x * 2, current_y);
                    Console.Write(cell);

                    Console.ForegroundColor = ConsoleColor.White;
                }
            }
        }

        animation_states[i] += 1;
        
        // Remove animation state from the array when it finishes
        if (animation_states[i] >= STATES.Length) {
    
            for (int t = i; t < animation_count - 1; ++t) {
                animation_states[t] = animation_states[t + 1];

                animation_positions[t, 0] = animation_positions[t + 1, 0];
                animation_positions[t, 1] = animation_positions[t + 1, 1];
            }

            animation_count -= 1;
        }
    }
}


// Main loop of the game
void GameLoop(int sleep) {

    // Print field to console
    for (int i = 0; i < HEIGHT; ++i) {
        for (int t = 0; t < WIDTH; ++t) {

            SetCell(t, i, field[i, t]);
        }
    }

    while (!game_over && !exit_game) {

        HandleInput();

        int seconds_passed = (int)((double)time_units * ((double)sleep / 1000.0));

        Console.SetCursorPosition(WIDTH * 2, 0);
        Console.Write("Time   : {0}   ", seconds_passed);

        Console.SetCursorPosition(WIDTH * 2, 1);
        Console.Write("Score  : {0}   ", score);

        Console.SetCursorPosition(WIDTH * 2, 2);
        Console.Write("Energy : {0}   ", energy);

        Console.SetCursorPosition(WIDTH * 2, 3);
        Console.Write("Mine   : {0}   ", mine);

        if (old_player_x == 0 && old_player_y == 0) {
            Thread.Sleep(sleep);
            continue;
        }

        for (int i = 0; i < enemy_x_count; ++i) {

            MoveEnemyX(i);
        }

        for (int i = 0; i < enemy_y_count; ++i) {

            MoveEnemyY(i);
        }

        AnimateExplosions();

        time_units += 1;

        // 1 number (1, 2, or 3 with the same generation probabilities) is
        // added to the game area (random position) for every 10 time units. 
        if (time_units % 10 == 0) {
            PlaceNumber();
        }

        // 1 enemy (X or Y with equal probability) is
        // added to the game area (random position) for every 150 time units. 
        if (time_units % 150 == 0) {

            if (random.Next(2) == 0) {
                PlaceEnemyX();
            } else {
                PlaceEnemyY();
            }
        }

        // 1 wall is added or deleted randomly for every 150 time unit.
        if (time_units % 5 == 0) {

            int core_x = random.Next(10); // core_x: [0, 10)
            int core_y = random.Next(4);  // core_y: [0, 4)

            // position to left most cell of the core
            core_x = 2 + core_x * 5;
            core_y = 2 + core_y * 5;

            int wall_count = 0;

            for (int i = 0; i < 4; ++i) {
                if (IsWall(core_x, core_y, i)) {
                    wall_count += 1;
                }
            }

            // Add wall
            if (wall_count == 1) {
                AddWallToCore(core_x, core_y, wall_count);
            }

            // Add or remove wall
            if (wall_count == 2) {
                // Select one randomly
                if (random.Next(2) == 0) {
                    AddWallToCore(core_x, core_y, wall_count);
                } else {
                    RemoveWallFromCore(core_x, core_y, wall_count);
                }
            }

            // Remove wall
            if (wall_count == 3) {
                RemoveWallFromCore(core_x, core_y, wall_count);
            }

        }

        // Clear console inputs
        while (Console.KeyAvailable) {
            Console.ReadKey(false);
        }

        Thread.Sleep(sleep);
    }

}

while (!exit_game) {

    Console.Clear();

    Console.WriteLine("***DIFFICULTY MENU***");
    Console.WriteLine("1-) Hard Mode");
    Console.WriteLine("2-) Normal Mode (Default)");
    Console.WriteLine("3-) Easy Mode");

    int difficulty;

    while (!int.TryParse(Console.ReadLine(), out difficulty) || difficulty  < 1 || difficulty  > 3) {
        Console.WriteLine("Enter a valid difficulty");
    }

    int sleep = 0;

    switch (difficulty) {
        case 1:
            // Hard Mode
            sleep = 75;
            break;
        case 2:
            // Normal Mode
            sleep = 200;
            break;
        case 3:
            // Easy Mode
            sleep = 250;
            break;
    }
    
    Console.Clear();

    // Initialize the game
    Initialize();

    // Run the game
    GameLoop(sleep);

    Console.BackgroundColor = ConsoleColor.White;
    for (int i = 10; i <= 13; ++i) {
        for (int t = 20; t <= 35; ++t) {
            Console.SetCursorPosition(t * 2, i);
            Console.Write(' ');
            Console.SetCursorPosition(t * 2 + 1, i);
            Console.Write(' ');

        }
    }

    Console.ForegroundColor = ConsoleColor.Red;

    Console.SetCursorPosition(25 * 2, 11);
    Console.WriteLine("GAME OVER");
    Console.SetCursorPosition(21 * 2 + 1, 12);
    Console.WriteLine("Press enter for a new game");

    Console.ForegroundColor = ConsoleColor.White;

    Console.BackgroundColor = ConsoleColor.Black;

    while (!exit_game && game_over) {

        if (!Console.KeyAvailable) {
            continue;
        }

        // Read key from user
        ConsoleKeyInfo key_info = Console.ReadKey(true);

        if (key_info.Key == ConsoleKey.Enter) {
            game_over = false;
        } else if (key_info.Key == ConsoleKey.Escape) {
            exit_game = true;
        }
    }

}

