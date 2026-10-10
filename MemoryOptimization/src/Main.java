
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

public class Main {

    private static final Path INPUT_FILE =
            Path.of("src", "txt", "harry.txt");

    private static final int TOP_WORDS_LIMIT = 30;

    private static final Charset FILE_CHARSET =
            Charset.forName("windows-1252");

    private static final Comparator<Map.Entry<String, Long>>
            RESULT_ORDER =
            Map.Entry.<String, Long>comparingByValue(
                    Comparator.reverseOrder()
            ).thenComparing(Map.Entry.comparingByKey());

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        try {
            Map<String, Long> wordFrequencies =
                    countWords(INPUT_FILE);

            long elapsedNanos = System.nanoTime() - startTime;

            printMostFrequentWords(
                    wordFrequencies,
                    TOP_WORDS_LIMIT
            );

            System.out.println("------");
            System.out.printf(
                    Locale.ROOT,
                    "Час обробки: %.3f мс%n",
                    elapsedNanos / 1_000_000.0
            );

        } catch (IOException exception) {
            System.err.println(
                    "Не вдалося прочитати файл: " + INPUT_FILE
            );
            System.err.println(
                    "Перевірте наявність файлу та правильність шляху."
            );
            System.err.println(
                    "Деталі: " + exception.getMessage()
            );
        }
    }

    private static Map<String, Long> countWords(Path filePath)
            throws IOException {

        Map<String, Long> frequencies = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, FILE_CHARSET)) {

            StringBuilder word = new StringBuilder();
            int character;

            while ((character = reader.read()) != -1) {
                char ch = (char) character;
                int type = Character.getType(ch);

                if (Character.isLetter(ch)
                        || type == Character.NON_SPACING_MARK
                        || type == Character.COMBINING_SPACING_MARK
                        || type == Character.ENCLOSING_MARK) {

                    word.append(Character.toLowerCase(ch));

                } else {
                    saveWord(word, frequencies);
                }
            }

            saveWord(word, frequencies);
        }

        return frequencies;
    }

    private static void saveWord(
            StringBuilder word,
            Map<String, Long> frequencies) {

        if (word.length() == 0) {
            return;
        }

        String value = word.toString();

        frequencies.merge(value, 1L, Long::sum);

        word.setLength(0);
    }

    private static void printMostFrequentWords(
            Map<String, Long> frequencies,
            int limit) {

        Comparator<Map.Entry<String, Long>> worstFirst =
                Map.Entry.<String, Long>comparingByValue()
                        .thenComparing(
                                Map.Entry.comparingByKey(
                                        Comparator.reverseOrder()
                                )
                        );

        PriorityQueue<Map.Entry<String, Long>> topWords =
                new PriorityQueue<>(worstFirst);

        for (Map.Entry<String, Long> entry
                : frequencies.entrySet()) {

            topWords.offer(entry);

            if (topWords.size() > limit) {
                topWords.poll();
            }
        }

        List<Map.Entry<String, Long>> result =
                new ArrayList<>(topWords);

        result.sort(RESULT_ORDER);

        for (Map.Entry<String, Long> entry : result) {
            System.out.println(
                    entry.getKey() + " " + entry.getValue()
            );
        }
    }
}
