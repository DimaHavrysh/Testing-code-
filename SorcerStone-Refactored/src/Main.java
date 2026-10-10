import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private static final Path INPUT_FILE = Path.of("src", "txt", "harry.txt");
    private static final int TOP_WORDS_LIMIT = 30;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        try {
            String content = readText(INPUT_FILE);
            Map<String, Long> wordFrequencies = countWords(content);
            printMostFrequentWords(wordFrequencies, TOP_WORDS_LIMIT);

            long elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;
            System.out.println("------");
            System.out.println("Час виконання: " + elapsedMillis + " мс");
        } catch (IOException exception) {
            System.err.println("Не вдалося прочитати файл: " + INPUT_FILE);
            System.err.println("Перевірте, чи файл існує та чи правильний шлях.");
            System.err.println("Деталі: " + exception.getMessage());
        }
    }

    private static String readText(Path filePath) throws IOException {
        return Files.readString(
                filePath,
                java.nio.charset.Charset.forName("windows-1252")
        );
    }

    private static Map<String, Long> countWords(String content) {
        String normalizedContent = content
                .toLowerCase()
                .replaceAll("[^\\p{L}\\p{M}]+", " ")
                .trim();

        if (normalizedContent.isEmpty()) {
            return Map.of();
        }

        return Arrays.stream(normalizedContent.split("\\s+"))
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    }

    private static void printMostFrequentWords(
            Map<String, Long> wordFrequencies,
            int limit
    ) {
        wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .forEach(entry ->
                        System.out.println(entry.getKey() + " " + entry.getValue()));
    }
}
