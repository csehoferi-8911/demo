package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieApiProxy implements MovieApiService {

    @Value("${omdb.api-name}")
    private String omdbApiName;
    @Value("${themoviedb.api-name}")
    private String themoviedbApiName;

    private final OmdbApiService omdbApiService;
    private final TheMovideDbApiService theMovideDbApiService;
    private final Map<String, MovieApiService> apiServices = new HashMap<>();

    @PostConstruct
    public void init() {
        apiServices.put(omdbApiName.toLowerCase(), omdbApiService);
        apiServices.put(themoviedbApiName.toLowerCase(), theMovideDbApiService);
    }

    @Override
    public List<MovieResponse> getMovieDetails(final String movieTitle, final String api) {
        final var apiService = apiServices.get(api.toLowerCase());
        if (apiService == null) {
            throw new IllegalArgumentException("Invalid API specified.");
        }
        return apiService.getMovieDetails(movieTitle, api);
    }
}
