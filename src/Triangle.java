public final class Triangle {
    private final double a;
    private final double b;
    private final double c;

    public Triangle(double a, double b, double c) {
        validateSide(a, "a");
        validateSide(b, "b");
        validateSide(c, "c");

        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException(
                    "The specified sides cannot form a triangle"
            );
        }

        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double perimeter() {
        return a + b + c;
    }

    public double area() {
        double semiperimeter = perimeter() / 2.0;

        return Math.sqrt(
                semiperimeter
                        * (semiperimeter - a)
                        * (semiperimeter - b)
                        * (semiperimeter - c)
        );
    }

    public boolean isEquilateral() {
        return Double.compare(a, b) == 0
                && Double.compare(b, c) == 0;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Triangle triangle)) {
            return false;
        }

        return Double.compare(a, triangle.a) == 0
                && Double.compare(b, triangle.b) == 0
                && Double.compare(c, triangle.c) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * (31 * Double.hashCode(a) + Double.hashCode(b))
                + Double.hashCode(c);
    }

    private static void validateSide(double side, String sideName) {
        if (!Double.isFinite(side) || side <= 0) {
            throw new IllegalArgumentException(
                    "Triangle side " + sideName +
                            " must be a positive finite number"
            );
        }
    }
}