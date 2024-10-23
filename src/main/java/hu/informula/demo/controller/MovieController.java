package hu.informula.demo.controller;

import hu.informula.demo.data.MovieResponse;
import hu.informula.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{movieTitle}")
    public ResponseEntity<List<MovieResponse>> getMovieInfo(@PathVariable String movieTitle,
                                                            @RequestParam String api) {
        return ResponseEntity.ok(movieService.getMovies(movieTitle.toLowerCase(), api));
    }
}
