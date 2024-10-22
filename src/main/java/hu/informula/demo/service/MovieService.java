package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponseSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieApiProxy movieApiProxy;
    private final RedisService redisService;

    public MovieResponseSearch getMovies(final String movieTitle, final String api) {
        final var cachedMovies = redisService.searchInCache(movieTitle, api);

        if (!cachedMovies.isEmpty()) {
            return MovieResponseSearch.builder().movieResponses(cachedMovies).build();
        }

        return movieApiProxy.getMovieDetails(movieTitle, api);
    }
}
