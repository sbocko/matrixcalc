package sk.bocko.matrixcalc;

public final class TestUtils {

    private TestUtils() {
    }

    public static String anOperandIndex() {
        return "1-2";
    }

    public static String aBody() {
        return "{\"matrix\":[[1,2],[3.4,4]]}";
    }

    public static String aBinaryOperation() {
        return "add";
    }

    public static String anUnaryOperation() {
        return "sum";
    }

    public static String aRange() {
        return "1-x";
    }
}
