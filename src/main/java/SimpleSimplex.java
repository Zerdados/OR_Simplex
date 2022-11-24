import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleSimplex {

    public static SimplexMatrix matrix;
    public static int var = 3;
    public static int res = 5;
    public static GuiProgram simplex_gui;

    /**
     * Initializes a GuiProgram, a double-matrix and a simplexMatrix. <br>
     * Also adds exit button functionality to the GuiProgram
     * @param args does nothing
     */
    public static void main(String[] args){

        simplex_gui = new GuiProgram(var,res);
        matrix = new SimplexMatrix(var, res, simplex_gui);

        simplex_gui.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });

    }

    /**
     * Inputs data from the GUI into the matrix's, then solves the simplex matrix's and prints them onto the GUI and
     * into the console.
     */
    public static void simplex(){

        matrix.inputData(1);
        matrix.solveTest(1);
        simplex_gui.setSize(500,700);
        simplex_gui.setVisible(true);

    }

}
