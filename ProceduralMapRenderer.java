import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// Class to represent a Tile
class Tile {
    private String name;
    private String imagePath;

    public Tile(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}

// Class to represent the Game Map
class GameMap {
    private ArrayList<ArrayList<Tile>> map;
    private int rows;
    private int cols;
    private Tile[] tileSet;

    public GameMap(int rows, int cols, Tile[] tileSet) {
        this.rows = rows;
        this.cols = cols;
        this.tileSet = tileSet;
        this.map = new ArrayList<>();
        generateMap();
    }

    // Procedurally generate the map using white noise
    private void generateMap() {
        Random random = new Random();
        double[][] noiseMap = generateWhiteNoise(rows, cols, random);

        // Normalize and map noise values to tiles
        for (int row = 0; row < rows; row++) {
            ArrayList<Tile> mapRow = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                double value = noiseMap[row][col];

                if (value < 0.25) {
                    mapRow.add(tileSet[0]);
                } else if (value < 0.5) {
                    mapRow.add(tileSet[1]);
                } else if (value < 0.75) {
                    mapRow.add(tileSet[2]);
                } else {
                    mapRow.add(tileSet[3]);
                }
            }
            map.add(mapRow);
        }
    }

    // Method to generate a simple white noise map
    private double[][] generateWhiteNoise(int width, int height, Random random) {
        double[][] noise = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                noise[x][y] = random.nextDouble() / 0.75f;
            }
        }
        return noise;
    }

    public ArrayList<ArrayList<Tile>> getMap() {
        return map;
    }
}

// Main class to display the map
public class ProceduralMapRenderer {
    public static void main(String[] args) {
        // Define tile set
        Tile grass = new Tile("Grass", "images/grass.png");
        Tile sand = new Tile("Sand", "images/sand.png");
        Tile water = new Tile("Water", "images/water.png");
        Tile mountain = new Tile("Mountain", "images/stone.png");

        Tile[] tileSet = {mountain, sand, grass, water};

        // Create a game map
        int rows = 16;
        int cols = 16;
        GameMap gameMap = new GameMap(rows, cols, tileSet);

        // Render the map
        SwingUtilities.invokeLater(() -> createAndShowGUI(gameMap));
    }

    private static void createAndShowGUI(GameMap gameMap) {
        ArrayList<ArrayList<Tile>> map = gameMap.getMap();
        int rows = map.size();
        int cols = map.get(0).size();

        JFrame frame = new JFrame("Procedural Map Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(64 * rows, 64 * cols);

        // Create a panel with a grid layout
        JPanel panel = new JPanel(new GridLayout(rows, cols));

        for (ArrayList<Tile> row : map) {
            for (Tile tile : row) {
                JLabel label = new JLabel();
                label.setIcon(new ImageIcon(tile.getImagePath()));
                panel.add(label);
            }
        }

        frame.add(panel);
        frame.setVisible(true);
    }
}
