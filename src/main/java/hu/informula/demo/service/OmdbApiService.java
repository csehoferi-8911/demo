package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponse;
import hu.informula.demo.data.MovieResponseSearch;
import hu.informula.demo.data.omdb.OmdbMovieResponse;
import hu.informula.demo.data.omdb.OmdbSearchResponse;
import hu.informula.demo.mapper.MovieMapper;
import hu.informula.demo.model.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OmdbApiService implements MovieApiService {

    @Value("${omdb.apikey}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieMapper movieMapper;
    private final MovieRepository movieRepository;
    private final RedisService redisService;

    @Override
    @Transactional
    public MovieResponseSearch getMovieDetails(final String movieTitle, final String api) {
        final var searchUrl = String.format("http://www.omdbapi.com/?s=%s&apikey=%s", movieTitle, apiKey);
        final var omdbSearchResponse = restTemplate.getForEntity(searchUrl, OmdbSearchResponse.class).getBody();

        final var movieList = new ArrayList<MovieResponse>();
        Optional.ofNullable(omdbSearchResponse)
                .map(OmdbSearchResponse::omdbMovieResponses)
                .ifPresent(omdbMovies -> omdbMovies.forEach(movieResponse -> {
                    final var detailUrl = String.format("http://www.omdbapi.com/?i=%s&apikey=%s", movieResponse.imdbId(), apiKey);
                    final var response = restTemplate.getForEntity(detailUrl, OmdbMovieResponse.class);
                    final var omdbMovieResponse = response.getBody();
                    if (omdbMovieResponse != null) {
                        final var directors = Optional.ofNullable(omdbMovieResponse.director())
                                .map(d -> List.of(d.split(",\\s*")))
                                .orElse(Collections.emptyList());
                        final var movie = movieMapper.omdbToMovie(omdbMovieResponse, directors);
                        movieRepository.save(movieMapper.responseToEntity(movie, api));
                        redisService.saveToCache(movie, api);
                        movieList.add(movie);
                    }
                }));

        return MovieResponseSearch.builder().movieResponses(movieList).build();
    }

}
