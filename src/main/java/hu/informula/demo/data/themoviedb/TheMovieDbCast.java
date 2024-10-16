package hu.informula.demo.data.themoviedb;

import lombok.Builder;

@Builder
public record TheMovieDbCast(String name, String department) {}

