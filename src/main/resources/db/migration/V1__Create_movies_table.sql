CREATE TABLE movies (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        year VARCHAR(4),
                        api VARCHAR(10)
);

CREATE TABLE movie_directors (
                                 movie_id BIGINT,
                                 director VARCHAR(255),
                                 FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);