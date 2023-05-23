public class Coord {
    double x, y;

    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public Coord diff(Coord c) {
        return new Coord(x - c.x, y - c.y);
    }

    public Coord sum(Coord c) {
        return new Coord(x + c.x, y + c.y);
    }

    public void addBy(Coord c) {
        x += c.x;
        y += c.y;
    }

    public Coord scale(double d) {
        return new Coord(x * d, y * d);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
