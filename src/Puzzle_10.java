import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Puzzle_10 {
    public record Position(int x, int y) {}
    public record PositionD(double x, double y) {}
    public static char[] symblos = "-|FJL7.S".toCharArray();
    public static HashMap<Character, List<Position>> connections = new HashMap<Character, List<Position>>(){{
        put('-', new ArrayList<>(){{
            add(new Position(1, 0));
            add(new Position(-1, 0));
        }});
        put('|', new ArrayList<>(){{
            add(new Position(0, 1));
            add(new Position(0, -1));
        }});
        put('F', new ArrayList<>(){{
            add(new Position(0, 1));
            add(new Position(1, 0));
        }});
        put('J', new ArrayList<>(){{
            add(new Position(0, -1));
            add(new Position(-1, 0));
        }});
        put('L', new ArrayList<>(){{
            add(new Position(0, -1));
            add(new Position(1, 0));
        }});
        put('7', new ArrayList<>(){{
            add(new Position(0, 1));
            add(new Position(-1, 0));
        }});
        put('.', new ArrayList<>());
        put('S', new ArrayList<>(){{
            add(new Position(0, 1));
            add(new Position(0, -1));
            add(new Position(1, 0));
            add(new Position(-1, 0));
        }});
    }};
    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        HashMap<PositionD, Boolean> searched = new HashMap<>();
        HashMap<Position, Integer> pathDist = new HashMap<>();
        try(InputStream in = new FileInputStream("res/input10.txt")) {
            byte[] bytes = in.readAllBytes();
            lines = Arrays.stream(new String(bytes).replace("\r", "").split("\n")).toList();
            Position start = new Position(0, 0);
            for(int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                for(int j = 0; j < line.length(); j++) {
                    if(line.charAt(j) == 'S') {
                        start = new Position(j, i);
                        break;
                    }
                }
            }
            Position current = start;
            Position prev = start;
            pathDist.put(current, 0);
            do {
                for(int i = -1; i < 2; i++) {
                    boolean found = false;
                    for(int j = -1; j < 2; j++) {
                        if((i == 0 && j == 0) || i * j != 0) continue;
                        Position relativePos = new Position(j, i);
                        Position nextPos = new Position(current.x() + j, current.y() + i);
                        if(nextPos.x() < 0 || nextPos.y() < 0 || nextPos.y() >= lines.size() || nextPos.x() >= lines.get(nextPos.y()).length()) continue;
                        char next = lines.get(nextPos.y()).charAt(nextPos.x());
                        if(isConnected(lines.get(current.y()).charAt(current.x()), next, relativePos) && !(nextPos.x == prev.x && nextPos.y == prev.y)) {
                            prev = current;
                            current = nextPos;
                            pathDist.put(current, pathDist.get(prev) + 1);
                            found = true;
                            break;
                        }
                    }
                    if(found) break;
                }
            } while (current.x() != start.x() || current.y() != start.y());
            System.out.println(pathDist.values().stream().mapToInt(Integer::intValue).max().getAsInt() / 2);
            searchPosition(searched, new PositionD(36 + 0.5, 36 + 0.5), lines.get(0).length() - 1.5, lines.size() - 1.5, lines);

            long falseCount = searched.values().stream().filter(b -> !b).count();
            System.out.println(falseCount);
            if(falseCount != 0) return;

            int count = 0;
            List<Position> inside = new ArrayList<>();
            for(int i = 0; i < lines.get(0).length(); i++) {
                for(int j = 0; j < lines.size(); j++) {
                    PositionD posBR = new PositionD(i + 0.5, j + 0.5);
                    PositionD posBL = new PositionD(i - 0.5, j + 0.5);
                    PositionD posTR = new PositionD(i + 0.5, j - 0.5);
                    PositionD posTL = new PositionD(i - 0.5, j - 0.5);
                    if(searched.containsKey(posBR) && searched.containsKey(posBL) && searched.containsKey(posTR) && searched.containsKey(posTL)) {
                        count++;
                        inside.add(new Position(i, j));
                    }
                }
            }
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnected(char current, char next, Position relativePos) {
        List<Position> conns = connections.get(current);
        if(conns.contains(relativePos)) {
            return connections.get(next).contains(new Position(-relativePos.x(), -relativePos.y()));
        }
        return false;
    }

    public static void searchPosition(HashMap<PositionD, Boolean> searched, PositionD pos, double maxX, double maxY, List<String> lines) {
        if(searched.containsKey(pos))
            return;

        boolean b = pos.x > maxX || pos.y > maxY || pos.x < 0 || pos.y < 0;

        searched.put(pos, !(b));
        if(b)
            return;

        PositionD nextUp = new PositionD(pos.x, pos.y - 1);
        PositionD nextDown = new PositionD(pos.x, pos.y + 1);
        PositionD nextLeft = new PositionD(pos.x - 1, pos.y);
        PositionD nextRight = new PositionD(pos.x + 1, pos.y);
        
        if(!isConnected(lines.get((int) (pos.y - 0.5)).charAt((int) (pos.x - 0.5)), lines.get((int) (pos.y - 0.5)).charAt((int) (pos.x + 0.5)), new Position(1, 0)))
            searchPosition(searched, nextUp, maxX, maxY, lines);

        if(!isConnected(lines.get((int) (pos.y + 0.5)).charAt((int) (pos.x - 0.5)), lines.get((int) (pos.y + 0.5)).charAt((int) (pos.x + 0.5)), new Position(1, 0)))
            searchPosition(searched, nextDown, maxX, maxY, lines);

        if(!isConnected(lines.get((int) (pos.y - 0.5)).charAt((int) (pos.x - 0.5)), lines.get((int) (pos.y + 0.5)).charAt((int) (pos.x - 0.5)), new Position(0, 1)))
            searchPosition(searched, nextLeft, maxX, maxY, lines);

        if(!isConnected(lines.get((int) (pos.y - 0.5)).charAt((int) (pos.x + 0.5)), lines.get((int) (pos.y + 0.5)).charAt((int) (pos.x + 0.5)), new Position(0, 1)))
            searchPosition(searched, nextRight, maxX, maxY, lines);
    }
}