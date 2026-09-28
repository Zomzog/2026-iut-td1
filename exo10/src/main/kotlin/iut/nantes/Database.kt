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

    fun findAll(queryRating: List<Int>?): List<Movie> {
        val all = movies.values.toList()
        return if (queryRating != null) {
            all.filter { it.rating in queryRating }
        } else {
            all
        }
    }

    fun update(movie: Movie): Movie {
        movies[movie.name] = movie
        return movie
    }

    fun delete(name: String) {
        movies.remove(name)
    }
}