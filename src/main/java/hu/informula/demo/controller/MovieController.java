package hu.informula.demo.controller;

import hu.informula.demo.data.MovieResponseSearch;
import hu.informula.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{movieTitle}")
    public ResponseEntity<MovieResponseSearch> getMovieInfo(@PathVariable String movieTitle,
                                                            @RequestParam String api) {
        return ResponseEntity.ok(movieService.getMovies(movieTitle, api));
    }
}
