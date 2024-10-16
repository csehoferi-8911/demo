package hu.informula.demo.data.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record OmdbMovieResponse(
        @JsonProperty("imdbID") String imdbId,
        @JsonProperty("Title") String title,
        @JsonProperty("Year") String year,
        @JsonProperty("Director") String director
) {}

