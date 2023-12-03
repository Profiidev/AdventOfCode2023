import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Puzzle_03 {
    public static void main(String[] args) {
        final List<Character> chars = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
        List<Integer[]> numbers = new ArrayList<>();
        HashMap<Integer, List<Integer>> specialChars = new HashMap<>();
        try (InputStream in = new FileInputStream("res/input3.txt")) {
            byte[] bytes = in.readAllBytes();
            String[] lines = new String(bytes).split("\n");
            int lineCounter = 0;
            for(String line: lines) {
                String number = "";
                List<Integer> specialCharsInLine = new ArrayList<>();
                int charCounter = 0;
                for (char c : line.toCharArray()) {
                    if (chars.contains(c)) {
                        number += c;
                    } else {
                        if (!number.equals("")) {
                            numbers.add(new Integer[]{Integer.parseInt(number), lineCounter, charCounter - number.length(), charCounter - 1});
                            number = "";
                        }
                        /*if(c != '.' && c != '\n' && c != '\r') {
                            specialCharsInLine.add(charCounter);
                        }*/
                        if(c == '*') {
                            specialCharsInLine.add(charCounter);
                        }
                    }
                    charCounter++;
                }
                specialChars.put(lineCounter, specialCharsInLine);
                lineCounter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //numbers.forEach(n -> System.out.println(n[0] + " " + n[1] + " " + n[2] + " " + n[3]));
        //specialChars.forEach((k, v) -> System.out.println(k + " " + v));
        int sum = 0;
        for(Integer[] number: numbers) {
            boolean adjacent = false;
            for(int i = 0; i < number[0].toString().length(); i++) {
                if(adjacentToChar(number[1], number[2] + i, specialChars)) {
                    adjacent = true;
                    break;
                }
            }
            if(adjacent) {
                sum += number[0];
            }
        }
        System.out.println(sum);

        sum = 0;
        for (int line: specialChars.keySet()) {
            List<Integer> specialCharsInLine = specialChars.get(line);
            for(int i = 0; i < specialCharsInLine.size(); i++) {
                List<Integer> adjacentNumbers = adjacentToNumber(line, specialCharsInLine.get(i), numbers);
                if(adjacentNumbers.size() == 2) {
                    sum += adjacentNumbers.get(0) * adjacentNumbers.get(1);
                }
            }
        }
        System.out.println(sum);
    }

    public static List<Integer> adjacentToNumber(int line, int pos, List<Integer[]> numbers) {
        List<Integer> adjacentNumbers = new ArrayList<>();
        for(Integer[] number: numbers) {
            if(number[1] == line || number[1] == line - 1 || number[1] == line + 1) {
                if(number[2] - 1 <= pos && number[3] + 1 >= pos) {
                    adjacentNumbers.add(number[0]);
                }
            }
        }
        return adjacentNumbers;
    }

    public static boolean adjacentToChar(int line, int pos, HashMap<Integer, List<Integer>> specialChars) {
        List<Integer> specialCharsInLine = specialChars.get(line);
        for(int i = pos - 1; i <= pos + 1; i++) {
            if(specialCharsInLine.contains(i)) {
                return true;
            }
        }
        if(line != 0) {
            List<Integer> specialCharsInLineBefore = specialChars.get(line - 1);
            for(int i = pos - 1; i <= pos + 1; i++) {
                if(specialCharsInLineBefore.contains(i)) {
                    return true;
                }
            }
        }
        if(line != specialChars.size() - 1) {
            List<Integer> specialCharsInLineAfter = specialChars.get(line + 1);
            for(int i = pos - 1; i <= pos + 1; i++) {
                if(specialCharsInLineAfter.contains(i)) {
                    return true;
                }
            }
        }
        return false;
    }
}
