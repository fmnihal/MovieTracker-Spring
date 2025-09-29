package com.example.movietracker.service;

import com.example.movietracker.model.Movie;
import com.example.movietracker.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
import java.util.*;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${movies.initial.count:100}")
    private int initialCount;

    @Value("${tmdb.api.key:}")
    private String tmdbApiKey;

    private static final String TMDB_IMAGE_BASE = "https://image.tmdb.org/t/p/w500";

    public MovieService(MovieRepository movieRepository, RestTemplate restTemplate) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
    }

    public Page<Movie> findAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public void fetchAndSaveInitialMovies() {
        if (movieRepository.count() > 0) return; // already loaded
        List<Movie> movies = new ArrayList<>();
        try {
            if (tmdbApiKey != null && !tmdbApiKey.isBlank()) {
                // fetch from TMDB discover endpoint paging until we have initialCount or run out
                int page = 1;
                while (movies.size() < initialCount) {
                    String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + tmdbApiKey + "&page=" + page;
                    Map resp = restTemplate.getForObject(url, Map.class);
                    if (resp == null) break;
                    List<Map<String, Object>> results = (List<Map<String, Object>>) resp.get("results");
                    if (results == null || results.isEmpty()) break;
                    for (Map<String, Object> r : results) {
                        if (movies.size() >= initialCount) break;
                        String tmdbId = String.valueOf(r.get("id"));
                        if (movieRepository.findByExternalId(tmdbId).isPresent()) continue;
                        String title = (String) r.getOrDefault("title", r.get("name"));
                        String overview = (String) r.getOrDefault("overview", "");
                        String posterPath = (String) r.get("poster_path");
                        String posterUrl = posterPath != null ? TMDB_IMAGE_BASE + posterPath : null;
                        String releaseDate = (String) r.getOrDefault("release_date", "");
                        movies.add(new Movie(tmdbId, title, overview, posterUrl, releaseDate));
                    }
                    page++;
                    // safety: don't go infinite if TMDB limit; break at 20 pages
                    if (page > 20) break;
                }
            } else {
                // fallback: load sample JSON from resources
                InputStream is = getClass().getResourceAsStream("/data/movies_sample.json");
                if (is != null) {
                    List<Map<String, Object>> list = objectMapper.readValue(is, List.class);
                    for (Map<String, Object> m : list) {
                        movies.add(new Movie(
                                Objects.toString(m.get("externalId"), null),
                                Objects.toString(m.get("title"), ""),
                                Objects.toString(m.get("overview"), ""),
                                Objects.toString(m.get("posterUrl"), ""),
                                Objects.toString(m.get("releaseDate"), "")
                        ));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!movies.isEmpty()) {
            movieRepository.saveAll(movies);
        }
    }
}
