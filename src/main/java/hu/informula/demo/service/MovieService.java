package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieApiProxy movieApiProxy;

    @Cacheable(value = "moviesCache", key = "#movieTitle.concat('-').concat(#api)", unless = "#result == null || #result.isEmpty()")
    public List<MovieResponse> getMovies(final String movieTitle, final String api) {
        return movieApiProxy.getMovieDetails(movieTitle, api);
    }
}
