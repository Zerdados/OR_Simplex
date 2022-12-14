//import org.apache.commons.lang3.math.Fraction;
import org.apache.commons.numbers.fraction.BigFraction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtility {

    private static final DecimalFormat df = new DecimalFormat("0.###E0");
    private static BigFraction bf = BigFraction.of(1,4);

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
        return df.format(BigDecimal.valueOf(Double.parseDouble(in)));

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
     * Calculates one simplex iteration in a String matrix, saves result in same matrix. <br>
     * Detailed calculation consists of: Finding the pivot element, normalizing the pivot-row,
     * then normalizing all remaining rows.
     * @param inMatrix String-Matrix containing the simplex tableau to be iterated upon
     * @return 0 if iteration was successful, 1 if simplex tableau was already solved
     */
    public static int simplexIteration(String[][] inMatrix, int p) {

        if(p == 2){
            System.out.println(bf);
            bf = bf.divide(4);
            System.out.println(bf);
            return -1;
        }
        int[] dual_pivot = calculateDualPivot(inMatrix, p);
        if(dual_pivot[0] != -1){
            System.out.println("Its a Dual Simplex! The Pivot element is " + dual_pivot[0] + "|" + dual_pivot[1]);
            rowNormalization(inMatrix, dual_pivot, p);
            System.out.println("Rows normalized");
            columnNormalization(inMatrix, dual_pivot, p);
            System.out.println("Columns normalized");
            return 2;
        } else if(dual_pivot[1] == -1 && dual_pivot[0] != -1){
            System.out.println("Something went wrong!");
            return -1;
        }
        System.out.println("Calculating Pivot");
        int[] pivot = calculatePivot(inMatrix, p);

        System.out.println(pivot[1]);
        if(pivot[1] == -1){
            return 1;
        }

        rowNormalization(inMatrix, pivot, p);
        columnNormalization(inMatrix, pivot, p);
        return 0;

    }


    /**
     * Calculates the pivot element of a dual simplex tableau using two for loops, one for the row and another for the column. <br>
     * @param inMatrix Simplex Tableau
     * @return Pivot element as an int-array. If no element is found, {-1, 0} is returned instead
     */
    private static int[] calculateDualPivot(String[][] inMatrix, int p) {

        BigDecimal temp = new BigDecimal(0);
        //Fraction temp_f = Fraction.getFraction(0);
        BigFraction temp_f = BigFraction.of(0);
        int[] pivot = new int[] {-1, -1};

        for(int i = 0; i < inMatrix.length; i++){

            switch (p){
                case 1:
                    //Fraction f = Fraction.getFraction(inMatrix[i][inMatrix[i].length-1]);
                    BigFraction bf = BigFraction.parse(inMatrix[i][inMatrix[i].length-1]);
                    if(bf.compareTo(temp_f) < 0){
                        temp_f = BigFraction.of(bf.getNumerator(), bf.getDenominator());
                        pivot[0] = i;
                    }
                    break;
                default:
                    BigDecimal bd = new BigDecimal(inMatrix[i][inMatrix[i].length-1]);
                    System.out.println(bd + "|" + temp);
                    if(bd.doubleValue() < temp.doubleValue()){
                        temp = BigDecimal.valueOf(bd.doubleValue());
                        pivot[0] = i;
                    }
            }

        }
        System.out.println(temp.doubleValue() + " " + temp_f.doubleValue());
        if(temp.doubleValue() == 0.0 && temp_f.doubleValue() == 0.0){
            return pivot;
        }

        temp = new BigDecimal(0);
        temp_f = BigFraction.of(0);

        for(int i = 0; i < inMatrix[pivot[0]].length; i++){

            switch (p){
                case 1:
                    BigFraction bf = BigFraction.parse(inMatrix[0][i]);
                    //Fraction f = Fraction.getFraction(inMatrix[0][i]);
                    bf = bf.negate();
                    //f = f.negate();
                    if(BigFraction.parse(inMatrix[pivot[0]][i]).doubleValue() == 0.0){
                        continue;
                    }
                    bf = bf.divide(BigFraction.parse(inMatrix[pivot[0]][i]));
                    if(((bf.compareTo(temp_f) > 0) || temp_f.doubleValue() == 0.0) && (bf.compareTo(BigFraction.of(0)) < 0)){
                        temp_f = BigFraction.of(bf.getNumerator(), bf.getDenominator());
                        pivot[1] = i;
                    }
                    break;
                default:
                    BigDecimal bd = new BigDecimal(inMatrix[0][i]);
                    bd = bd.negate();
                    if(Double.parseDouble(inMatrix[pivot[0]][i]) == 0.0){
                        continue;
                    }
                    bd = simplifyStringToBigDecimal(bd.divide(new BigDecimal(inMatrix[pivot[0]][i]), 100, RoundingMode.HALF_EVEN).toString());
                    //Sets the current row as the new pivot row, if the value of the quotient is either bigger than temp or temp is 0, and if the quotient is negative
                    if((bd.doubleValue() > temp.doubleValue() || temp.doubleValue() == 0.0) && bd.doubleValue() < 0){
                        temp = BigDecimal.valueOf(bd.doubleValue());
                        pivot[1] = i;
                    }
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
            return new int[]{0, -1};
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

        return new int[]{pivot_row, pivot_col};

    }


    /**
     * Calculates the pivot element for a given simplex matrix using the most-negative strategy.
     * @param inMatrix simplex tableau as a String-Matrix
     * @return position of pivot element as int-array, {0, -1} if no element was found
     */
    private static int[] calculatePivot(String[][] inMatrix, int p){

        int pivot_col = 0;
        int pivot_row = 0;
        double temp = 0;
        boolean first_iteration = true;

        for(int i = 0; i < inMatrix[0].length; i++){

            double d;
            switch (p){
                case 1:
                    d = BigFraction.parse(inMatrix[0][i]).doubleValue();
                    break;
                default:
                    d = Double.parseDouble(inMatrix[0][i]);
            }
            if(d < temp){
                temp = d;
                pivot_col = i;
            }

        }

        if(temp == 0) {
            return new int[]{0, -1};
        }

        temp = 0;

        for(int i = 1; i < inMatrix.length; i++){

            double quot;

            if(BigFraction.parse(inMatrix[i][pivot_col]).doubleValue() == 0.0){
                continue;
            }

            switch (p){
                case 1:
                    quot = BigFraction.parse(inMatrix[i][inMatrix[i].length-1]).divide(BigFraction.parse(inMatrix[i][pivot_col])).doubleValue();
                    break;
                default:
                    quot = Double.parseDouble(inMatrix[i][inMatrix[i].length-1])/Double.parseDouble(inMatrix[i][pivot_col]);
            }


            if((quot < temp || first_iteration) && quot > 0){

                temp = quot;
                pivot_row = i;
                first_iteration = false;

            }

        }

        return new int[]{pivot_row, pivot_col};

    }

    /**
     * Normalizes the pivot-row in a simplex tableau. Every element in the row is divided by the pivot-element.
     * @param inMatrix simplex tableau as a String-Matrix
     * @param pivot position of the pivot-element as an int-array
     */
    private static void rowNormalization(String[][] inMatrix, int[] pivot, int p){

        BigDecimal pivotElement = new BigDecimal(1);
        if(p != 1) {
            pivotElement = new BigDecimal(inMatrix[pivot[0]][pivot[1]]);
        }
        BigFraction pivotFraction = BigFraction.parse(inMatrix[pivot[0]][pivot[1]]);

        for(int i = 0; i < inMatrix[pivot[0]].length; i++){

            switch(p){
                case 1:
                    inMatrix[pivot[0]][i] = SimplexFraction.fractionToString(BigFraction.parse(inMatrix[pivot[0]][i]).divide(pivotFraction));
                    System.out.println(SimplexFraction.fractionToString(BigFraction.parse(inMatrix[pivot[0]][i]).divide(pivotFraction)));
                    break;
                default:
                    inMatrix[pivot[0]][i] = ((new BigDecimal(inMatrix[pivot[0]][i])).divide(pivotElement, 100, RoundingMode.HALF_EVEN)).toString();
                    inMatrix[pivot[0]][i] = simplifyStringToBigDecimal(inMatrix[pivot[0]][i]).toString();
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
    private static void columnNormalization(String[][] inMatrix, int[] pivot, int p){

        for(int i = 0; i < inMatrix.length; i++){

            if(i == pivot[0]){
                continue;
            }

            BigDecimal factor = new BigDecimal(0);
            if(p != 1){
                factor = (new BigDecimal(inMatrix[i][pivot[1]])).negate();
            }
            BigFraction f_factor = BigFraction.parse(inMatrix[i][pivot[1]]).negate();

            for(int j = 0; j < inMatrix[i].length; j++){

                switch (p){
                    case 1:
                        BigFraction f_factor2 = f_factor.multiply(BigFraction.parse(inMatrix[pivot[0]][j]));
                        System.out.println(SimplexFraction.fractionToString(BigFraction.parse(inMatrix[i][j]).add(f_factor2)));
                        inMatrix[i][j] = SimplexFraction.fractionToString(BigFraction.parse(inMatrix[i][j]).add(f_factor2));
                        break;
                    default:
                        BigDecimal factor2 = simplifyStringToBigDecimal((factor.multiply(new BigDecimal(inMatrix[pivot[0]][j]))).toString());
                        inMatrix[i][j] = simplifyStringToBigDecimal(((new BigDecimal(inMatrix[i][j])).add(factor2)).toString()).toString();
                }
            }

        }

    }

}
