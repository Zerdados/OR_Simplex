import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleSimplex {

    public static double[][] simplexMatrix;
    public static SimplexMatrix matrix;
    public static int var = 2;
    public static int res = 3;
    public static GuiProgram simplex_gui;

    public static void main(String args[]){

        //Scanner scanner = new Scanner(System.in);

        simplex_gui = new GuiProgram(var,res);

        simplexMatrix = new double[res + 1][var + res + 1]; //TODO: Change to BigDecimal Matrix

        matrix = new SimplexMatrix(var, res, simplex_gui);

        simplexMatrix[0][var + res] = 0;

        simplex_gui.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });

    }

    public static void simplex(){

        String input;

        matrix.inputData();

        for(int i = 0; i < simplexMatrix.length; i++){

            for(int j = 0; j < var; j++){

                System.out.println("Input number at [" + i + "][" + j + "]:");
                //input = scanner.nextLine();
                input = simplex_gui.functions[i+1].textFields[j].getText();
                simplexMatrix[i][j] = MathUtility.simplifyDouble(Double.parseDouble(input)).doubleValue();



            }

            for(int j = var; j < (var + res); j++){


                simplexMatrix[i][j] = 0;

            }

            if(i > 0) {
                System.out.println("Input number at [" + i + "][" + (var + res) + "]:");
                //input = scanner.nextLine();
                input = simplex_gui.functions[i+1].textFields[var].getText();
                simplexMatrix[i][var + res] = MathUtility.simplifyDouble(Double.parseDouble(input)).doubleValue();
                simplexMatrix[i][var + i - 1] = 1.0;
            }

        }

        printMatrix();
        simplex_gui.printMatrix(simplexMatrix);
        MathUtility.simplexIteration(simplexMatrix);
        System.out.println();
        printMatrix();
        simplex_gui.printMatrix(simplexMatrix);
        MathUtility.simplexIteration(simplexMatrix);
        System.out.println();
        printMatrix();
        simplex_gui.printMatrix(simplexMatrix);
        matrix.solve();
        simplex_gui.setSize(500,700);
        simplex_gui.setVisible(true);

    }

    public static void printMatrix(){

        for(int i = 0; i < simplexMatrix.length; i++){

            for(int j = 0; j < simplexMatrix[i].length; j++){

                System.out.print(simplexMatrix[i][j] + " : ");

            }

            System.out.println();
        }

    }



}
