package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponse;
import hu.informula.demo.data.MovieResponseSearch;
import hu.informula.demo.data.themoviedb.TheMovieDbCast;
import hu.informula.demo.data.themoviedb.TheMovieDbSearchById;
import hu.informula.demo.data.themoviedb.TheMovieDbSearchCast;
import hu.informula.demo.mapper.MovieMapper;
import hu.informula.demo.model.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TheMovideDbApiService {

    @Value("${themoviedb.apikey}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieMapper movieMapper;
    private final MovieRepository movieRepository;
    private final RedisService redisService;

    @Transactional
    public MovieResponseSearch getMovieDetails(final String movieTitle, final String api) {
        final var searchUrl = String.format("https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s&include_adult=true", apiKey, movieTitle);
        final var theMovieDbSearchById = restTemplate.getForEntity(searchUrl, TheMovieDbSearchById.class).getBody();

        final var movieList = new ArrayList<MovieResponse>();
        Optional.ofNullable(theMovieDbSearchById)
                .map(TheMovieDbSearchById::ids)
                .ifPresent(theMovieDbSearches -> theMovieDbSearches.forEach(theMovieDbSearch -> {
                    final var castUrl = String.format("https://api.themoviedb.org/3/movie/%s/credits?api_key=%s", theMovieDbSearch.id(), apiKey);
                    final var castList = restTemplate.getForEntity(castUrl, TheMovieDbSearchCast.class).getBody();
                    List<String> directors = castList.cast().stream()
                            .filter(cast -> "Writing".equals(cast.department()))
                            .map(TheMovieDbCast::name)
                            .toList();
                    String releaseYear = Optional.ofNullable(theMovieDbSearch.year())
                            .filter(date -> date.length() >= 4)
                            .map(date -> date.substring(0, 4))
                            .orElse("N/A");
                    final var movie = movieMapper.moviedbToMovie(theMovieDbSearch, directors, releaseYear);
                    movieRepository.save(movieMapper.responseToEntity(movie, api));
                    redisService.saveToCache(movie, api);
                    movieList.add(movie);
                }));

        return MovieResponseSearch.builder().movieResponses(movieList).build();
    }

}
