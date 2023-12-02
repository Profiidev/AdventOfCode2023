import java.io.FileInputStream;
import java.io.InputStream;

public class Puzzel_02 {
    public static void main(String[] args) {
        try(InputStream in = new FileInputStream("input2.txt")) {
            byte[] bytes = in.readAllBytes();
            String[] lines = new String(bytes).split("\n");
            for (String line: lines) {
                String[] parts = line.split(": ");
                String[] rounds = parts[1].split("; ");
                int id = Integer.parseInt(parts[0].replace("Game ", ""));
                int[][] colorsCount = new int[rounds.length][3];
                int roundCount = 0;
                for(String round: rounds) {
                    String[] colors = round.split(", ");
                    for (String color: colors) {
                        System.out.println(color + " " + round + " " + id + " " + roundCount);
                        if(color.contains("red")) {
                            colorsCount[roundCount][0] = Integer.parseInt(color.replace(" red", ""));
                        } else if(color.contains("green")) {
                            colorsCount[roundCount][1] = Integer.parseInt(color.replace(" green", ""));
                        } else if(color.contains("blue")) {
                            colorsCount[roundCount][2] = Integer.parseInt(color.replace(" blue", ""));
                        }
                    }
                    roundCount++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
