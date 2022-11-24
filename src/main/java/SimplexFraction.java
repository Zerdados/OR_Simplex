import org.apache.commons.numbers.fraction.BigFraction;

import java.math.BigInteger;

public class SimplexFraction {

    public static String fractionToString(BigFraction bf){
        String output = "";
        if(bf.getNumerator().signum() == 0){
            return "0";
        }
        if(BigInteger.ONE.equals(bf.getDenominator())){
            return bf.getNumerator().toString();
        }
        if(bf.getNumerator().signum() == -1 && bf.getDenominator().signum() == -1){
            output = bf.getNumerator().negate() + "/" + bf.getDenominator().negate();
            if(!bf.getDenominator().equals(BigInteger.valueOf(-1))){
                output += "/" + bf.getDenominator().negate();
            }
            return output;
        }
        return bf.getNumerator() + "/" + bf.getDenominator();
    }



}
