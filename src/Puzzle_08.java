import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class Puzzle_08 {
    public static void main(String[] args) {
        record Location(String l, String r) {}
        HashMap<String, Location> locs = new HashMap<>();
        List<String> instructions = new ArrayList<>();
        List<String> starts = new ArrayList<>();

        try(InputStream in = new FileInputStream("res/input8.txt")) {
            byte[] bytes = in.readAllBytes();
            String[] lines = new String(bytes).replace("\r", "").split("\n");
            for(String line: lines) {
                if(line.contains("=")) {
                    String[] parts = line.replace(" ", "").split("=");
                    String[] lr = parts[1].replace("(", "").replace(")", "").split(",");
                    locs.put(parts[0], new Location(lr[0], lr[1]));
                    if(parts[0].endsWith("A")) {
                        starts.add(parts[0]);
                    }
                } else if(line.contains("R")) {
                    instructions.addAll(Arrays.stream(line.trim().split("")).toList());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long count = 0;
        while (starts.stream().anyMatch(k -> !k.endsWith("Z"))) {
            String instruction = instructions.get((int) (count % (long) instructions.size()));
            count++;
            for (int i = 0; i < starts.size(); i++) {
                String start = starts.get(i);
                Location loc = locs.get(start);
                if (instruction.equals("R")) {
                    starts.set(i, loc.r());
                } else {
                    starts.set(i, loc.l());
                }
            }
        }
        System.out.println(count);
    }
}
