import org.apache.commons.lang3.math.Fraction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtility {

    private static DecimalFormat df = new DecimalFormat("0.###E0");
    private static Fraction f = Fraction.getFraction(1, 4);

    /**
     * Transforms a double value into a String with exponential notation and a matrissa length of 4. <br>
     * Uses half-even rounding.
     * @param in double to be transformed
     * @return String in exponential notation
     */
    public static String doubleToFourMatrissa(double in){

        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return df.format(BigDecimal.valueOf(in));

    }

    /**
     * Transforms a String representing a decimal into a String with exponential notation and a matrissa
     * length of 4. <br>
     * Uses half-even rounding.
     * @param in String to be transformed
     * @return String in exponential notation
     */
    private static String stringToFourMatrissa(String in){

        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return df.format(BigDecimal.valueOf(Double.valueOf(in)));

    }

    /**
     * Transforms a String representing a decimal in exponential notation, returns its value as a BigDecimal.
     * @param in String in exponential notation
     * @return BigDecimal representing value
     */
    public static BigDecimal fourMatrissaToBigDecimal(String in){

        String[] splitIn = in.split("E");
        splitIn[0] = splitIn[0].replace(",", ".");

        int exponent = Integer.parseInt(splitIn[1]);
        BigDecimal matrissa = new BigDecimal(splitIn[0]);

        return matrissa.multiply(BigDecimal.valueOf(Math.pow(10.0, exponent)));

    }

    /**
     * Takes a double, returns a BigDecimal with same value and matrissa length of four
     * @param in double to be transformed
     * @return BigDecimal
     */
    public static BigDecimal simplifyDouble(double in){

        return fourMatrissaToBigDecimal(doubleToFourMatrissa(in));

    }

    /**
     * Takes a String representing a decimal, returns a BigDecimal with same value and matrissa length of four
     * @param in double to be transformed
     * @return BigDecimal
     */
    public static BigDecimal simplifyStringToBigDecimal(String in){

        return fourMatrissaToBigDecimal(stringToFourMatrissa(in));

    }

    /**
     * Calculates one simplex iteration in a double matrix, saves result in same matrix. <br>
     * Detailed calculation consists of: Finding the pivot element, normalizing the pivot-row,
     * then normalizing all remaining rows.
     * @param inMatrix double-matrix containing the simplex tableau to be iterated upon
     * @return 0 if iteration was successful, 1 if simplex tableau was already solved
     */
    public static int simplexIteration(double[][] inMatrix) {

        int[] pivot = calculatePivot(inMatrix);

        if(pivot[1] == -1){
            return 1;
        }

        rowNormalization(inMatrix, pivot);
        columnNormalization(inMatrix, pivot);
        return 0;

    }

    /**
     * Calculates one simplex iteration in a String matrix, saves result in same matrix. <br>
     * Detailed calculation consists of: Finding the pivot element, normalizing the pivot-row,
     * then normalizing all remaining rows.
     * @param inMatrix String-Matrix containing the simplex tableau to be iterated upon
     * @return 0 if iteration was successful, 1 if simplex tableau was already solved
     */
    public static int simplexIteration(String[][] inMatrix, int p) {

        if(p == 1){
            System.out.println(f);
            f = f.add(Fraction.getFraction(1, 4));
            System.out.println(f);
            return -1;
        }
        int[] dual_pivot = calculateDualPivot(inMatrix);
        if(dual_pivot[0] != -1){
            System.out.println("Its a Dual Simplex! The Pivot element is " + dual_pivot[0] + "|" + dual_pivot[1]);
            rowNormalization(inMatrix, dual_pivot);
            System.out.println("Rows normalized");
            columnNormalization(inMatrix, dual_pivot);
            System.out.println("Columns normalized");
            return 2;
        } else if(dual_pivot[1] == -1 && dual_pivot[0] != -1){
            System.out.println("Something went wrong!");
            return -1;
        }
        System.out.println("Calculating Pivot");
        int[] pivot = calculatePivot(inMatrix);

        System.out.println(pivot[1]);
        if(pivot[1] == -1){
            return 1;
        }

        rowNormalization(inMatrix, pivot);
        columnNormalization(inMatrix, pivot);
        return 0;

    }


    /**
     * Calculates the pivot element of a dual simplex tableau using two for loops, one for the row and another for the column. <br>
     * @param inMatrix Simplex Tableau
     * @return Pivot element as an int-array. If no element is found, {-1, 0} is returned instead
     */
    private static int[] calculateDualPivot(String[][] inMatrix) {

        BigDecimal temp = new BigDecimal(0);
        int[] pivot = new int[] {-1, -1};

        for(int i = 0; i < inMatrix.length; i++){

            BigDecimal bd = new BigDecimal(inMatrix[i][inMatrix[i].length-1]);

            System.out.println(bd + "|" + temp);
            if(bd.doubleValue() < temp.doubleValue()){
                temp = new BigDecimal(bd.doubleValue());
                pivot[0] = i;
            }
        }
        System.out.println(temp.doubleValue());
        if(temp.doubleValue() == 0.0){
            return pivot;
        }

        temp = new BigDecimal(0);

        for(int i = 0; i < inMatrix[pivot[0]].length; i++){

            BigDecimal bd = new BigDecimal(inMatrix[0][i]);
            bd = bd.negate();
            if(Double.parseDouble(inMatrix[pivot[0]][i]) == 0.0){
                continue;
            }
            bd = simplifyStringToBigDecimal(bd.divide(new BigDecimal(inMatrix[pivot[0]][i]), 100, RoundingMode.HALF_EVEN).toString());
            //Sets the current row as the new pivot row, if the value of the quotient is either bigger than temp or temp is 0, and if the quotient is negative
            if((bd.doubleValue() > temp.doubleValue() || temp.doubleValue() == 0.0) && bd.doubleValue() < 0){
                temp = new BigDecimal(bd.doubleValue());
                pivot[1] = i;
            }
        }

        return pivot;
    }

    /**
     * Calculates the pivot element for a given simplex matrix using the most-negative strategy.
     * @param inMatrix simplex tableau as a double-matrix
     * @return position of pivot element as int-array, {0, -1} if no element was found
     */
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

            //TODO Simplify the value before comparing
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


    /**
     * Calculates the pivot element for a given simplex matrix using the most-negative strategy.
     * @param inMatrix simplex tableau as a String-Matrix
     * @return position of pivot element as int-array, {0, -1} if no element was found
     */
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

    /**
     * Normalizes the pivot-row in a simplex tableau. Every element in the row is divided by the pivot-element.
     * @param inMatrix simplex tableau as a double-matrix
     * @param pivot position of the pivot-element as an int-array
     */
    private static void rowNormalization(double[][] inMatrix, int[] pivot){

        double pivotElement = inMatrix[pivot[0]][pivot[1]];

        for(int i = 0; i < inMatrix[pivot[0]].length; i++){

            inMatrix[pivot[0]][i] = simplifyDouble(inMatrix[pivot[0]][i]/pivotElement).doubleValue();

        }

    }

    /**
     * Normalizes the pivot-row in a simplex tableau. Every element in the row is divided by the pivot-element.
     * @param inMatrix simplex tableau as a String-Matrix
     * @param pivot position of the pivot-element as an int-array
     */
    private static void rowNormalization(String[][] inMatrix, int[] pivot){

        BigDecimal pivotElement = new BigDecimal(inMatrix[pivot[0]][pivot[1]]);
        System.out.println(pivotElement);

        for(int i = 0; i < inMatrix[pivot[0]].length; i++){

            inMatrix[pivot[0]][i] = ((new BigDecimal(inMatrix[pivot[0]][i])).divide(pivotElement, 100, RoundingMode.HALF_EVEN)).toString();
            inMatrix[pivot[0]][i] = simplifyStringToBigDecimal(inMatrix[pivot[0]][i]).toString();

        }
    }

    /**
     * Normalizes the pivot-column in a simplex tableau. For every row except the pivot-row, a factor is calculated by
     * multiplying the element that is in that row and the pivot-column with -1.<br>
     * Then, every element of that row gets that factor multiplied with the element in the same column and pivot-row
     * added to it.<br>
     * The result should have every element in the pivot-column except the pivot element as 0.
     * @param inMatrix simplex tableau as a double-matrix
     * @param pivot position of the pivot-element as an int-array
     */
    private static void columnNormalization(double[][] inMatrix, int[] pivot){


        for(int i = 0; i < inMatrix.length; i++){

            if(i == pivot[0]){
                continue;
            }

            double factor = inMatrix[i][pivot[1]] * -1.0;

            for(int j = 0; j < inMatrix[i].length; j++){

                double factor2 = simplifyDouble(factor * inMatrix[pivot[0]][j]).doubleValue();
                inMatrix[i][j] = simplifyDouble(inMatrix[i][j] + factor2).doubleValue();

            }

        }

    }

    /**
     * Normalizes the pivot-column in a simplex tableau. For every row except the pivot-row, a factor is calculated by
     * multiplying the element that is in that row and the pivot-column with -1.<br>
     * Then, every element of that row gets that factor multiplied with the element in the same column and pivot-row
     * added to it.<br>
     * The result should have every element in the pivot-column except the pivot element as 0.
     * @param inMatrix simplex tableau as a String-Matrix
     * @param pivot position of the pivot-element as an int-array
     */
    private static void columnNormalization(String[][] inMatrix, int[] pivot){

        for(int i = 0; i < inMatrix.length; i++){

            if(i == pivot[0]){
                continue;
            }

            BigDecimal factor = (new BigDecimal(inMatrix[i][pivot[1]])).negate();

            for(int j = 0; j < inMatrix[i].length; j++){

                BigDecimal factor2 = simplifyStringToBigDecimal((factor.multiply(new BigDecimal(inMatrix[pivot[0]][j]))).toString());
                inMatrix[i][j] = simplifyStringToBigDecimal(((new BigDecimal(inMatrix[i][j])).add(factor2)).toString()).toString();

            }

        }

    }

}
