package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponseSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    @Value("${omdb.api-name}")
    private String omdbApiName;
    @Value("${themoviedb.api-name}")
    private String themoviedbApiName;

    private final OmdbApiService omdbApiService;
    private final TheMovideDbApiService theMovideDbApiService;
    private final RedisService redisService;

    public MovieResponseSearch getMovies(final String movieTitle, final String api) {
        final var cachedMovies = redisService.searchInCache(movieTitle, api);

        if (!cachedMovies.isEmpty()) {
            return MovieResponseSearch.builder().movieResponses(cachedMovies).build();
        }

        MovieResponseSearch movieResponse;

        if (omdbApiName.equalsIgnoreCase(api)) {
            movieResponse = omdbApiService.getMovieDetails(movieTitle, api);
        } else if (themoviedbApiName.equalsIgnoreCase(api)) {
            movieResponse = theMovideDbApiService.getMovieDetails(movieTitle, api);
        } else {
            throw new IllegalArgumentException("Invalid API specified.");
        }

        return movieResponse;
    }

}
