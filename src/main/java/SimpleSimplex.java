import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleSimplex {

    public static double[][] simplexMatrix;
    public static SimplexMatrix matrix;
    public static int var = 2;
    public static int res = 3;
    public static GuiProgram simplex_gui;

    /**
     * Initializes a GuiProgram, a double-matrix and a simplexMatrix. <br>
     * Also adds exit button functionality to the GuiProgram
     * @param args does nothing
     */
    public static void main(String[] args){

        simplex_gui = new GuiProgram(var,res);

        simplexMatrix = new double[res + 1][var + res + 1];

        matrix = new SimplexMatrix(var, res, simplex_gui);

        simplexMatrix[0][var + res] = 0;

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

        String input;
        matrix.inputData();

        for(int i = 0; i < simplexMatrix.length; i++){

            for(int j = 0; j < var; j++){

                input = simplex_gui.functions[i+1].textFields[j].getText();
                simplexMatrix[i][j] = MathUtility.simplifyDouble(Double.parseDouble(input)).doubleValue();

            }

            for(int j = var; j < (var + res); j++){

                simplexMatrix[i][j] = 0;

            }

            if(i > 0) {

                input = simplex_gui.functions[i+1].textFields[var].getText();
                simplexMatrix[i][var + res] = MathUtility.simplifyDouble(Double.parseDouble(input)).doubleValue();
                simplexMatrix[i][var + i - 1] = 1.0;

            }

        }

        matrix.solve();
        printMatrix();
        MathUtility.simplexIteration(simplexMatrix);
        System.out.println();
        printMatrix();
        MathUtility.simplexIteration(simplexMatrix);
        System.out.println();
        printMatrix();
        simplex_gui.setSize(500,700);
        simplex_gui.setVisible(true);

    }

    /**
     * Prints simplexMatrix into the console
     */
    public static void printMatrix(){

        for(int i = 0; i < simplexMatrix.length; i++){

            for(int j = 0; j < simplexMatrix[i].length; j++){

                System.out.print(simplexMatrix[i][j] + " : ");

            }

            System.out.println();
        }

    }

}
