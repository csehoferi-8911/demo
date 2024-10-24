package hu.informula.demo.service;


import hu.informula.demo.data.themoviedb.TheMovieDbSearchById;
import hu.informula.demo.data.themoviedb.TheMovieDbSearchCast;
import hu.informula.demo.mapper.MovieMapper;
import hu.informula.demo.model.MovieRepository;
import hu.informula.demo.util.MovieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TheMovideDbApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private TheMovideDbApiService theMovideDbApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMovieDetails() {
        final var theMovieDbSearchById = MovieUtil.createTheMovieDbSearchById();
        when(restTemplate.getForEntity(anyString(), eq(TheMovieDbSearchById.class))).thenReturn(new ResponseEntity<>(theMovieDbSearchById, HttpStatus.OK));

        final var theMovieDbSearchCast = MovieUtil.createTheMovieDbSearchCast();
        when(restTemplate.getForEntity(anyString(), eq(TheMovieDbSearchCast.class))).thenReturn(new ResponseEntity<>(theMovieDbSearchCast, HttpStatus.OK));

        final var movieResponse = MovieUtil.createMovieResponse();
        when(movieMapper.moviedbToMovie(any(), anyList(), anyString())).thenReturn(movieResponse);

        final var result = theMovideDbApiService.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME);

        assertEquals(1, result.size());
        verify(movieRepository, times(1)).save(any());
    }

    @Test
    void testGetMovieDetails_EmptyResponse() {
        final var searchResponse = new TheMovieDbSearchById(Collections.emptyList());
        when(restTemplate.getForEntity(anyString(), eq(TheMovieDbSearchById.class))).thenReturn(new ResponseEntity<>(searchResponse, HttpStatus.OK));

        final var result = theMovideDbApiService.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);

        assertEquals(0, result.size());

        verify(movieRepository, never()).save(any());
    }

}
