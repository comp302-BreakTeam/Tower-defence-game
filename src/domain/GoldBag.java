package domain;

public class GoldBag {
    private double x, y;
    private int amount;
    private long spawnTime; // in ms

    public GoldBag(double x, double y, int amount) {
        this.x = x;
        this.y = y;
        this.amount = amount;
        this.spawnTime = System.currentTimeMillis();
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getAmount() { return amount; }
    public long getSpawnTime() { return spawnTime; }
} 