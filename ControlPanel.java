import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;

public class ControlPanel extends JPanel {
   private DisplayPanel display;
   private Font font = new Font("Tahoma", Font.BOLD, 24);
   private String[] boardList, mutationList, selectionList, populationList, generationsList, pauseList;
   private JComboBox boardDropdown, mutationDropdown, selectionDropdown, populationDropdown, generationsDropdown, pauseDropdown;
   private JButton update, restart;
   
   public ControlPanel(DisplayPanel display) {
      this.display = display;
      setPreferredSize(new Dimension(300, 720));
      setBackground(Color.DARK_GRAY);
      
      setLayout(new FlowLayout(FlowLayout.LEFT));
      //top, left, bottom, right 
      setBorder(new EmptyBorder(new Insets(150, 50, 150, 25)));
      
      boardList = new String[]{"8", "9", "10", "12","15", "16", "20", "24", "30",
       "36", "48", "60", "120", "144", "180", "240", "360", "720"};
      mutationList = new String[]{"10", "20", "40", "60", "80", "100"};
      selectionList = new String[]{"2", "3", "4", "5", "10", "15"};
      populationList = new String[]{"25", "50", "75", "100", "150", "200", "300"};
      generationsList = new String[]{"50", "100", "200", "400", "1000", "4000", "Unlimited"};
      pauseList = new String[]{"0", "10", "50", "100", "500"}; 
       
      boardDropdown = new JComboBox(boardList);
      mutationDropdown = new JComboBox(mutationList);
      selectionDropdown = new JComboBox(selectionList);
      populationDropdown = new JComboBox(populationList);
      generationsDropdown = new JComboBox(generationsList);
      pauseDropdown = new JComboBox(pauseList);
      
      boardDropdown.setSelectedItem("8");
      mutationDropdown.setSelectedItem("80");
      selectionDropdown.setSelectedItem("5");
      populationDropdown.setSelectedItem("150");
      generationsDropdown.setSelectedItem("400");
      pauseDropdown.setSelectedItem("100");
      
      JLabel sizeLabel = new JLabel("Board Size");
      JLabel mutationLabel = new JLabel("Mutation Chance (%)");
      JLabel selectionLabel = new JLabel("Selection Factor");
      JLabel populationLabel = new JLabel("Population Size");
      JLabel generationsLabel = new JLabel("# of Generations");
      JLabel pauseLabel = new JLabel("Delay (Milliseconds)");
      
      update = new JButton("Update");
      
      update.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent a) {
            display.updateVariables(getMutationChance(), getSelectionFactor(),
             getNumGenerations(), getPauseLength());
         }
      });
      
      restart = new JButton("Restart");
      
      restart.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent a) {
            // Update the variables and reset
            display.updateVariables(getMutationChance(), getSelectionFactor(),
             getNumGenerations(), getPauseLength());
            display.restart(getBoardSize(), getPopulationSize());
            repaint();
         }
      });
      
      sizeLabel.setForeground(Color.WHITE);
      mutationLabel.setForeground(Color.WHITE);
      selectionLabel.setForeground(Color.WHITE);
      populationLabel.setForeground(Color.WHITE);
      generationsLabel.setForeground(Color.WHITE);
      pauseLabel.setForeground(Color.WHITE);
      
      int gap = 50;
      
      add(sizeLabel);
      add(boardDropdown);
      add(Box.createRigidArea(new Dimension(0, gap)));
      add(mutationLabel);
      add(mutationDropdown);
      add(Box.createRigidArea(new Dimension(0, gap)));
      add(selectionLabel);
      add(selectionDropdown);
      add(Box.createRigidArea(new Dimension(0, gap)));
      add(populationLabel);
      add(populationDropdown);
      add(Box.createRigidArea(new Dimension(0, gap)));
      add(generationsLabel);
      add(generationsDropdown);
      add(Box.createRigidArea(new Dimension(0, gap)));
      add(pauseLabel);
      add(pauseDropdown);
      add(Box.createRigidArea(new Dimension(0, gap)));
      add(update);
      add(Box.createRigidArea(new Dimension(10, 0)));
      add(restart);
      
   }
   
   public void update() {
      repaint();
   }
   
   public void paint(Graphics g) {
      super.paint(g);
      
      QueenSet bestBoard = display.getBestBoard();
      int fitness = bestBoard.getFitness();
      int currentGeneration = display.getCurrentGeneration();
      
      g.setColor(Color.WHITE);
      g.setFont(font);
      g.drawString("N Queens Genetic", 50, 70);
      g.drawString("Algorithm", 100, 105);
      g.drawString("Generation #" + currentGeneration, 50, 600);
      g.drawString("Fitness: " + fitness, 50, 650);
   }
   
   public int getBoardSize() {
      return Integer.parseInt(boardList[boardDropdown.getSelectedIndex()]);
   }
   
   public int getMutationChance() {
      return Integer.parseInt(mutationList[mutationDropdown.getSelectedIndex()]);
   }
   
   public int getSelectionFactor() {
      return Integer.parseInt(selectionList[selectionDropdown.getSelectedIndex()]);
   }
   
   public int getPopulationSize() {
      return Integer.parseInt(populationList[populationDropdown.getSelectedIndex()]);
   }
   
   public int getNumGenerations() {
      String choice = generationsList[generationsDropdown.getSelectedIndex()];
      if (choice.equals("Unlimited")) {
         return (int) Double.POSITIVE_INFINITY;
      }
      return Integer.parseInt(choice);
   }
   
   public int getPauseLength() {
      return Integer.parseInt(pauseList[pauseDropdown.getSelectedIndex()]);
   }
}