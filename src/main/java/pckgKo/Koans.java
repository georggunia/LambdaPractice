package pckgKo;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class Koans {

    /**
     * Method which applies Function f to each element of array
     *
     * @param array array of elements
     * @param f     Function to apply to each element
     */
    public static void mapArray(int[] array, Function<Integer, Integer> f) {
        for (int i = 0; i < array.length; i++)  //Loop over the array
            array[i] = f.apply(array[i]); // apply f on the current element(right side) and insert result into the current element(left side)
    }


    /**
     * Function which returns x+1
     */
    public static Function<Integer, Integer> plusOne = x -> x + 1;

    /**
     * Function which returns x^2
     */
    public static Function<Integer, Integer> square = x -> (x) * (x);

    /**
     * Fills Array with elements
     *
     * @param length length of array
     * @param s      Supplier, which will be used to fill the array
     * @return Array with elements
     */
    public static double[] fillArray(int length, Supplier<Double> s) {
        double[] array = new double[length];
        for (int i = 0; i < array.length; i++)
            array[i] = s.get();
        return array;
    }

    /**
     * Supplier which returns PI
     */
    public static Supplier<Double> PISupplier = () -> Math.PI;

    public static Random rand = new Random(42);    //Generates random number with the seed 42, so we can test if the RNG works in the test class

    /**
     * Supplier, Iterates through the RN pattern with given seed 42
     */
    public static Supplier<Double> RandomSupplier = () -> rand.nextDouble();

    /**
     * Iterates through an array and applies f to each element
     *
     * @param length length of the array
     * @param first  first element of the array
     * @param f      function to apply to each element
     * @return Array with result
     */
    public static int[] iterateFunction(int length, int first, Function<Integer, Integer> f) {
        int[] array = new int[length];
        array[0] = first;

        for (int i = 1; i < length; i++)                  // Iterating through the array, starting with index 1 because index 0 = first
            array[i] = f.apply(array[i - 1]);             // Apply f to the previous element and insert it into current index

        return array;
    }

    /**
     * Function, which returns x*2
     */
    public static Function<Integer, Integer> timesTwo = x -> (x) * 2;

    /**
     * Method, which returns the minimal element of an array
     *
     * @param elements array of elements
     * @param c        Comperator to compare the elements
     * @param <T>      Generic Method, can be applied to T
     * @return minimal element
     */
    public static <T> T min(T[] elements, Comparator<T> c) {
        if (elements == null || elements.length == 0)
            return null;
        T min = elements[0];

        for (int i = 1; i < elements.length; i++)
            if (c.compare(elements[i], min) < 0)
                min = elements[i];

        return min;
    }

    /**
     * Method, which returns a Function, which multiplies something by d
     *
     * @param d Multiplier for the function
     * @return function that multiplies something by d
     */
    public static Function<Double, Double> createMultiplier(double d) { //Methode die eine Functio
        return x -> (x) * (d);
    }

    /**
     * Method, which lets the Consumer consume every String of the given array
     *
     * @param strings array of strings
     * @param c       Consumer
     */
    public static void forEachArray(String[] strings, Consumer<String> c) {
        for (String s : strings)
            c.accept(s);
    }

    /**
     * Method which creates a HasSet and uses it to check if something has been seen already
     * .add returns true if element was successfully added to the HashSet or false otherwise
     * We use the negation of this because we want it to be true if already seen and false if not seen
     *
     * @param <T>
     * @return a Predicate
     */
    public static <T> Predicate<T> duplicateChecker() {
        HashSet<T> cache = new HashSet<>(); // Closure, because HashSet is defined outside of Predicate
        return t -> !cache.add(t); //add returns true if element was added succesfull (element wasnt there before)
    }

    @FunctionalInterface
    public interface RealFunction {
        double apply(double d);

        static RealFunction constant(double c) {
            return x -> c;
        }

        static RealFunction linear(double a, double b) {
            return x -> (a) * (x) + b;
        }

        static RealFunction sine(double a, double f) {
            return x -> (a) * (Math.sin(f * x));
        }

        static RealFunction exp() {
            return Math::exp;
        }

        default RealFunction addTo(RealFunction g) {
            return x -> this.apply(x) + g.apply(x);
        }

        default RealFunction composeWith(RealFunction f) {
            return x -> this.apply(f.apply(x)); //First apply f to x and then apply this to the result
        }

        default RealFunction multiplyWith(RealFunction... funs) {
            return x -> {
                double v = 1;
                for (RealFunction f : funs)
                    v *= f.apply(x); //Apply F to x and then and then multiply with previous result v (starting from 1)

                return v;
            };
        }

        default RealFunction approxDiff(double h){
            return x -> {
                RealFunction lin = RealFunction.linear(1,h);
                RealFunction lin2 = RealFunction.linear(1,0);
                return (lin.apply(x)-lin2.apply(x))/h;
            };
        }

        default RealFunction max(RealFunction... funs){
            return x -> {
                double max = funs[0].apply(x);
                for (RealFunction f : funs)
                    if (f.apply(x) > max)
                        max = f.apply(x);

                return max;
            };
        }
    }
}
