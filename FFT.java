public class FFT{

    public static void main(String[] args){

    /*
    input is the coefficients of the polynomial
           a(x) = (a_n)x^n+(a_n-1)x^(n-1)+...+(a_1)x+(a_0)*1
    output is the solution of a(x) for each nth root of unity (see below for defn)
    */
    double[] polynomial = {1., 1., 1.};
    System.out.println("input: 1 + x + x^2");

    Complex[] result = FFT(polynomial);
    int num_solns = result.length;

    System.out.print("Solutions when x is an nth root of unity:\nFFT: ");
    for (int i=0; i<num_solns; i++)
        System.out.print(result[i]+", ");

    System.out.print("\nDFT: ");
    Complex[] result2 = DFT(polynomial);
    for (int i=0; i<result2.length; i++)
        System.out.print(result2[i]+", ");
  }

    /*the input represents the coefficients of some polynomial
    the DFT is going to convert it to point-value form at the complex
    nth roots of unity*/
    public static Complex[] DFT(double[] input){

      int n = input.length;

      //find the closest power of 2 to input length
      int j=2;
      while (n>j){j=j*2;}

      //copy array over into a double 2x2 array
      Complex[] result = new Complex[j];
      //placeholders
      Complex kthRoot, kPow, sum = new Complex(0.,0.);//kth complex nth root of unity

      for(int k=0; k<j; k++){
        //apply transformation!
        kthRoot = Complex.kthNthRootOfUnity(k, j);
        kPow = kthRoot.copy();
        //iteratively calculate y
        for (int p=0; p<n; p++) {
          if (p==0) sum = new Complex(input[p], 0.);//clear sum to first val
          else {
            sum = sum.plus(kPow.scale(input[p]));
               kPow = kPow.times(kPow);//increase powers
             }//end else
           }//end for
           result[k] = sum;
       }//end for
       return result;
    }//end DFT

    /*
    input is the coefficients of the polynomial
            a(x) = (a_n)x^n+(a_n-1)x^(n-1)+...+(a_1)x+(a_0)*1
    output is the solution of a(x) for each nth root of unity

    written with reference to Coreman et al's Introduction to Algorithms
    this fft is recursive, future works could include the faster iterative version
    */
    public static Complex[] FFT(double[] input){

      int inputLength = input.length;
      Complex[] result;

      //base case/ trivial case
      if (inputLength==1) {
        result = new Complex[1];
        result[0] = new Complex(input[0], 0.);
        return result;
      }
      //implicit else--------------------------------------

      //find the closest power of 2 to input length
      int j=2;
      while (inputLength>j){j=j*2;}

      /* Divide and conquer prep: break into smaller arrays

      For a polynomial of form a0+a1*x+a2*x^2+...an*x^n, where n is even:
        evens = a0 + a2*x + a4*x^2 + ... + a_n*x^(n/2)
        odds = a1 + a3*x + a5*x^2 + ... + a_n-1*x^(n/2-1)
      */
      double[] evens = new double[j/2], odds =  new double[j/2];
      for (int k=0; k<inputLength; k++){
        if (k%2==0) evens[k/2] = input[k];
        else odds[k/2] = input[k];
      }

      //divide and conquer!
      Complex[] evensDone = FFT(evens);
      Complex[] oddsDone = FFT(odds);

      int half = j/2;
      result = new Complex[j];

      /*
      Recall: the kth nth root of unity is of the form e^(2pi*i*k/n)
      The principal nth root of unity is where k=1
      */
      Complex principal = Complex.kthNthRootOfUnity(1., (double) j),
              pow = new Complex(1., 0.), // 1.0, for now
              oddsPow; // defined below

      for (int k=0; k<half; k++){
        oddsPow = pow.times(oddsDone[k]);
        result[k] = evensDone[k].plus(oddsPow);
        result[k+half] = evensDone[k].minus(oddsPow);
        pow = pow.times(principal);
      }

      return result;
    }//end FFT
  }//end class
