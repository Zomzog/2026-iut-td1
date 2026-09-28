package iut.nantes

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository

@Repository
class Database {
    private val movies = mutableMapOf<String, Movie>()

    fun create(movie: Movie): Movie {
        movies[movie.name] = movie
        return movie
    }

    fun getOne(name: String): Movie? = movies[name]

    fun findAll(): List<Movie> {
        return movies.values.toList()
    }
}