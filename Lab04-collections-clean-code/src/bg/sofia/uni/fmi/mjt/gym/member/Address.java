package bg.sofia.uni.fmi.mjt.gym.member;

public record Address(double longitude, double latitude) {
    public double getDistanceTo(Address other) {
        double dx = other.longitude - longitude;
        double dy = other.latitude - latitude;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
