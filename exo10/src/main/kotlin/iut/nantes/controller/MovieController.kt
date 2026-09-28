package iut.nantes.controller

import iut.nantes.Database
import iut.nantes.Movie
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriUtils
import java.net.URI
import java.net.URLEncoder

@RestController
@RequestMapping("/api/movies")
class MovieController(val database: Database) {

    @PostMapping
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

    @GetMapping
    fun listAll(): ResponseEntity<List<Movie>> =
        database.findAll().let { ResponseEntity.ok(it) }

    @GetMapping("/{name}")
    fun findOne(@PathVariable name: String): ResponseEntity<Movie> =
        database.getOne(name)
            ?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @PutMapping("/{name}")
    fun update(@RequestBody movie: Movie, @PathVariable name: String) =
        if (name != movie.name) {
            ResponseEntity.badRequest().body("Name in path and body must be the same")
        } else if (database.getOne(name) == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Movie not found")
        } else {
            database.update(movie).let {
                ResponseEntity.ok(it)
            }
        }

    @DeleteMapping("/{name}")
    fun delete(@PathVariable name: String): ResponseEntity<Unit> =
        database.getOne(name)?.let {
            database.delete(it.name)
            ResponseEntity.noContent().build()
        } ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build()
}