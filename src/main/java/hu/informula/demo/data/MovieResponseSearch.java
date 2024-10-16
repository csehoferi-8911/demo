package hu.informula.demo.data;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record MovieResponseSearch(
        List<MovieResponse> movieResponses
) implements Serializable {}

