import java.util.Random;

public class TimeTrial{

  public static final int DEGREE_BOUND = 64;
  public static final int NUM_TEST_POINTS = 10000;

  public static void main(String[] args){
    /*
    Testing data:
      1000 points
      polynomials of degree-bound 16
      Set 20% chance that a coefficient will be 0
      Otherwise coefficient is in the range [0, 1.)
    */

    double[][] polynomials = getTestData();

    Stopwatch stopwatch = new Stopwatch();

    for (int i=0; i<NUM_TEST_POINTS; i++){
      FFT.FFT(polynomials[i]);
    }
    double fftTime = stopwatch.getTimeElapsed();

    stopwatch.restart();
    for (int i=0; i<NUM_TEST_POINTS; i++){
      FFT.DFT(polynomials[i]);
    }
    double dftTime = stopwatch.getTimeElapsed();

    System.out.println("Test points: "+NUM_TEST_POINTS+
        ", Degree bound: "+DEGREE_BOUND);
    System.out.println("FFT time: "+fftTime+" (miliseconds)");
    System.out.println("DFT time: "+dftTime+" (miliseconds)");
    System.out.println("Ratio: "+(fftTime/dftTime));
  }

  private static double[][] getTestData(){
    final double ZERO_RATE = 0.2;
    Random random = new Random();

    double[][] polynomials = new double[NUM_TEST_POINTS][DEGREE_BOUND];

    for (int polyNum=0; polyNum<NUM_TEST_POINTS; polyNum++){
      for (int coef=0; coef<DEGREE_BOUND; coef++) {
        if (random.nextDouble() > ZERO_RATE)
          polynomials[polyNum][coef] = random.nextDouble();
        else
          polynomials[polyNum][coef] = 0.0;
      }
    }
    return polynomials;
  }

  public static void singletonTest() {
    double[] sixteen = new double[DEGREE_BOUND];

    // if it's full, then the result is trivial, so use degBound-1 instead
    for (int i=0; i<DEGREE_BOUND-1; i++) sixteen[i] = 1.; // fill array

    Complex[] result = FFT.FFT(sixteen);
    System.out.print("Solutions when x is an nth root of unity:\nFFT: ");
    for (int i=0; i<DEGREE_BOUND; i++)
        System.out.print(result[i]+", ");
  }

  private static class Stopwatch{
    private double startTime;
    private double endTime = -1.;

    // pre-started
    public Stopwatch(){
      startTime = System.currentTimeMillis();
    }

    public void stop(){
      endTime = System.currentTimeMillis();
    }

    public double getTimeElapsed(){
        if (endTime == -1.) stop();
        return endTime-startTime;
    }

    public String toString(){
      return getTimeElapsed() + "";
    }

    public void restart(){
      startTime = System.currentTimeMillis();
      endTime = -1.;
    }
  }
}
