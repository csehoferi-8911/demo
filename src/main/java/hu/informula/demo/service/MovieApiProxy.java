package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponseSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieApiProxy implements MovieApiService {

    @Value("${omdb.api-name}")
    private String omdbApiName;
    @Value("${themoviedb.api-name}")
    private String themoviedbApiName;

    private final OmdbApiService omdbApiService;
    private final TheMovideDbApiService theMovideDbApiService;

    @Override
    public MovieResponseSearch getMovieDetails(String movieTitle, String api) {
        if (omdbApiName.equalsIgnoreCase(api)) {
            return omdbApiService.getMovieDetails(movieTitle, api);
        } else if (themoviedbApiName.equalsIgnoreCase(api)) {
            return theMovideDbApiService.getMovieDetails(movieTitle, api);
        } else {
            throw new IllegalArgumentException("Invalid API specified.");
        }
    }
}