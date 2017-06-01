package beaver.backend.service;

import beaver.backend.entity.Movie;
import beaver.backend.entity.responseType.Info;
import beaver.backend.entity.responseType.ResponseMovies;
import beaver.backend.repository.MovieRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by parda on 2017/5/27.
 */
@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    RestTemplate restTemplate;

    public List<Movie> addMovies() {
        ResponseEntity<ResponseMovies> responseEntity = restTemplate.exchange("http://localhost:3000/", HttpMethod.GET,
                null, new ParameterizedTypeReference<ResponseMovies>() { });
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody().getState().equals("success")) {
            return responseEntity.getBody()
                    .getMovies()
                    .stream()
                    .filter(movie -> movieRepository.findOne(movie.getId()) == null)
                    .map(movie -> {
                        movieRepository.save(movie);
                        return movie;
                    }).collect(Collectors.toList());
        }

        return null;
    }

    public ResponseEntity getMovie(long id) {
        return restTemplate.exchange("http://localhost:3000/movieItem/{id}", HttpMethod.GET,
                null, Map.class, id);
    }

    public List<Movie> getLastest(final int startNum) {
        List<Movie> list = new ArrayList<>();
        movieRepository.findAll()
                .forEach(movie -> {
                    list.add(movie);
                });
        return list.stream()
                .skip(startNum)
                .limit(10)
                .collect(Collectors.toList());
    }
}