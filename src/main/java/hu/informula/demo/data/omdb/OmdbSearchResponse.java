package hu.informula.demo.data.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record OmdbSearchResponse(@JsonProperty("Search")List<OmdbMovieResponse> omdbMovieResponses) {}

