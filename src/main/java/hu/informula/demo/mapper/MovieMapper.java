package hu.informula.demo.mapper;

import hu.informula.demo.data.MovieResponse;
import hu.informula.demo.data.omdb.OmdbMovieResponse;
import hu.informula.demo.data.themoviedb.TheMovieDbSearch;
import hu.informula.demo.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieResponse omdbToMovie(OmdbMovieResponse omdbMovieResponse, List<String> directors);
    Movie responseToEntity(MovieResponse movieResponse, String api, String search);
    @Mapping(target = "year", source = "releaseYear")
    MovieResponse moviedbToMovie(TheMovieDbSearch theMovieDbSearch, List<String> directors, String releaseYear);
}
