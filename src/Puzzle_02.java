import java.io.FileInputStream;
import java.io.InputStream;

public class Puzzle_02 {
    public static void main(String[] args) {
        int maxRed = 12;
        int maxGreen = 13;
        int maxBlue = 14;
        int sum = 0;
        try(InputStream in = new FileInputStream("res/input2.txt")) {
            byte[] bytes = in.readAllBytes();
            String[] lines = new String(bytes).replace("\r","").split("\n");
            for (String line: lines) {
                String[] parts = line.split(": ");
                String[] rounds = parts[1].split("; ");
                int id = Integer.parseInt(parts[0].replace("Game ", ""));
                int[][] colorsCount = new int[rounds.length][3];
                int roundCount = 0;
                int[] highest = new int[3];
                for(String round: rounds) {
                    String[] colors = round.split(", ");
                    for (String color: colors) {
                        if(color.contains("red")) {
                            colorsCount[roundCount][0] = Integer.parseInt(color.replace(" red", ""));
                            if(colorsCount[roundCount][0] > highest[0]) {
                                highest[0] = colorsCount[roundCount][0];
                            }
                        } else if(color.contains("green")) {
                            colorsCount[roundCount][1] = Integer.parseInt(color.replace(" green", ""));
                            if(colorsCount[roundCount][1] > highest[1]) {
                                highest[1] = colorsCount[roundCount][1];
                            }
                        } else if(color.contains("blue")) {
                            colorsCount[roundCount][2] = Integer.parseInt(color.replace(" blue", ""));
                            if(colorsCount[roundCount][2] > highest[2]) {
                                highest[2] = colorsCount[roundCount][2];
                            }
                        }
                    }
                    roundCount++;
                }
                sum += highest[0] * highest[1] * highest[2];
            }
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
