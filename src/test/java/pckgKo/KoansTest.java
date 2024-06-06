package pckgKo;

import org.junit.*;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static pckgKo.Koans.*;
import static org.junit.Assert.*;
import static pckgKo.Koans.RealFunction.*;


public class KoansTest {
    private static final double d = 1e-10;

    @Test
    public void testPlusOne(){
        //Arrange
        int[] testarray = new int[]{1,2,0,-1};
        //Act
        mapArray(testarray,plusOne);
        //Assert
        assertArrayEquals(new int[]{2,3,1,0}, testarray);
    }

    @Test
    public void testSquare(){
        //Arrange
        int[] testArray = new int[]{1,2,0,-1};
        //Act
        mapArray(testArray,square);
        //Assert
        assertArrayEquals(new int[]{1,4,0,1}, testArray);
    }

    @Test
    public void testPISupplier(){
        //Arrange & Act
        double[] result = fillArray(5,PISupplier);
        //Assert
        assertArrayEquals(new double[]{Math.PI,Math.PI,Math.PI,Math.PI,Math.PI},result,d);
    }

    @Test
    public void testRandomSupplier(){
        //Arrange & Act
        double[] result = fillArray(5,RandomSupplier);
        //Assert
        assertArrayEquals(new double[]{0.7275636800328681,0.6832234717598454,0.30871945533265976,0.27707849007413665,0.6655489517945736},result,1e-10);
    }

    @Test
    public void testIterateFunctionPlusOne(){
        //Arrange & Act
        int[] result = iterateFunction(5,0,plusOne);
        //Assert
        assertArrayEquals(new int[]{0,1,2,3,4},result);
    }

    @Test
    public void testIterateFunctionTimesTwo(){
       //Arrange & Act
        int[] result = iterateFunction(5,2,timesTwo);
        //Assert
        assertArrayEquals(new int[]{2,4,8,16,32},result);
    }

    @Test
    public void testMinInt(){
        //Arrange
        Integer[] intArray = {4, 1, -1, 2, 0};          //Objekttyp Integer, da sich T auf OBJEKTE bezieht
        Comparator<Integer> intComparator = Integer::compareTo;
        //Act
        int intMin = min(intArray,intComparator);
        //Assert
        assertEquals(-1,intMin);
    }

    @Test
    public void testMinString(){
        //Arrange
        String[] stringArray = {"Hallo", "Welt", "Java", "Lambda", "Koan","lol"};
        Comparator<String> stringComparator = (a,b) -> a.length() - b.length(); //Negative comperator value --> b is greater
        //Act
        String minString = min(stringArray,stringComparator);
        //Assert
        assertEquals("lol",minString);
    }

    @Test
    public void testMultiplier(){
        //Arrange & Act
        Function<Double, Double> multiplier = createMultiplier(5); //Multiplier is created by using the return of the function CreateMultiplier
        double result = multiplier.apply((double)10);
        //Assert
        assertEquals(50,result,d);
    }

    @Test
    public void testForEachArray(){
        //Arrange
        String[] strings = {"Hallo", "Welt","!"};
        StringBuilder sb = new StringBuilder();
        Consumer<String> stringConsumer = s -> sb.append(s).append(" ");
        //Act
        forEachArray(strings,stringConsumer);
        //Assert
        assertEquals("Hallo Welt ! ",sb.toString());
    }

    @Test
    public void testDuplicateChecker(){
        //Arrange
        Predicate<String> p =  duplicateChecker();
        //Act & Assert
        assertFalse(p.test("Hello"));
        assertFalse(p.test("World"));
        assertFalse(p.test("!"));
        assertTrue(p.test("World"));
    }

    /*
    Tests for RealFunction functional interface
    .apply(x) gets the function value on x
     */
    @Test
    public void testConstant() {
        //Arrange & Act
        RealFunction constantFunction = RealFunction.constant(5);
        //Assert
        assertEquals(5, constantFunction.apply(0), d);
    }

    @Test
    public void testLinear() {
        //Arrange & Act
        RealFunction linearFunction = RealFunction.linear(2,3);
        //Assert
        assertEquals(5,linearFunction.apply(1),d);
    }

    @Test
    public void testSine() {
        //Arrange & Act
        RealFunction sineFunction1 = RealFunction.sine(2, Math.PI);
        //Assert
        assertEquals(0, sineFunction1.apply(0), d);
        //Arrange & Act
        RealFunction sineFunction2 = RealFunction.sine(2, 1);
        //Assert
        assertEquals(2, sineFunction2.apply(Math.PI / 2), d);
    }

    @Test
    public void testExp() {
        //Arrange & Act
        RealFunction expFunction = exp();
        assertEquals(1, expFunction.apply(0),d);
        assertEquals(Math.E, expFunction.apply(1),d);
        assertEquals(1/Math.E, expFunction.apply(-1),d);
    }

    @Test
    public void testAddTo() {
        //Arrange
        RealFunction  f = RealFunction.linear(1,0);
        RealFunction g = RealFunction.sine(1,1);
        //Act
        RealFunction h = f.addTo(g);
        //Assert
        assertEquals(0+Math.sin(0), h.apply(0),d);
        assertEquals(1+Math.sin(1), h.apply(1),d);
    }

    @Test
    public void testComposeWith() {
        //Arrange
        RealFunction f = exp();
        RealFunction g = RealFunction.linear(2,1);
        //Act
        RealFunction h = f.composeWith(g);
        //Assert
        assertEquals(f.apply(g.apply(1)),h.apply(1),d); //Checks if h applied to 1 is the same as g applied and then f applied to the result
    }

    @Test
    public void testMultiplyWith() {
        //Arrange
        RealFunction  f = RealFunction.linear(1,0);
        RealFunction exp = exp();
        RealFunction sine = RealFunction.sine(1,1);
        //Act
        RealFunction h = f.multiplyWith(exp,sine);
        //Assert
        assertEquals(f.apply(1)*exp().apply(1)*sine.apply(1), h.apply(1),d); //Expected: Apply every Function then multiply, Actual: Same but with multiplyWith method
    }

    @Test
    public void testApproxDiff() {
        double h = 1e-5;
        //Arrange
        RealFunction f = RealFunction.sine(1,0);
        //Act
        RealFunction res = f.approxDiff(h);
        //Assert
        assertEquals(Math.cos(0),res.apply(5),1e-5);
    }

    @Test
    public void testMax() {
        //Arrange
        RealFunction f = RealFunction.linear(1,0);
        RealFunction exp = exp();
        RealFunction sin = RealFunction.sine(1,1);
        //Act
        RealFunction res = f.max(exp,sin);
        //Assert
        assertEquals(Math.max(f.apply(1),Math.max(exp.apply(1), sin.apply(1))),res.apply(1),d);
    }



}
