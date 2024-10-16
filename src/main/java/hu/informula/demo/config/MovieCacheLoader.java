package hu.informula.demo.config;

import hu.informula.demo.data.MovieResponse;
import hu.informula.demo.model.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

import static hu.informula.demo.data.MovieStatic.CACHE_KEY_PREFIX;

@RequiredArgsConstructor
@Slf4j
@Component
public class MovieCacheLoader implements CommandLineRunner {

    @Value("${omdb.api-name}")
    private String omdbApiName;
    @Value("${themoviedb.api-name}")
    private String themoviedbApiName;

    private final MovieRepository movieRepository;
    private final RedisTemplate<String, MovieResponse> redisTemplate;

    @Override
    public void run(String... args) {
        if (!containsKey(CACHE_KEY_PREFIX + omdbApiName.concat(":")) && !containsKey(CACHE_KEY_PREFIX + themoviedbApiName.concat(":"))) {
            log.info("Redis cache is empty, load datas from database...");

            final var moviesFromDb = movieRepository.findAll();

            moviesFromDb.forEach(m -> {
                final var directors = m.getDirectors().stream().toList();
                final var movieResp = MovieResponse.builder().title(m.getTitle()).year(m.getYear()).directors(directors).build();
                redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + m.getApi() + ":" + movieResp.title().toLowerCase(), movieResp);
            });
            log.info("Redis cache is uploaded");
        } else {
            log.info("Redis cache already uploaded");
        }
    }

    public boolean containsKey(String partOfTheKey) {
        Set<String> keys = redisTemplate.keys(CACHE_KEY_PREFIX + "*");
        return keys.stream().anyMatch(key -> key.contains(partOfTheKey));
    }
}

