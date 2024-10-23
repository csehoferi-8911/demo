package hu.informula.demo.config;

import hu.informula.demo.data.MovieResponse;
import hu.informula.demo.model.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class MovieCacheLoader implements CommandLineRunner {

    @Value("${omdb.api-name}")
    private String omdbApiName;
    @Value("${themoviedb.api-name}")
    private String themoviedbApiName;

    private final MovieRepository movieRepository;
    private final CacheManager cacheManager;

    private final Set<String> cacheKeys = new HashSet<>();

    @Override
    public void run(String... args) {
        Cache cache = cacheManager.getCache("moviesCache");

        if (containsCacheKey(omdbApiName) && containsCacheKey(themoviedbApiName)) {
            log.info("Redis cache is empty, loading data from database...");

            final var moviesFromDb = movieRepository.findAll();

            Map<String, List<MovieResponse>> groupedMovies = moviesFromDb.stream()
                    .collect(Collectors.groupingBy(
                            m -> m.getSearch().toLowerCase().concat("-").concat(m.getApi()),
                            Collectors.mapping(m -> {
                                final var directors = m.getDirectors().stream().toList();
                                return MovieResponse.builder()
                                        .title(m.getTitle())
                                        .year(m.getYear())
                                        .directors(directors)
                                        .build();
                            }, Collectors.toList())
                    ));

            groupedMovies.forEach((cacheKey, movieResponses) -> {
                if (cache != null) {
                    cache.put(cacheKey, movieResponses);
                    cacheKeys.add(cacheKey);
                }
            });

            log.info("Redis cache is uploaded");
        } else {
            log.info("Redis cache already uploaded");
        }
    }

    public boolean containsCacheKey(String apiName) {
        return cacheKeys.stream().noneMatch(key -> key.contains(apiName));
    }
}
