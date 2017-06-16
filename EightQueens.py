from graphics import *
import time
import random

SCR_HEIGHT = 800
SCR_WIDTH = 800
BOARD_SIZE = 8
SQUARE_SIZE = SCR_HEIGHT / BOARD_SIZE
MAX_SCORE = sum(range(BOARD_SIZE))
STARTING_POPULATION = 150
NUM_GENERATIONS = 200
MUTATION_CHANCE = 60
FITNESS_EXPONENT = 15
PAUSE_TIME = 1

win = GraphWin("EightQueens", SCR_WIDTH, SCR_HEIGHT)


def draw_grid():
    # Used to alternate the colors, making a checkered pattern
    white_square = False
    for j in range(BOARD_SIZE):
        # Inverse every column in order to create a checkered pattern if the board size is an even number
        if BOARD_SIZE % 2 == 0:
            white_square = not white_square
        for k in range(BOARD_SIZE):
            x = j * SQUARE_SIZE
            y = k * SQUARE_SIZE
            rect = Rectangle(Point(x, y), Point(x + SQUARE_SIZE, y + SQUARE_SIZE))

            if white_square:
                rect.setFill("white")
                white_square = False
            else:
                rect.setFill("grey")
                white_square = True
            rect.draw(win)


def get_random_positions():
    lst = []
    for i in range(BOARD_SIZE):
        lst.append(random.randrange(BOARD_SIZE))
    return lst


class Queens:

    def fitness_function(self):
        # Counts the number of queens on a column or row; if there are more than 2, increment attacks
        attacks = 0

        # Check rows for attacks
        queen_copy = self.queen_positions[:]
        while len(queen_copy) > 0:
            # Continue to index at 0, because items are removed during the loop
            if queen_copy.count(queen_copy[0]) > 1:
                attacks += sum(range(queen_copy.count(queen_copy[0])))
            queen_copy = list(filter(lambda a: a != queen_copy[0], queen_copy))

        # Check diagonals
        queen_copy = self.queen_positions[:]
        diagonals = [[queen_copy[i] + i for i in range(BOARD_SIZE)], [queen_copy[i] - i for i in range(BOARD_SIZE)]]

        for i in range(len(diagonals)):
            while len(diagonals[i]) > 0:
                if diagonals[i].count(diagonals[i][0]) > 1:
                    attacks += sum(range(diagonals[i].count(diagonals[i][0])))
                diagonals[i] = list(filter(lambda a: a != diagonals[i][0], diagonals[i]))

        return attacks

    def __init__(self, positions):
        self.queen_positions = positions
        self.fitness = MAX_SCORE - self.fitness_function()

    def draw_queens(self):
        win.delete('all')
        draw_grid()
        for l in range(len(self.queen_positions)):
            buffer = SQUARE_SIZE / 4
            queen_length = SQUARE_SIZE / 2
            x = l * SQUARE_SIZE + buffer
            y = self.queen_positions[l] * SQUARE_SIZE + buffer
            rect = Rectangle(Point(x, y), Point(x + queen_length, y + queen_length))
            rect.setFill("black")
            rect.draw(win)


# Creates a starting population of 10 queens
def starting_population():
    lst = []
    for i in range(STARTING_POPULATION):
        lst.append(Queens(get_random_positions()))
    return lst


# Gets two queens from the queen population to reproduce more queens with
def selection(population):
    # Get a list of all the scores of the queens and square each one to make the differences more noticeable
    # Get the sum of all the squared scores and produce a random number within the range
    # Loop through all of the queen scores until we get to the queen that the random number falls within
    # Pop this queen from the list, and do the process again to get another queen.
    parents = []
    count = 0
    # Repeat process twice to get two queens
    for k in range(2):
        # Gets a list of the scores of each individual queen set and squares the number
        queen_population_fitness = [i.fitness ** FITNESS_EXPONENT for i in population]
        total_fitness = sum(queen_population_fitness)
        rand = random.randrange(total_fitness)
        for i in range(len(queen_population_fitness)):
            count += queen_population_fitness[i]
            if rand < count:
                # Append the queen to the queen list and break from the current loop to find the second queen
                # print(round(population_fitness[i] ** (1 / EXPONENT)))
                parents.append(population.pop(i))
                break
    return parents


# Based off a small percentage, changes the value of one position in the child
def get_mutation(queen):
    # 5% chance of mutation
    chance = random.randrange(100)
    if chance < MUTATION_CHANCE:
        queen[random.randrange(BOARD_SIZE)] = random.randrange(BOARD_SIZE)
    # Double mutation
    elif chance < MUTATION_CHANCE / 2:
        queen[random.randrange(BOARD_SIZE)] = random.randrange(BOARD_SIZE)
        queen[random.randrange(BOARD_SIZE)] = random.randrange(BOARD_SIZE)
    return queen


# Produces TWO new queens set given the two best queens of the population
def reproduce(parent_queens):
    # Create a crossover point, but never let one queen's data dominate the entire child (Number between 2 and 7)
    cross_over_point = random.randrange(2, 7)

    # Generates the board positions for two new queens
    queen1_pos = parent_queens[0].queen_positions[:cross_over_point] + parent_queens[1].queen_positions[cross_over_point:]
    queen2_pos = parent_queens[1].queen_positions[:cross_over_point] + parent_queens[0].queen_positions[cross_over_point:]

    # Generates a mutation on the queen positions, and sends the positions as a parameter to create a new Queens object
    queen1 = Queens(get_mutation(queen1_pos))
    queen2 = Queens(get_mutation(queen2_pos))
    return [queen1, queen2]

# Main
queen_population = starting_population()

for i in range(NUM_GENERATIONS):
    print("GENERATION NUMBER:", i + 1)
    # Create a copy of the current generation, so we can reproduce from it and overwrite it at the same time
    population_fitness = [i.fitness for i in queen_population]
    best_queen = queen_population[:].pop(population_fitness.index(max(population_fitness)))
    print("FITNESS:", best_queen.fitness)

    best_queen.draw_queens()

    # print([i.queen_positions for i in queen_population])
    # Print fitness in descending order
    print(sorted([i.fitness for i in queen_population], key=int, reverse=True))
    # If we find a solution
    if best_queen.fitness == MAX_SCORE:
        time.sleep(1000)

    time.sleep(PAUSE_TIME)

    parent_population = queen_population[:]
    for j in range(0, len(parent_population), 2):
        # Feed the function a copy of queen population so we don't actually pop the best value
        best_queens = selection(parent_population[:])
        children = reproduce(best_queens)
        queen_population[j] = children[0]
        if j != len(parent_population) - 1:
            queen_population[j + 1] = children[1]










