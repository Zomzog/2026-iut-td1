package iut.nantes

import org.hamcrest.Matchers.endsWith
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

/**
 * Tests fournis pour les exos 10 à 16, un `@Nested` par exo.
 * Ils servent d'exemples pour l'exo 19, où vous écrivez vous-même les tests des exos 17 et 18.
 *
 * La base est réinitialisée avant chaque test : les tests ne dépendent pas les uns des autres.
 */
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class Exercises10Test {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Nested
    @DisplayName("Exo 10 — HelloController")
    inner class Exo10 {

        @Test
        fun hello() {
            mockMvc.get("/hello")
                .andExpect {
                    status { isOk() }
                    content { string("world") }
                }
        }
    }

    @Nested
    @DisplayName("Exo 11 — POST → 201")
    inner class Exo11 {

        @Test
        fun create() {
            mockMvc.post("/api/movies") {
                contentType = MediaType.APPLICATION_JSON
                content = JURASSIC_PARK
            }.andExpect {
                status { isCreated() }
                header { string("Location", endsWith("/api/movies/Jurassic%20Park")) }
                jsonPath("$.name") { value("Jurassic Park") }
                jsonPath("$.rating") { value(91) }
            }
        }
    }

    @Nested
    @DisplayName("Exo 12 — POST → 409")
    inner class Exo12 {

        @Test
        fun conflict() {
            mockMvc.post("/api/movies") {
                contentType = MediaType.APPLICATION_JSON
                content = JURASSIC_PARK
            }.andExpect { status { isCreated() } }

            mockMvc.post("/api/movies") {
                contentType = MediaType.APPLICATION_JSON
                content = JURASSIC_PARK
            }.andExpect { status { isConflict() } }
        }

        @Test
        fun no_conflict() {
            jurrasicParkExist()

            mockMvc.post("/api/movies") {
                contentType = MediaType.APPLICATION_JSON
                content = INCEPTION
            }.andExpect { status { isCreated() } }
        }
    }

    @Nested
    @DisplayName("Exo 13 — GET liste")
    inner class Exo13 {

        @Test
        fun demoGet() {
            // GIVEN
            jurrasicParkExist()

            //WHEN
            mockMvc.get("/api/movies")
                // THEN
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    jsonPath("$[0].name") { value("Jurassic Park") }
                }
        }
    }

    @Nested
    @DisplayName("Exo 14 — GET unitaire")
    inner class Exo14 {

        @Test
        fun found() {
            // GIVEN
            inceptionExist()
            // WHEN
            mockMvc.get("/api/movies/Inception")
                // THEN
                .andExpect {
                    status { isOk() }
                    jsonPath("$.name") { value("Inception") }
                    jsonPath("$.releaseDate") { value(2010) }
                }
        }

        @Test
        fun notFound() {
            mockMvc.get("/api/movies/Dune")
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("Exo 15 — PUT")
    inner class Exo15 {

        @Test
        fun updated() {
            // GIVEN
            inceptionExist()
            // WHEN
            mockMvc.put("/api/movies/Inception") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"name": "Inception", "releaseDate": 2010, "rating": 87, "languages": ["VF", "VO"]}"""
            }.andExpect {
                status { isOk() }
                jsonPath("$.rating") { value(87) }
            }

            mockMvc.get("/api/movies/Inception")
                .andExpect { jsonPath("$.rating") { value(87) } }
        }

        @Test
        fun `nom différent dans le chemin et le body`() {
            mockMvc.put("/api/movies/Inception") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"name": "My Little Pony", "releaseDate": 2010, "rating": 87, "languages": ["VF"]}"""
            }.andExpect { status { isBadRequest() } }
        }

        @Test
        fun notFound() {
            mockMvc.put("/api/movies/Dune") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"name": "Dune", "releaseDate": 2010, "rating": 87, "languages": ["VF"]}"""
            }.andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("Exo 16 — DELETE")
    inner class Exo16 {

        @Test
        fun deleted() {
            // GIVEN
            inceptionExist()
            // WHEN
            mockMvc.delete("/api/movies/Inception")
                // THEN
                .andExpect { status { isNoContent() } }

            mockMvc.get("/api/movies/Inception")
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun notFound() {
            mockMvc.delete("/api/movies/Dune")
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("Exo 17 — GET list filtre")
    inner class Exo17 {

        @Test
        fun demoGet() {
            // GIVEN
            jurrasicParkExist()
            inceptionExist()

            //WHEN
            mockMvc.get("/api/movies?rating=91")
                // THEN
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    jsonPath("$.length()") { value(1) }
                    jsonPath("$[0].name") { value("Jurassic Park") }
                }
        }
    }

    @Nested
    @DisplayName("Exo 18 — GET list translate")
    inner class Exo18 {

        @Test
        fun demoGet() {
            // GIVEN
            jurrasicParkExist()
            inceptionExist()

            //WHEN
            mockMvc.get("/api/movies?rating=8") {
                header(HttpHeaders.ACCEPT_LANGUAGE, "FR-CA")
            }
                // THEN
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    jsonPath("$.length()") { value(1) }
                    jsonPath("$[0].name") { value("Origine") }
                }
        }
    }
}

private fun Exercises10Test.jurrasicParkExist() {
    mockMvc.post("/api/movies") {
        contentType = MediaType.APPLICATION_JSON
        content = JURASSIC_PARK
    }.andExpect { status { isCreated() } }
}

private fun Exercises10Test.inceptionExist() {
    mockMvc.post("/api/movies") {
        contentType = MediaType.APPLICATION_JSON
        content = INCEPTION
    }.andExpect { status { isCreated() } }
}

private const val JURASSIC_PARK =
    """{"name": "Jurassic Park", "rating": 91, "releaseDate": 1993, "languages": ["VO", "VFF", "VFQ"]}"""
private const val INCEPTION =
    """{ "name": "Inception", "releaseDate": 2010, "rating": 8, "languages": [ "VF" ] }"""
