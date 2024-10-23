package hu.informula.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String year;
    @Column(nullable = false)
    private String api;
    @Column(nullable = false)
    private String search;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_directors", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "director")
    private List<String> directors;
}
