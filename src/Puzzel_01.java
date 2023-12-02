import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class Puzzel_01 {
    public static void main(String[] args) {
        List<String> input = List.of(new String[]{
                "one",
                "two",
                "three",
                "four",
                "five",
                "six",
                "seven",
                "eight",
                "nine"
        });
        HashMap<String, Integer> inputMap = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            inputMap.put(input.get(i), i + 1);
        }

        try (InputStream in = new FileInputStream("input1.txt")) {
            byte[] bytes = in.readAllBytes();
            String[] lines = new String(bytes).split("\n");
            int sum = 0;
            int count = 0;
            for (String line : lines) {
                int firstNumStringIndex = Integer.MAX_VALUE;
                int firstNumString = -1;
                int lastNumStringIndex = Integer.MIN_VALUE;
                int lastNumString = -1;
                for (String s : input) {
                    if (line.contains(s)) {
                        if(line.indexOf(s) < firstNumStringIndex) {
                            firstNumStringIndex = line.indexOf(s);
                            firstNumString = inputMap.get(s);
                        }
                        if(line.lastIndexOf(s) > lastNumStringIndex) {
                            lastNumStringIndex = line.lastIndexOf(s);
                            lastNumString = inputMap.get(s);
                        }
                    }
                }

                int firstNum = -1;
                int lastNum = -1;
                for (int i = 0; i < line.length(); i++) {
                    try{
                        int num = Integer.parseInt(line.substring(i, i + 1));
                        if(firstNum == -1) {
                            if(firstNumStringIndex < i) {
                                firstNum = firstNumString;
                            } else {
                                firstNum = num;
                            }
                        }
                        if(lastNumStringIndex > i) {
                            lastNum = lastNumString;
                        } else {
                            lastNum = num;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
                count++;
                int finalNum = firstNum * 10 + lastNum;
                System.out.println(count + ": " + finalNum);
                sum += finalNum;
            }
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
