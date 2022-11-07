import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtility {

    //TODO Comments for all functions
    private static DecimalFormat df = new DecimalFormat("0.###E0");

    //Takes a double value as input and transforms it to exponential notation with a matrissa length of for
    //Returns exponential notation as a String
    public static String doubleToFourMatrissa(double in){

        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return df.format(BigDecimal.valueOf(in));

    }

    private static String stringToFourMatrissa(String in){

        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return df.format(BigDecimal.valueOf(Double.valueOf(in)));

    }

    //Takes a String representing a number in exponential notation and returns that number as a BigDecimal
    public static BigDecimal fourMatrissaToBigDecimal(String in){

        String[] splitIn = in.split("E");
        splitIn[0] = splitIn[0].replace(",", ".");

        int exponent = Integer.parseInt(splitIn[1]);
        BigDecimal matrissa = new BigDecimal(splitIn[0]);

        return matrissa.multiply(BigDecimal.valueOf(Math.pow(10.0, exponent)));

    }

    //Takes a double value as input and returns as a BigDecimal with a matrissa length of four
    public static BigDecimal simplifyDouble(double in){

        return fourMatrissaToBigDecimal(doubleToFourMatrissa(in));

    }

    //TODO: Finish up this function
    public static String simplifyString(String in){

        return in;

    }

    public static BigDecimal simplifyStringToBigDecimal(String in){

        return fourMatrissaToBigDecimal(stringToFourMatrissa(in));

    }

    //Calculates one simplex iteration on a given matrix
    //Returns 0 if successful, returns 1 if matrix is already solved
    public static int simplexIteration(double[][] inMatrix) {

        int[] pivot = calculatePivot(inMatrix);

        if(pivot[1] == -1){
            return 1;
        }

        rowNormalization(inMatrix, pivot);
        columnNormalization(inMatrix, pivot);
        return 0;

    }

    public static int simplexIteration(String[][] inMatrix) {

        int[] pivot = calculatePivot(inMatrix);

        System.out.println(pivot[1]);
        if(pivot[1] == -1){
            return 1;
        }

        rowNormalization(inMatrix, pivot);
        columnNormalization(inMatrix, pivot);
        return 0;

    }

    //Calculates the pivot element for a given matrix using the most-negative strategy
    private static int[] calculatePivot(double[][] inMatrix){

        int pivot_col = 0;
        int pivot_row = 0;
        double temp = 0;
        boolean first_iteration = true;

        for(int i = 0; i < inMatrix[0].length; i++){

            if(inMatrix[0][i] < temp){
                temp = inMatrix[0][i];
                pivot_col = i;
            }

        }

        if(temp == 0) {
            int[] arr = {0, -1};
            return arr;
        }

        temp = 0;

        for(int i = 1; i < inMatrix.length; i++){

            double quot = inMatrix[i][inMatrix[i].length-1]/inMatrix[i][pivot_col];

            if(quot < temp || first_iteration){

                temp = quot;
                pivot_row = i;
                first_iteration = false;

            }

        }

        int[] arr = {pivot_row, pivot_col};
        return arr;

    }


    //Calculates and returns the pivot element of a given simplex matrix
    private static int[] calculatePivot(String[][] inMatrix){

        int pivot_col = 0;
        int pivot_row = 0;
        double temp = 0;
        boolean first_iteration = true;

        for(int i = 0; i < inMatrix[0].length; i++){

            double d = Double.parseDouble(inMatrix[0][i]);
            if(d < temp){
                temp = d;
                pivot_col = i;
            }

        }

        if(temp == 0) {
            int[] arr = {0, -1};
            return arr;
        }

        temp = 0;

        for(int i = 1; i < inMatrix.length; i++){

            double quot = Double.parseDouble(inMatrix[i][inMatrix[i].length-1])/Double.parseDouble(inMatrix[i][pivot_col]);

            if(quot < temp || first_iteration){

                temp = quot;
                pivot_row = i;
                first_iteration = false;

            }

        }

        int[] arr = {pivot_row, pivot_col};
        return arr;

    }

    private static void rowNormalization(double[][] inMatrix, int[] pivot){

        double pivotElement = inMatrix[pivot[0]][pivot[1]];
        //BigDecimal pivotElement = new BigDecimal(inMatrix[pivot[0]][pivot[1]]);

        for(int i = 0; i < inMatrix[pivot[0]].length; i++){

            inMatrix[pivot[0]][i] = simplifyDouble(inMatrix[pivot[0]][i]/pivotElement).doubleValue();
            //inMatrix[pivot[0]][i] = simplifyDouble((BigDecimal.valueOf(inMatrix[pivot[0]][i]).divide(pivotElement)).doubleValue()).doubleValue();

        }

    }

    private static void rowNormalization(String[][] inMatrix, int[] pivot){
        BigDecimal pivotElement = new BigDecimal(inMatrix[pivot[0]][pivot[1]]);

        for(int i = 0; i < inMatrix[pivot[0]].length; i++){

            //inMatrix[pivot[0]][i] = ((new BigDecimal(inMatrix[pivot[0]][i])).divide(pivotElement, RoundingMode.HALF_EVEN)).toString();
            inMatrix[pivot[0]][i] = String.valueOf(Double.parseDouble(inMatrix[pivot[0]][i])/pivotElement.doubleValue());
            inMatrix[pivot[0]][i] = simplifyStringToBigDecimal(inMatrix[pivot[0]][i]).toString();

        }
    }

    private static void columnNormalization(double[][] inMatrix, int[] pivot){


        for(int i = 0; i < inMatrix.length; i++){

            if(i == pivot[0]){
                continue;
            }

            double factor = inMatrix[i][pivot[1]] * -1.0;
            //BigDecimal factor = new BigDecimal(inMatrix[i][pivot[1]] * -1.0);

            for(int j = 0; j < inMatrix[i].length; j++){

                //inMatrix[i][j] = simplifyDouble(BigDecimal.valueOf(inMatrix[i][j]).add(factor.multiply(BigDecimal.valueOf(inMatrix[pivot[0]][j]))).doubleValue()).doubleValue();
                double factor2 = simplifyDouble(factor * inMatrix[pivot[0]][j]).doubleValue();
                inMatrix[i][j] = simplifyDouble(inMatrix[i][j] + factor2).doubleValue();

            }

        }

    }

    private static void columnNormalization(String[][] inMatrix, int[] pivot){

        for(int i = 0; i < inMatrix.length; i++){

            if(i == pivot[0]){
                continue;
            }

            //double factor = inMatrix[i][pivot[1]] * -1.0;
            BigDecimal factor = (new BigDecimal(inMatrix[i][pivot[1]])).negate();

            for(int j = 0; j < inMatrix[i].length; j++){

                //inMatrix[i][j] = simplifyDouble(BigDecimal.valueOf(inMatrix[i][j]).add(factor.multiply(BigDecimal.valueOf(inMatrix[pivot[0]][j]))).doubleValue()).doubleValue();
                BigDecimal factor2 = simplifyStringToBigDecimal((factor.multiply(new BigDecimal(inMatrix[pivot[0]][j]))).toString());
                //double factor2 = simplifyDouble(factor * inMatrix[pivot[0]][j]).doubleValue();
                inMatrix[i][j] = simplifyStringToBigDecimal(((new BigDecimal(inMatrix[i][j])).add(factor2)).toString()).toString();
                //inMatrix[i][j] = simplifyDouble(inMatrix[i][j] + factor2).doubleValue();

            }

        }

    }

    public static BigDecimal upperPriceLimit(double[][] inMatrix, int res_pos, double[] var_prices){

        BigDecimal return_price = new BigDecimal(0);

        for(int i = 1; i < inMatrix.length; i++){

            for(int j = 0; j < var_prices.length; j++){

                if(inMatrix[i][j] == 1.0){

                    return_price = return_price.add(BigDecimal.valueOf(inMatrix[i][res_pos]).multiply(BigDecimal.valueOf(var_prices[j])));

                }
                
            }

        }

        return return_price;
    }

}
