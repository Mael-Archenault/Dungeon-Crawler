public enum Direction {
    SOUTH(0),SOUTHWEST(0), WEST(1), NORTHWEST(2), NORTH(2),NORTHEAST(2), EAST(3),  SOUTHEAST(0);

    private final int value;

    private Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Direction next(){
        Direction[] values = Direction.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}
