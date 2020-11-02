import java.math.BigInteger;
import java.util.Random;


public class KaratsubaMultiplication
{
	
	public static BigInteger karatsuba(final BigInteger factor0, final BigInteger factor1)
	{
		//base cases
		//we want to divide the number of digits in half (based on the base representation)
        //algorithm
		int N = Math.max(factor0.bitLength(), factor1.bitLength());
		if(N == 1) return factor0.multiply(factor1); 
		N = (N / 2) + (N % 2);
		
		 BigInteger b = factor0.shiftRight(N);
         BigInteger a = factor0.subtract(b.shiftLeft(N));
         BigInteger d = factor1.shiftRight(N);
         BigInteger c = factor1.subtract(d.shiftLeft(N));
 
         BigInteger ac    = karatsuba(a, c);
         BigInteger bd    = karatsuba(b, d);
         BigInteger abcd  = karatsuba(a.add(b), c.add(d));
 
         return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2*N));

    }

	public static void main(String[] args)
	{
		//test cases
		if(args.length < 2)
		{
			System.out.println("Need two factors as input");
			return;
		}
		BigInteger factor0 = null;
		BigInteger factor1 = null;
		final Random r = new Random();
		if(args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("rand") || args[0].equalsIgnoreCase("random"))
		{
			factor0 = new BigInteger(r.nextInt(100000), r);
			System.out.println("First factor : " + factor0.toString());
		}
		else
		{
			factor0 = new BigInteger(args[0]);
		}
		if(args[1].equalsIgnoreCase("r") || args[1].equalsIgnoreCase("rand") || args[1].equalsIgnoreCase("random"))
		{
			factor1 = new BigInteger(r.nextInt(100000), r);
			System.out.println("Second factor : " + factor1.toString());
		}
		else
		{
			factor1 = new BigInteger(args[1]);
		}
		final BigInteger result = karatsuba(factor0, factor1);
		System.out.println(result);
		System.out.println(result.equals(factor0.multiply(factor1)));
	}
}