public class SimplexMatrix {

    private String[][] matrix_data;
    private int[] left_row;
    private int var;
    private int res;
    private GuiProgram gui;

    /**
     * Creates a SimplexMatrix with an amount of variables and restrictions, the values as a String-Matrix, and a GUI
     * @param var amount of variables
     * @param res amount of restrictions
     * @param gui GUI
     */
    public SimplexMatrix(int var, int res, GuiProgram gui) {

        matrix_data = new String[res + 1][var + res + 1];
        this.var = var;
        this.res = res;
        this.gui = gui;
        left_row = new int[res];
        for(int i = 0; i < res; i++){
            left_row[i] = var+1+i;
        }

    }

    /**
     * Calls method in gui to print the matrix_data.
     */
    public void printMatrix(){

        gui.printStringMatrix(matrix_data);

    }

    /**
     * Takes values from textFields in gui and saves them in matrix_data.
     * @param p signifies if input is taken literally or simplified
     */
    public void inputData(int p){

        String input;
        matrix_data[0][var + res] = "0";

        for(int i = 0; i < matrix_data.length; i++){

            for(int j = 0; j < var; j++){

                input = gui.functions[i+1].textFields[j].getText();
                switch (p){
                    case 1:
                        matrix_data[i][j] = input;
                        break;
                    default:
                        matrix_data[i][j] = MathUtility.simplifyStringToBigDecimal(input).toString();
                        break;
                }
            }

            for(int j = var; j < (var + res); j++){


                matrix_data[i][j] = "0";

            }

            if(i > 0) {

                input = gui.functions[i+1].textFields[var].getText();
                switch (p){
                    case 1:
                        matrix_data[i][var + res] = input;
                        break;
                    default:
                        matrix_data[i][var + res] = MathUtility.simplifyStringToBigDecimal(input).toString();
                        break;
                }

                matrix_data[i][var + i -1] = "1";

            }
        }
    }

    /**
     * Solves the matrix by calling simplexIteration until the matrix is solved, signified by a 1 being returned.
     */
    public void solve(int p){

        int out = 0;
        this.printMatrix();

        while(true){

            out = MathUtility.simplexIteration(matrix_data, p);
            if(out == 1 || out == -1){
                break;
            }
            this.printMatrix();

        }

    }

    /**
     * For Testing purposes only!
     */
    public void solveTest(int p){
        this.printMatrix();
        MathUtility.simplexIteration(matrix_data, p);
        this.printMatrix();
        MathUtility.simplexIteration(matrix_data, p);
        this.printMatrix();
        MathUtility.simplexIteration(matrix_data, p);
        this.printMatrix();
    }
}
