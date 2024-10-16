package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponseSearch;
import hu.informula.demo.util.MovieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private OmdbApiService omdbApiService;

    @Mock
    private TheMovideDbApiService theMovideDbApiService;

    @Mock
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(movieService, "omdbApiName", "omdb");
        ReflectionTestUtils.setField(movieService, "themoviedbApiName", "moviedb");
    }

    @Test
    void testGetMovies_WhenFoundInCache() {
        final var movieResponse = MovieUtil.createMovieResponse();
        final var expectedResponse = MovieResponseSearch.builder().movieResponses(Collections.singletonList(movieResponse)).build();

        when(redisService.searchInCache(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME)).thenReturn(Collections.singletonList(movieResponse));

        var actualResponse = movieService.getMovies(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        assertEquals(expectedResponse, actualResponse);

        verify(redisService, times(1)).searchInCache(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        verifyNoInteractions(omdbApiService);
        verifyNoInteractions(theMovideDbApiService);
    }

    @Test
    void testGetMovies_FromOmdbApi() {
        final var movieResponse = MovieUtil.createMovieResponse();
        final var expectedResponse = MovieResponseSearch.builder().movieResponses(Collections.singletonList(movieResponse)).build();

        when(redisService.searchInCache(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME)).thenReturn(Collections.emptyList());
        when(omdbApiService.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME)).thenReturn(expectedResponse);

        var actualResponse = movieService.getMovies(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        assertEquals(expectedResponse, actualResponse);

        verify(redisService, times(1)).searchInCache(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        verify(omdbApiService, times(1)).getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        verifyNoInteractions(theMovideDbApiService);
    }

    @Test
    void testGetMovies_FromTheMovieDbApi() {
        final var movieResponse = MovieUtil.createMovieResponse();
        final var expectedResponse = MovieResponseSearch.builder().movieResponses(Collections.singletonList(movieResponse)).build();

        when(redisService.searchInCache(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME)).thenReturn(Collections.emptyList());
        when(theMovideDbApiService.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME)).thenReturn(expectedResponse);

        var actualResponse = movieService.getMovies(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME);
        assertEquals(expectedResponse, actualResponse);

        verify(redisService, times(1)).searchInCache(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME);
        verify(theMovideDbApiService, times(1)).getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME);
        verifyNoInteractions(omdbApiService);
    }

    @Test
    void testGetMovies_InvalidApi() {
        final var invalidApi = "invalidApi";

        when(redisService.searchInCache(MovieUtil.MOVIE_TITLE, invalidApi)).thenReturn(Collections.emptyList());

        var exception = assertThrows(IllegalArgumentException.class, () -> movieService.getMovies(MovieUtil.MOVIE_TITLE, invalidApi));
        assertEquals("Invalid API specified.", exception.getMessage());

        verify(redisService, times(1)).searchInCache(MovieUtil.MOVIE_TITLE, invalidApi);
        verifyNoInteractions(omdbApiService);
        verifyNoInteractions(theMovideDbApiService);
    }
}
