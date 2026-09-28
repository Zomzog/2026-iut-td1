package iut.nantes.controller

import iut.nantes.Database
import iut.nantes.Movie
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
        if (database.getOne(movie.name) == null) {
            database.create(movie)
                .let {
                    ResponseEntity
                        .created(URI.create("/api/movies/${UriUtils.encode(movie.name, "utf-8")}"))
                        .body(it)
                }
        } else {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }

    @GetMapping("/api/movies")
    fun listAll(): ResponseEntity<List<Movie>> =
        database.findAll().let { ResponseEntity.ok(it) }

    @GetMapping("/api/movies/{name}")
    fun findOne(@PathVariable name: String): ResponseEntity<Movie> =
        database.getOne(name)
            ?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

}