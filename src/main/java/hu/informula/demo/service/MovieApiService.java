package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponse;

import java.util.List;

public interface MovieApiService {
    List<MovieResponse> getMovieDetails(String movieTitle, String api);
}
