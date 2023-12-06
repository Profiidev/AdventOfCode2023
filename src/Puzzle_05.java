import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Puzzle_05 {
    public static void main(String[] args) {
        HashMap<Long, Long> seeds = new HashMap<>();
        HashMap<Long, Long[]> seedToSoil = new HashMap<>();
        HashMap<Long, Long[]> soilToFertilizer = new HashMap<>();
        HashMap<Long, Long[]> fertilizerToWater = new HashMap<>();
        HashMap<Long, Long[]> waterToLight = new HashMap<>();
        HashMap<Long, Long[]> lightToTemperature = new HashMap<>();
        HashMap<Long, Long[]> temperatureToHumidity = new HashMap<>();
        HashMap<Long, Long[]> humidityToLocation = new HashMap<>();
        try(InputStream in = new FileInputStream("res/input5.txt")) {
            byte[] bytes = in.readAllBytes();
            String[] lines = new String(bytes).split("\n");
            String currentMap = "";
            for (String line : lines) {
                line = line.trim();
                if(line.startsWith("seeds: ")) {
                    String[] seed = line.substring(7).split(" ");
                    for(int i = 0; i < seed.length; i += 2) {
                        seeds.put(Long.parseLong(seed[i]), Long.parseLong(seed[i + 1]));
                    }
                } else if(line.startsWith("seed-to-soil map:")) {
                    currentMap = "seed-to-soil";
                } else if(line.startsWith("soil-to-fertilizer map:")) {
                    currentMap = "soil-to-fertilizer";
                } else if(line.startsWith("fertilizer-to-water map:")) {
                    currentMap = "fertilizer-to-water";
                } else if(line.startsWith("water-to-light map:")) {
                    currentMap = "water-to-light";
                } else if(line.startsWith("light-to-temperature map:")) {
                    currentMap = "light-to-temperature";
                } else if(line.startsWith("temperature-to-humidity map:")) {
                    currentMap = "temperature-to-humidity";
                } else if(line.startsWith("humidity-to-location map:")) {
                    currentMap = "humidity-to-location";
                } else if (line.isEmpty()) {
                    currentMap = "";
                } else {
                    Long[] map = Arrays.stream(line.split(" ")).map(Long::parseLong).toArray(Long[]::new);
                    switch (currentMap) {
                        case "seed-to-soil":
                            seedToSoil.put(map[1], map);
                            break;
                        case "soil-to-fertilizer":
                            soilToFertilizer.put(map[1], map);
                            break;
                        case "fertilizer-to-water":
                            fertilizerToWater.put(map[1], map);
                            break;
                        case "water-to-light":
                            waterToLight.put(map[1], map);
                            break;
                        case "light-to-temperature":
                            lightToTemperature.put(map[1], map);
                            break;
                        case "temperature-to-humidity":
                            temperatureToHumidity.put(map[1], map);
                            break;
                        case "humidity-to-location":
                            humidityToLocation.put(map[1], map);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AtomicLong smallest = new AtomicLong(Long.MAX_VALUE);
        AtomicInteger started = new AtomicInteger();
        AtomicInteger finished = new AtomicInteger();
        for(Long seed : seeds.keySet()) {
            if(seed != 3037945983L) continue;
            Thread.startVirtualThread(() -> {
                started.getAndIncrement();
                long small = smallest(seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation, seed, seeds.get(seed), Long.MAX_VALUE);
                if(small < smallest.get()) {
                    smallest.set(small);
                }
                System.out.println("Smallest: " + smallest.get());
                finished.getAndIncrement();
            });
        }
        while (started.get() != finished.get() || started.get() == 0) {
            try {
                System.out.println("Started: " + started.get() + " Finished: " + finished.get());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(smallest.get());
    }

    public static long smallest(HashMap<Long, Long[]> seedToSoil, HashMap<Long, Long[]> soilToFertilizer, HashMap<Long, Long[]> fertilizerToWater, HashMap<Long, Long[]> waterToLight, HashMap<Long, Long[]> lightToTemperature, HashMap<Long, Long[]> temperatureToHumidity, HashMap<Long, Long[]> humidityToLocation, long seed, long range, long smallest) {
        for (long j = seed; j < range + seed; j++) {
            if(j != 3763992295L) continue;
            Long soil = contains(seedToSoil, j);
            Long fertilizer = contains(soilToFertilizer, soil);
            Long water = contains(fertilizerToWater, fertilizer);
            Long light = contains(waterToLight, water);
            Long temperature = contains(lightToTemperature, light);
            Long humidity = contains(temperatureToHumidity, temperature);
            Long location = contains(humidityToLocation, humidity);
            System.out.println("Seed: " + j + " Soil: " + soil + " Fertilizer: " + fertilizer + " Water: " + water + " Light: " + light + " Temperature: " + temperature + " Humidity: " + humidity + " Location: " + location);
            if(location < smallest) {
                smallest = location;
            }
        }
        return smallest;
    }

    public static long contains(HashMap<Long, Long[]> map, Long value) {
        for (Long key : map.keySet()) {
            if(key <= value && key + map.get(key)[2] >= value) {
                System.out.println(key);
                return map.get(key)[0] + value - key;
            }
        }
        return value;
    }
}
