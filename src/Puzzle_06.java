import java.io.FileInputStream;
import java.io.InputStream;

public class Puzzle_06 {
    public static void main(String[] args) {
        try(InputStream in = new FileInputStream("res/input6.txt")) {
            byte[] bytes = in.readAllBytes();
            String[] lines = new String(bytes).split("\n");
            long time = Long.parseLong(lines[0].replace("Time:", "").replace(" ", "").trim());
            long distance = Long.parseLong(lines[1].replace("Distance:", "").replace(" ", "").trim());
            int count = 0;
            for (long i = 0; i < time; i++) {
                if(getDistance(i, time) > distance) {
                    count++;
                }
            }
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getDistance(long holdTime, long maxTime) {
        return holdTime * (maxTime - holdTime);
    }
}
