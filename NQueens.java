import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;

public class NQueens extends JFrame{
   private ControlPanel controls;
   private DisplayPanel display;

   public NQueens() {
      super("NQueens Genetic Algorithm");
      setLayout(new BorderLayout());
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setResizable(false);
      
      display = new DisplayPanel();
      controls = new ControlPanel(display);
      
      add(controls, BorderLayout.WEST);
      add(display, BorderLayout.EAST);
      
      pack();
      
      // Centers the window and sets it to be visible
      setLocationRelativeTo(null);
      setVisible(true);
   }
   
   public void start() {
      while (true) {
         controls.update();
         
         // An error can occur if the user clicks restart too fast
         try {
            display.update();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   public static void main(String args[]) {
      NQueens window = new NQueens();
      window.start();
   }
}