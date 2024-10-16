package hu.informula.demo.service;

import hu.informula.demo.data.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static hu.informula.demo.data.MovieStatic.CACHE_KEY_PREFIX;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, MovieResponse> redisTemplate;

    public void saveToCache(final MovieResponse movieResponse, final String api) {
        redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + api.concat(":") + movieResponse.title().toLowerCase(), movieResponse);
    }

    public List<MovieResponse> searchInCache(String movieTitle, String api) {
        Set<String> keys = redisTemplate.keys(CACHE_KEY_PREFIX + api.concat(":") + "*").stream()
                .filter(s -> s.toLowerCase().contains(movieTitle.toLowerCase()))
                .collect(Collectors.toSet());
        return keys.stream()
                .map(redisTemplate.opsForValue()::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
