public class FFT{

   public static void main(String[] args){

     double[] polynomial = {1., 1., 1.};
     Complex[] res2 = FFT(polynomial);
     for(int i=0; i<res2.length; i++){
       System.out.println(res2[i]);
     }
    }

    /*the input represents the coefficients of some polynomial
    the DFT is going to convert it to point-value form at the complex
    nth roots of unity*/
    public static Complex[] DFT(double[] input){

      int n = input.length;

      //find the closest power of 2 to input length
      int n2=n, j=2;
      while (n>j){j=j*2;}
      n2=j;

      //copy array over into a double 2x2 array
      Complex[] result = new Complex[n2];
      //placeholders
      Complex kthRoot, kPow, sum = new Complex(0.,0.);//kth complex nth root of unity

      for(int k=0; k<n2; k++){
         if (k>=n) {
           result[k] = new Complex(0., 0.);
          }//extra spaces -> zeroes
         //apply transformation!
         else {
           kthRoot = Complex.kOverN(k, n2);
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
         }//end else
       }//end for
       return result;
    }//end DFT

    //written with reference to Coreman et al's Introduction to Algorithms
    //this fft is recursive, future works could include the faster iterative version
    public static Complex[] FFT(double[] input){

      int n = input.length;
      Complex[] result;

      //base case/ trivial case
      if (n==1) {
        result = new Complex[1];
        result[0] = new Complex(input[0], 0.);
        return result;
      }

      //implicit else--------------------------------------

      //find the closest power of 2 to input length
      int j=2;
      while (n>j){j=j*2;}

      Complex principal = Complex.kOverN(1., (double) j),
              pow = new Complex(1., 0.),
              oddsPow;

      //copy over smaller arrays
      double[] evens = new double[j/2], odds =  new double[j/2];
      for (int k=0; k<n; k++){
        if (k%2==0) evens[k/2] = input[k];
        else odds[k/2] = input[k];
      }

      //divide and conquer!
      Complex[] evensDone = FFT(evens);
      Complex[] oddsDone = FFT(odds);


      int half = j/2;
      result = new Complex[j];

      for (int k=0; k<half; k++){
        oddsPow = pow.times(oddsDone[k]);
        result[k] = evensDone[k].plus(oddsPow);
        result[k+half] = evensDone[k].minus(oddsPow);
        pow = pow.times(principal);
      }

      return result;
    }//end FFT

  }//end class
