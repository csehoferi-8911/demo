package hu.informula.demo.util;

import hu.informula.demo.data.MovieResponse;
import hu.informula.demo.data.MovieResponseSearch;
import hu.informula.demo.data.omdb.OmdbMovieResponse;
import hu.informula.demo.data.omdb.OmdbSearchResponse;
import hu.informula.demo.data.themoviedb.TheMovieDbCast;
import hu.informula.demo.data.themoviedb.TheMovieDbSearch;
import hu.informula.demo.data.themoviedb.TheMovieDbSearchById;
import hu.informula.demo.data.themoviedb.TheMovieDbSearchCast;

import java.util.Collections;
import java.util.List;

public class MovieUtil {

    public final static String MOVIE_TITLE = "Rambo";
    public final static String MOVIE_YEAR = "1989";
    public final static String MOVIE_IMDB_ID = "1";
    public final static String MOVIE_DIRECTOR = "Ted Kotcheff";
    public final static String MOVIE_DEPARTMENT = "Writing";
    public final static String OMDB_API_NAME = "omdb";
    public final static String THE_MOVIE_DB_API_NAME = "moviedb";
    public final static String TEST_API_KEY = "test_api_key";

    public static MovieResponse createMovieResponse() {
        return MovieResponse.builder().title(MOVIE_TITLE).year(MOVIE_YEAR).directors(Collections.emptyList()).build();
    }

    public static OmdbMovieResponse createOmdbMovieResponse() {
        return OmdbMovieResponse.builder().title(MOVIE_TITLE).year(MOVIE_YEAR).imdbId(MOVIE_IMDB_ID).director(MOVIE_DIRECTOR).build();
    }

    public static OmdbSearchResponse createOmdbSearchResponse() {
        return OmdbSearchResponse.builder().omdbMovieResponses(List.of(createOmdbMovieResponse())).build();
    }

    public static TheMovieDbSearchById createTheMovieDbSearchById() {
        return TheMovieDbSearchById.builder().ids(List.of(createTheMovieDbSearch())).build();
    }

    public static TheMovieDbSearch createTheMovieDbSearch() {
        return TheMovieDbSearch.builder().id(MOVIE_IMDB_ID).title(MOVIE_TITLE).year(MOVIE_YEAR).build();
    }

    public static TheMovieDbSearchCast createTheMovieDbSearchCast() {
        return TheMovieDbSearchCast.builder().cast(List.of(createTheMovieDbCast())).build();
    }

    public static TheMovieDbCast createTheMovieDbCast() {
        return TheMovieDbCast.builder().name(MOVIE_DIRECTOR).department(MOVIE_DEPARTMENT).build();
    }

    public static MovieResponseSearch createMovieResponseSearch() {
        return MovieResponseSearch.builder().movieResponses(List.of(createMovieResponse())).build();
    }
}
