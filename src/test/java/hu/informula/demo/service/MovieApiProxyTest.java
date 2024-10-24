package hu.informula.demo.service;

import hu.informula.demo.util.MovieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MovieApiProxyTest {

    @Mock
    private OmdbApiService omdbApiService;

    @Mock
    private TheMovideDbApiService theMovideDbApiService;

    @InjectMocks
    private MovieApiProxy movieApiProxy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(movieApiProxy, "omdbApiName", MovieUtil.OMDB_API_NAME);
        ReflectionTestUtils.setField(movieApiProxy, "themoviedbApiName", MovieUtil.THE_MOVIE_DB_API_NAME);
        movieApiProxy.init();
    }

    @Test
    void testGetMovieDetails_OMDbApi() {
        final var movieResponseList = List.of(MovieUtil.createMovieResponse());

        when(omdbApiService.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME)).thenReturn(movieResponseList);

        final var result = movieApiProxy.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);

        verify(omdbApiService, times(1)).getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        assertThat(result).isEqualTo(movieResponseList);
    }

    @Test
    void testGetMovieDetails_TheMovieDbApi() {
        final var movieResponseList = List.of(MovieUtil.createMovieResponse());

        when(theMovideDbApiService.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME)).thenReturn(movieResponseList);

        final var result = movieApiProxy.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME);

        verify(theMovideDbApiService, times(1)).getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME);
        assertThat(result).isEqualTo(movieResponseList);
    }

    @Test
    void testGetMovieDetails_InvalidApi() {
        String invalidApi = "InvalidApi";

        assertThatThrownBy(() -> movieApiProxy.getMovieDetails(MovieUtil.MOVIE_TITLE, invalidApi))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid API specified.");

        verifyNoInteractions(omdbApiService, theMovideDbApiService);
    }
}
