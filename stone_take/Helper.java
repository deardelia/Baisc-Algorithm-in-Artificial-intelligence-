public class Helper {
	
	/** 
    * Class constructor.
    */
	public Helper () {}

	/**
	* This method is used to check if a number is prime or not
	* @param x A positive integer number
	* @return boolean True if x is prime; Otherwise, false
	*/
	public static boolean isPrime(int x) {
		
		// TODO Add your code here
		if (x <= 1)
			return false;
		if (x <= 3)
			return true;

		// middle five numbers in below loop
		if (x % 2 == 0 || x % 3 == 0)
			return false;

		for (int i = 5; i * i <= x; i = i + 6)
			if (x % i == 0 || x % (i + 2) == 0)
				return false;

		return true;
	}

	/**
	* This method is used to get the largest prime factor 
	* @param x A positive integer number
	* @return int The largest prime factor of x
	*/
	public static int getLargestPrimeFactor(int x) {

    	// TODO Add your code here
		int i = -1;
		int copyOfInput = x;
		for (i = 2; i <= copyOfInput; i++) {
			if (copyOfInput % i == 0) {
				copyOfInput /= i;
				i--;
			}
		}
		return i;
    }
}