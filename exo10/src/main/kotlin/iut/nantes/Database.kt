package iut.nantes

import org.springframework.stereotype.Repository

@Repository
class Database {
    private val movies = mutableMapOf<String, Movie>()

    fun create(movie: Movie): Movie {
        movies[movie.name] = movie
        return movie
    }
}