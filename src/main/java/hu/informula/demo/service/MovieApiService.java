package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponseSearch;

public interface MovieApiService {
    MovieResponseSearch getMovieDetails(String movieTitle, String api);
}
