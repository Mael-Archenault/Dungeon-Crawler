import java.awt.Graphics;

public interface Displayable {
    void draw(Graphics g, double cameraX, double cameraY, double zoom);
}
