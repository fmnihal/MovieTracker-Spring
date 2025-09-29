package com.example.movie_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.example.movie_tracker.service.MovieService;

@SpringBootApplication
public class MovieTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieTrackerApplication.class, args);
	}

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    // load initial movies on startup (only if DB empty)
    @Bean
    public CommandLineRunner loadMovies(MovieService movieService) {
        return args -> movieService.fetchAndSaveInitialMovies();
    }

}
