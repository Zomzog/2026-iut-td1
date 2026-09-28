package iut.nantes.controller

import iut.nantes.Database
import iut.nantes.Movie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriUtils
import java.net.URI
import java.net.URLEncoder

@RestController
class MovieController(val database: Database) {

    @PostMapping("/api/movies")
    fun create(@RequestBody movie: Movie): ResponseEntity<Movie> =
        database.create(movie)
            .let {
                ResponseEntity
                    .created(URI.create("/api/movies/${UriUtils.encode(movie.name, "utf-8")}"))
                    .body(it)
            }


}