package hu.informula.demo.data.themoviedb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TheMovieDbSearchById(@JsonProperty("results")List<TheMovieDbSearch> ids) {}

