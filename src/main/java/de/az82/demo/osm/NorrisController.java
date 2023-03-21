package de.az82.demo.osm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.util.Objects.requireNonNull;

/**
 * Provides chuck norris facts.
 */
@RestController
public class NorrisController {

    private final List<String> norrisFacts = loadLines("/norris-facts.txt");

    /**
     * Provides a random Chuck Norris fact.
     *
     * @return Chuck Norris fact
     */
    @GetMapping("/norris/fact")
    public String authQuote() {
        return norrisFacts.get(ThreadLocalRandom.current().nextInt(norrisFacts.size()));
    }

    @SuppressWarnings("SameParameterValue")
    private static List<String> loadLines(String location) {
        try (Stream<String> lines = lines(Paths.get(requireNonNull(NorrisController.class.getResource(location)).toURI()))) {
            return lines.toList();
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
