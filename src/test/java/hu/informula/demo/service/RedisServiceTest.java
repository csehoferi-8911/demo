package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponse;
import hu.informula.demo.util.MovieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static hu.informula.demo.data.MovieStatic.CACHE_KEY_PREFIX;
import static org.mockito.Mockito.*;

class RedisServiceTest {

    @Mock
    private RedisTemplate<String, MovieResponse> redisTemplate;

    @Mock
    private ValueOperations<String, MovieResponse> valueOperations;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testSaveToCache() {
        final var movieResponse = MovieUtil.createMovieResponse();
        redisService.saveToCache(movieResponse, MovieUtil.OMDB_API_NAME);
        verify(valueOperations, times(1)).set(CACHE_KEY_PREFIX + "omdb:" + MovieUtil.MOVIE_TITLE.toLowerCase(), movieResponse);
    }

}
