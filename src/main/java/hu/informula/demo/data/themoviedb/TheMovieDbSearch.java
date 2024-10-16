package hu.informula.demo.data.themoviedb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TheMovieDbSearch(String id, String title, @JsonProperty("release_date") String year) {}

