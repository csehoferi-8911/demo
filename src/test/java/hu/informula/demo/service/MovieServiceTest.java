package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponse;
import hu.informula.demo.util.MovieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieApiProxy movieApiProxy;

    @InjectMocks
    private MovieService movieService;

    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cacheManager = new ConcurrentMapCacheManager("moviesCache");
    }

    @Test
    void testGetMovies_Cacheable() {
        final var movieResponseList = List.of(MovieUtil.createMovieResponse());

        when(movieApiProxy.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME)).thenReturn(movieResponseList);

        final var resultFirstCall = movieService.getMovies(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        final var resultSecondCall = movieService.getMovies(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);

        verify(movieApiProxy, times(2)).getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        assertThat(resultFirstCall).isEqualTo(movieResponseList);
        assertThat(resultSecondCall).isEqualTo(movieResponseList);
    }

    @Test
    void testGetMovies_NotCachedIfEmpty() {
        final var movieTitle = "UnknownMovie";
        List<MovieResponse> emptyList = new ArrayList<>();

        when(movieApiProxy.getMovieDetails(anyString(), anyString())).thenReturn(emptyList);

        final var result = movieService.getMovies(movieTitle, MovieUtil.OMDB_API_NAME);

        verify(movieApiProxy, times(1)).getMovieDetails(movieTitle, MovieUtil.OMDB_API_NAME);
        assertThat(result).isEmpty();

        assertThat(cacheManager.getCache("moviesCache").get(movieTitle.concat("-").concat(MovieUtil.OMDB_API_NAME))).isNull();
    }

    @Test
    void testGetMovies_CacheableDifferentApi() {
        final var movieResponseList1 = List.of(MovieUtil.createMovieResponse());
        final var movieResponseList2 = List.of(MovieUtil.createMovieResponse());

        when(movieApiProxy.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME)).thenReturn(movieResponseList1);
        when(movieApiProxy.getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME)).thenReturn(movieResponseList2);

        final var resultApi1 = movieService.getMovies(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        final var resultApi2 = movieService.getMovies(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME);

        verify(movieApiProxy, times(1)).getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.OMDB_API_NAME);
        verify(movieApiProxy, times(1)).getMovieDetails(MovieUtil.MOVIE_TITLE, MovieUtil.THE_MOVIE_DB_API_NAME);
        assertThat(resultApi1).isEqualTo(movieResponseList1);
        assertThat(resultApi2).isEqualTo(movieResponseList2);
    }
}
