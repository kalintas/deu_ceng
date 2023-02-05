
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
int player_x = 0;
int player_y = 0;

int energy = 0;
int mine = 10;
int score = 0;

int time_units = 0;


int old_player_x = player_x;
int old_player_y = player_y;

int enemy_x_count = 0;
int enemy_y_count = 0;

int[,] enemy_x_positions = new int[1000, 2];
int[,] enemy_y_positions = new int[1000, 2];

bool game_over = false;

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

// Place given type of entity to an random empty location
void PlaceRandomly(char type) {

    int x;
    int y;

    do {

        x = random.Next(1, WIDTH - 1);
        y = random.Next(1, HEIGHT - 1);

    } while (field[y, x] != SPACE);

    field[y, x] = type;
}

void PlaceEnemyX() {
    int x;
    int y;

    do {

        x = random.Next(1, WIDTH - 1);
        y = random.Next(1, HEIGHT - 1);

    } while (field[y, x] != SPACE);

    field[y, x] = ENEMY_X;

    enemy_x_positions[enemy_x_count, 0] = y;
    enemy_x_positions[enemy_x_count, 1] = x;

    enemy_x_count += 1;
}

void PlaceEnemyY() {
    int x;
    int y;

    do {

        x = random.Next(1, WIDTH - 1);
        y = random.Next(1, HEIGHT - 1);

    } while (field[y, x] != SPACE);

    field[y, x] = ENEMY_Y;

    enemy_y_positions[enemy_y_count, 0] = y;
    enemy_y_positions[enemy_y_count, 1] = x;

    enemy_y_count += 1;
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

    PlaceEnemyX();
    PlaceEnemyX();
    PlaceEnemyY();
    PlaceEnemyY();
    // 20 numbers (1, 2, or 3) are placed at random positions.
    //  o Generation probability of 1: 60%
    //  o Generation probability of 2: 30%
    //  o Generation probability of 3: 10%
    for (int i = 0; i < 20; ++i) {

        double rand = random.NextDouble();

        if (rand > 0.6) {
            PlaceRandomly('1');
        } else if (rand > 0.9) {
            PlaceRandomly('2');
        } else {
            PlaceRandomly('3');
        }
    }
}

// Set cell at locations of x and y
void SetCell(int x, int y, char cell) {

    field[y, x] = cell;
    
    if (cell == PLAYER) {
        Console.ForegroundColor = ConsoleColor.Green;
    }

    Console.SetCursorPosition(x * 2, y);
    Console.Write(cell);

    if (cell == PLAYER) {
        Console.ForegroundColor = ConsoleColor.White;
    }
}

// Moves [x, y] cell to [x + add_x, y + add_y]
// returns if cell can move
bool Move(int x, int y, int add_x, int add_y, char type) {

    if (field[y + add_y, x + add_x] == WALL) {
        return false;
    }

    SetCell(x, y, SPACE);
    SetCell(x + add_x, y + add_y, type);

    return true;
}

// Moves player if it can move.
// player_x -> player_x + add_x
// player_y -> player_y + add_y
void MovePlayer(int add_x, int add_y) {

    // Move in every two time units if there is no energy left
    if (energy == 0 && time_units % 2 == 0) {
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
}

// Print field to console
void PrintField() {

    for (int i = 0; i < HEIGHT; ++i) {
        for (int t = 0; t < WIDTH; ++t) {

            Console.Write("{0} ", field[i, t]);
        }
        Console.WriteLine();
    }
}

void HandleInput() {

    if (!Console.KeyAvailable) {
        return;
    }

    // Read key from user
    ConsoleKeyInfo key_info = Console.ReadKey(true);

    switch (key_info.Key) {
        case ConsoleKey.Escape:
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

            return;
        default:
            break;
    }

    SetCell(x, y, SPACE);
    SetCell(x + add_x, y + add_y, ENEMY_X);

    enemy_x_positions[index, 0] += add_y;
    enemy_x_positions[index, 1] += add_x;
}

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

            return;
        default:
            break;
    }

    SetCell(x, y, SPACE);
    SetCell(x + add_x, y + add_y, ENEMY_Y);

    enemy_y_positions[index, 0] += add_y;
    enemy_y_positions[index, 1] += add_x;
}

// Main loop of the game
void GameLoop() {

    PrintField();

    while (!game_over) {

        HandleInput();

        if (time_units % 5 == 0) {
            for (int i = 0; i < enemy_x_count; ++i) {

                MoveEnemyX(i);
            }

            for (int i = 0; i < enemy_y_count; ++i) {

                MoveEnemyY(i);
            }
        }

        Console.SetCursorPosition(WIDTH * 2, 0);
        Console.Write("Time   : {0}   ", time_units / 5);

        Console.SetCursorPosition(WIDTH * 2, 1);
        Console.Write("Score  : {0}   ", score);

        Console.SetCursorPosition(WIDTH * 2, 2);
        Console.Write("Energy : {0}   ", energy);

        Console.SetCursorPosition(WIDTH * 2, 3);
        Console.Write("Mine   : {0}   ", mine);

        // Clear console inputs
        while (Console.KeyAvailable) {
            Console.ReadKey(false);
        }

        time_units += 1;
        Thread.Sleep(200);
    }

    Console.WriteLine("GAME OVER");
    Console.ReadLine();
}

// Initialize the game
Initialize();
// Run the game
GameLoop();