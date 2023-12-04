import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

public class Puzzle_04 {
    public static void main(String[] args) {
        HashMap<Integer, Integer[]> winning = new HashMap<>();
        HashMap<Integer, Integer[]> available = new HashMap<>();
        try (InputStream in = new FileInputStream("res/input4.txt")) {
            byte[] bytes = in.readAllBytes();
            String[] lines = new String(bytes).replace("\r", "").split("\n");
            for (String line : lines) {
                String[] parts = line.split(": ");
                int id = Integer.parseInt(parts[0].replace("Card", "").replaceAll(" ", ""));
                String[] nums = parts[1].split("\\| ");
                nums[0] = nums[0].replaceAll("  ", " ");
                nums[1] = nums[1].replaceAll("  ", " ");
                Integer[] win = Arrays.stream(nums[0].split(" ")).map(String::trim).filter(s -> !s.isEmpty()).map(Integer::parseInt).toArray(Integer[]::new);
                Integer[] avail = Arrays.stream(nums[1].split(" ")).map(String::trim).filter(s -> !s.isEmpty()).map(Integer::parseInt).toArray(Integer[]::new);
                winning.put(id, win);
                available.put(id, avail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<Integer, Integer> cardsCount = new HashMap<>();
        for(Integer id: winning.keySet()) {
            HashMap<Integer, Integer> cards = getCardCount(id, winning, available, new HashMap<>());
            for(Integer card: cards.keySet()) {
                cardsCount.put(card, cardsCount.getOrDefault(card, 0) + cards.get(card));
            }
        }
        int sum = cardsCount.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Total number of cards: " + sum);
    }

    public static HashMap<Integer, Integer> getCardCount(int id, HashMap<Integer, Integer[]> winning, HashMap<Integer, Integer[]> available, HashMap<Integer, Integer> cards) {
        if(!winning.containsKey(id)) {
            return cards;
        }
        cards.put(id, cards.getOrDefault(id, 0) + 1);
        Integer[] win = winning.get(id);
        Integer[] avail = available.get(id);
        int i = 1;
        for(Integer num: win) {
            if(Arrays.asList(avail).contains(num)) {
                cards = getCardCount(id + i, winning, available, cards);
                i++;
            }
        }
        return cards;
    }
}