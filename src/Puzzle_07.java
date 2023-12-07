import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle_07 {
    static String cards = "AKQT98765432J";
    public static void main(String[] args) {
        HashMap<String, Integer> hands = new HashMap<>();
        try(InputStream in = new FileInputStream("res/input7.txt")) {
            byte[] bytes = in.readAllBytes();
            String[] lines = new String(bytes).replace("\r", "").split("\n");
            for(String line: lines) {
                String[] parts = line.split(" ");
                hands.put(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> sortedHands = hands.keySet().stream().sorted(Puzzle_07::greaterThanAsInt).collect(Collectors.toList());
        System.out.println(sortedHands);
        int sum = 0;
        for(int i = 0; i < sortedHands.size(); i++) {
            sum += (i + 1) * hands.get(sortedHands.get(i));
        }
        System.out.println(sum);
    }

    public static int greaterThanAsInt(String hand1, String hand2) {
        if(greaterThan(hand1, hand2)) {
            return 1;
        } else {
            return -1;
        }
    }

    public static boolean greaterThan(String hand1, String hand2) {
        HashMap<Character, Integer> cardCount1 = getCardCountWithJokers(hand1);
        HashMap<Character, Integer> cardCount2 = getCardCountWithJokers(hand2);
        int hand1HighestCount = cardCount1.values().stream().max(Integer::compareTo).get();
        int hand2HighestCount = cardCount2.values().stream().max(Integer::compareTo).get();
        if(hand1HighestCount > hand2HighestCount) {
            return true;
        } else if(hand1HighestCount < hand2HighestCount) {
            return false;
        } else {
            switch (hand1HighestCount) {
                case 1, 4, 5:
                    return higherCard(hand1, hand2);
                case 2:
                    long hand1Pairs = cardCount1.values().stream().filter(i -> i == 2).count();
                    long hand2Pairs = cardCount2.values().stream().filter(i -> i == 2).count();
                    if(hand1Pairs > hand2Pairs) {
                        return true;
                    } else if(hand1Pairs < hand2Pairs) {
                        return false;
                    } else {
                        return higherCard(hand1, hand2);
                    }
                case 3:
                    long hand1HasPair = cardCount1.values().stream().filter(i -> i == 2).count();
                    long hand2HasPair = cardCount2.values().stream().filter(i -> i == 2).count();
                    if(hand1HasPair > 0 && hand2HasPair > 0) {
                        return higherCard(hand1, hand2);
                    } else if(hand1HasPair > 0) {
                        return true;
                    } else if(hand2HasPair > 0) {
                        return false;
                    } else {
                        return higherCard(hand1, hand2);
                    }
            }
        }
        return true;
    }

    public static boolean higherCard(String hand1, String hand2) {
        for(int i = 0; i < hand1.length(); i++) {
            if(higherCard(hand1.charAt(i), hand2.charAt(i)) > 0) {
                return true;
            } else if(higherCard(hand1.charAt(i), hand2.charAt(i)) < 0) {
                return false;
            }
        }
        return true;
    }

    public static int higherCard(char card1, char card2) {
        return Integer.compare(cards.indexOf(card2), cards.indexOf(card1));
    }

    public static HashMap<Character, Integer> getCardCount(String hand) {
        HashMap<Character, Integer> cardCount = new HashMap<>();
        for(char card: hand.toCharArray()) {
            if(cardCount.containsKey(card)) {
                cardCount.put(card, cardCount.get(card) + 1);
            } else {
                cardCount.put(card, 1);
            }
        }
        return cardCount;
    }

    public static HashMap<Character, Integer> getCardCountWithJokers(String hand) {
        HashMap<Character, Integer> cardCount = getCardCount(hand);
        if(!cardCount.containsKey('J')) return cardCount;
        if(cardCount.keySet().size() == 1) return cardCount;
        int jokers = cardCount.get('J');
        cardCount.remove('J');
        char highestCardCount = cardCount.keySet().stream().max(Comparator.comparingInt(cardCount::get)).get();
        cardCount.put(highestCardCount, cardCount.get(highestCardCount) + jokers);
        return cardCount;
    }
}
