package hu.informula.demo.data.themoviedb;

import lombok.Builder;

import java.util.List;

@Builder
public record TheMovieDbSearchCast(List<TheMovieDbCast> cast) {}

