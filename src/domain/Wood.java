package domain;

public class Wood {
    private final int row, col;
    private final long spawnTime;
    private final int amount;

    public Wood(int row, int col, int amount) {
        this.row = row;
        this.col = col;
        this.amount = amount;
        this.spawnTime = System.currentTimeMillis();
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getAmount() { return amount; }
    public long getSpawnTime() { return spawnTime; }
}