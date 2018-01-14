import java.util.*;

public class QueenSet {
   private int[] positions;
   private int boardSize;
   
   // How many queens are attacking eachother
   private int fitness;
   
   public QueenSet(int[] positions) {
      this.positions = positions;
      boardSize = positions.length;
      fitness = calculateFitness();
   }
   
   public int[] getPositions() {
      return positions;
   }
   
   public int getFitness() {
      return fitness;
   }
   
   public int calculateFitness() {
      int totalAttacks = 0;
      
      int[] positionsCopy = positions.clone();
      
      while (positionsCopy.length > 0) {
          int numAttacks = countOccurences(positionsCopy, positionsCopy[0]);
          // If there is only one queen on that row, there are no attacks
          if (numAttacks > 1) {
             totalAttacks += calculateRangeSum(numAttacks);
          }
          
          positionsCopy = removeElement(positionsCopy, positionsCopy[0], positionsCopy.length - numAttacks);
      }
      
      positionsCopy = positions.clone();
      int[][] diagonals = new int[2][boardSize];
      
      // Generate the diagonals (numbers with the same value are on same diagonal)
      // Positive and Negative Diagonal
      for (int i = 0; i < 2; i++) {
         for (int j = 0; j < boardSize; j++) {
            if (i == 0)
               diagonals[i][j] = positionsCopy[j] + j;
            else
               diagonals[i][j] = positionsCopy[j] - j;
         }
      }
      
      for (int i = 0; i < 2; i++) {
        while (diagonals[i].length > 0) {
           int numAttacks = countOccurences(diagonals[i], diagonals[i][0]);
           // If there is only one queen on that row, there are no attacks
           if (numAttacks > 1) {
              totalAttacks += calculateRangeSum(numAttacks);
           }
            
           diagonals[i] = removeElement(diagonals[i], diagonals[i][0], diagonals[i].length - numAttacks);
        }
      }
      
      return totalAttacks;
   }
   
   // Counts the occurences of a number in a list
   private int countOccurences(int[] list, int num) {
      int total = 0;
      
      for (int i = 0; i < list.length; i++) {
         if (list[i] == num)
            total++;
      }
      return total;
   }
   
   private int[] removeElement(int[] list, int num, int newLength) {
      int[] newList = new int[newLength];
      int total = 0;
      
      int j = 0;
      for (int i = 0; i < list.length; i++) {
         if (list[i] != num) {
            newList[j] = list[i];
            j++;
         }
      }
      
      return newList;
   }
   
   // Sums the range of numbers
   private int calculateRangeSum(int num) {
      int total = 0;
      
      for (int i = 0; i < num; i++) {
         total += i;
      }
      return total;
   }  
}