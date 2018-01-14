import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.*;
import java.lang.*;
import javax.imageio.ImageIO;
import java.io.IOException;

public class DisplayPanel extends JPanel {
   private int boardSize = 8;
   private int populationSize = 150;
   private int numGenerations = 400;
   private int currentGeneration = 1;
   private int mutationChance = 80;
   
   /* The higher the selection factor, the more likely QueenSets with higher 
   fitness will be selected for reproduction */
   private int selectionFactor = 5;
   private QueenSet bestBoard;
   private boolean isFinished = false;
   private int pauseTime = 100;
   private int width = 720;
   private int height = 720;
   private int squareWidth = width / boardSize;
   
   // The population of chess boards with random queen positions
   //private List<QueenSet> population = new ArrayList<QueenSet>(); 
   private QueenSet[] population;
   
   public DisplayPanel() {
      generateStartingPopulation();
      setPreferredSize(new Dimension(width, height));
      setBackground(Color.DARK_GRAY);
      
      // Initialize the best board to the first board in the population
      bestBoard = getBestBoard();
      
      // Paint the first generation
      repaint();
   }
   
   public void paint(Graphics g) {
      super.paint(g);
      
      drawBoard(g);
      drawQueens(g);
   }
   
   public void update() {
      if (currentGeneration < numGenerations && bestBoard.getFitness() > 0) {
          try {
             // Implement the clock feature later
             Thread.sleep(pauseTime);
          } catch (Exception ex) {}
          
          // The current generation becomes the parent generation
          QueenSet[] parentPopulation = population.clone();
          
          for (int i = 0; i < populationSize; i += 2) {
          
             // Clone the parent population so it is not changed in this method
             QueenSet[] parents = selectParents(parentPopulation);
             QueenSet[] children = reproduce(parents); 
             
             population[i] = children[0]; 
             
             // If the population size is an odd number, only add one final child
             if (i != populationSize - 1) {
               population[i + 1] = children[1];
             }
          }
          currentGeneration++;
          bestBoard = getBestBoard();
          repaint();
      }
      else {
         isFinished = true;
      }
   }
   
   public void updateVariables(int mutChance, int factor, int numGen, int pause) {
      // These variables can be changed without the program needing to reset
      mutationChance = mutChance;
      selectionFactor = factor;
      numGenerations = numGen;
      pauseTime = pause;
   }
   
   public void restart(int boardSize, int popSize) {
      this.boardSize = boardSize;
      squareWidth = width / boardSize;
      populationSize = popSize;
      generateStartingPopulation();
      currentGeneration = 1;
      bestBoard = getBestBoard();
      isFinished = false;
      
      // Paint the first generation
      repaint();
   }
   
   public QueenSet[] reproduce(QueenSet[] parents) {
      QueenSet[] children = new QueenSet[2];
      
      // Generate a random number between 1 and 7 for the crossover point
      int crossoverPoint = (int) (Math.random() * (boardSize - 1)) + 1;
      int[] firstParentPositions, secondParentPositions, firstChildPositions, secondChildPositions;
      
      firstParentPositions = splice(parents[0].getPositions(), 0, crossoverPoint);
      secondParentPositions = splice(parents[1].getPositions(), crossoverPoint, boardSize);
      firstChildPositions = combineArrays(firstParentPositions, secondParentPositions);      
      
      firstParentPositions = splice(parents[0].getPositions(), crossoverPoint, boardSize);
      secondParentPositions = splice(parents[1].getPositions(), 0, crossoverPoint);
      secondChildPositions = combineArrays(secondParentPositions, firstParentPositions);
      
      firstChildPositions = mutatePositions(firstChildPositions);
      secondChildPositions = mutatePositions(secondChildPositions);
      
      children[0] = new QueenSet(firstChildPositions);
      children[1] = new QueenSet(secondChildPositions);
      
      return children;
   }
   
   public int[] mutatePositions(int[] positions) {
      int rand = (int) Math.round(Math.random() * 100);
      
      if (rand <= mutationChance) {
         int randIndex = (int) Math.round(Math.random() * (boardSize - 1));
         positions[randIndex] = (int) Math.round(Math.random() * (boardSize - 1));
      }
      
      // Have a small chance of double mutation
      if (rand <= mutationChance / 2) {
         int randIndex = (int) Math.round(Math.random() * (boardSize - 1));
         positions[randIndex] = (int) Math.round(Math.random() * (boardSize - 1));
      }
      return positions;
   }
   
   public int[] combineArrays(int[] array1, int[] array2) {
      int[] newArray = new int[array1.length + array2.length];
      
      System.arraycopy(array1, 0, newArray, 0, array1.length);
      System.arraycopy(array2, 0, newArray, array1.length, array2.length);
      
      return newArray;
   }
   
   public int[] splice(int[] array, int startIndex, int endIndex) {
      int arrayLength = endIndex - startIndex;
      
      int[] newArray = new int[arrayLength];
      
      for (int i = 0; i < newArray.length; i++) {
         newArray[i] = array[startIndex + i];
      }
      
      return newArray;
   }
   
   public QueenSet[] selectParents(QueenSet[] parentPopulation) {
      QueenSet[] parents = new QueenSet[2];
      
      Arrays.sort(parentPopulation, new Comparator<QueenSet>() {
         public int compare(QueenSet q1, QueenSet q2) {
            int f1 = q1.getFitness();
            int f2 = q2.getFitness();
            
            // Return positive 1 for the less than condition to sort in ascending order
            if (f1 < f2) 
               return -1;
            else if (f1 > f2)
               return 1;
            else
               return 0;
         }
      });
            
      // Give more fit boards a better chance of moving on to the next generation 
      for (int i = 0; i < parents.length; i++) { 
         // Create a random variable with a then raise it to the fifth power
         double rand = Math.random();
                  
                  
         int randIndex = (int) (populationSize * Math.pow(rand, selectionFactor));
         parents[i] = parentPopulation[randIndex];
      }
      return parents;
   }
   
   public boolean isFinished() {
      return isFinished;
   }
   
   public int getCurrentGeneration() {
      return currentGeneration;
   }
   
   public QueenSet getBestBoard() {
      // Initialize the best board to the first board in the population
      QueenSet board = population[0];
      
      int maxScore = 1000000000;
      for (int i = 0; i < populationSize; i++) {
         int score = population[i].getFitness();
         
         if (score < maxScore) {
            maxScore = score;
            board = population[i];
         }
      }
      return board;
   }
   
   public void generateStartingPopulation() {
      population = new QueenSet[populationSize];
      for (int i = 0; i < populationSize; i++) {
         // A board with N queens arranged randomly on it
         population[i] = new QueenSet(generatePositions());
      }
   }
   
   private int[] generatePositions() {
      int[] positions = new int[boardSize];
      for (int i = 0; i < boardSize; i++) {
         positions[i] = (int) Math.floor(Math.random() * boardSize);
      }
      return positions;  
   }
   
   public Image loadImage() {
      // Read the queen.png file
      try {
         return ImageIO.read(this.getClass().getResource("queen.png"));
      } catch(IOException ex) {}
      
      return null;
   }
   
   public void drawQueens(Graphics g) {
      int[] positions = bestBoard.getPositions();
      int offset = squareWidth / 8;
      int queenLength = squareWidth * 6 / 8;
      Image queen = loadImage();
      
      //g.setColor(Color.BLACK);
      
      for (int i = 0; i < positions.length; i++) {
         g.drawImage(queen, i * squareWidth + offset, positions[i] * squareWidth + offset, queenLength, queenLength, null);
      }
   }
   
   public void drawBoard(Graphics g) {
      boolean whiteSquare = false;
      int x, y;
      
      // Columns
      for (int i = 0; i < boardSize; i++) {
         // Change which square is drawn first to create checkerboard pattern
         if (boardSize % 2 == 0)
            whiteSquare = !whiteSquare;
         // Rows
         for (int j = 0; j < boardSize; j++) {
            x = j * squareWidth;
            y = i * squareWidth;
            
            if (whiteSquare)
               g.setColor(Color.GRAY);
            else
               g.setColor(Color.WHITE);
            whiteSquare = !whiteSquare;
            

            g.fillRect(x, y, squareWidth, squareWidth); 
         }
      }
   }
}