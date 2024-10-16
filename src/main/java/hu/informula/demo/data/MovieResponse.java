package hu.informula.demo.data;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record MovieResponse(
        String title,
        String year,
        List<String> directors
) implements Serializable {}

