package hu.informula.demo.service;

import hu.informula.demo.data.omdb.OmdbMovieResponse;
import hu.informula.demo.data.omdb.OmdbSearchResponse;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OmdbApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private OmdbApiService omdbApiService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(omdbApiService, "apiKey", MovieUtil.TEST_API_KEY);
    }

    @Test
    void testGetMovieDetails_Success() {
        final var searchResponse = MovieUtil.createOmdbSearchResponse();
        when(restTemplate.getForEntity(anyString(), eq(OmdbSearchResponse.class))).thenReturn(new ResponseEntity<>(searchResponse, HttpStatus.OK));

        final var detailedResponse =MovieUtil.createOmdbMovieResponse();
        when(restTemplate.getForEntity(anyString(), eq(OmdbMovieResponse.class))).thenReturn(new ResponseEntity<>(detailedResponse, HttpStatus.OK));

        final var movieResponse = MovieUtil.createMovieResponse();
        when(movieMapper.omdbToMovie(detailedResponse, List.of(MovieUtil.MOVIE_DIRECTOR))).thenReturn(movieResponse);

        final var result = omdbApiService.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);

        assertEquals(1, result.size());
        assertEquals(MovieUtil.MOVIE_TITLE, result.getFirst().title());

        verify(restTemplate, times(1)).getForEntity(anyString(), eq(OmdbSearchResponse.class));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(OmdbMovieResponse.class));
        verify(movieMapper, times(1)).omdbToMovie(detailedResponse, List.of(MovieUtil.MOVIE_DIRECTOR));
        verify(movieRepository, times(1)).save(any());
    }

    @Test
    void testGetMovieDetails_EmptyResponse() {
        final var searchResponse = new OmdbSearchResponse(Collections.emptyList());
        when(restTemplate.getForEntity(anyString(), eq(OmdbSearchResponse.class))).thenReturn(new ResponseEntity<>(searchResponse, HttpStatus.OK));

        final var result = omdbApiService.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);

        assertEquals(0, result.size());

        verify(movieRepository, never()).save(any());
    }
}
