package iut.nantes

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import io.mockk.every
import io.mockk.just
import org.hamcrest.Matchers.endsWith
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest
class Exo19Test {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var database: Database

    @Nested
    inner class CreateTest {

        @Test
        fun success() {
            // GIVEN
            every { database.getOne("Jurassic Park") } returns null
            every { database.create(any())} answers { firstArg() }
            // WHEN

                mockMvc.post("/api/movies") {
                    contentType = MediaType.APPLICATION_JSON
                    content = JURASSIC_PARK_JSON
                }.andExpect {
                    status { isCreated() }
                    header { string("Location", endsWith("/api/movies/Jurassic%20Park")) }
                    jsonPath("$.name") { value("Jurassic Park") }
                    jsonPath("$.rating") { value(91) }
                }
            }

        @Test
        fun conflict() {
            // GIVEN
            every { database.getOne("Jurassic Park") } returns JURASSIC_PARK
            // WHEN

            mockMvc.post("/api/movies") {
                contentType = MediaType.APPLICATION_JSON
                content = JURASSIC_PARK_JSON
            }.andExpect {
                status { isConflict() }
            }
        }
    }

    @Nested
    inner class FindAllTest {

        @Test
        fun findAll() {
            // GIVEN
            every { database.findAll(null) } returns listOf(JURASSIC_PARK)
            //WHEN
            mockMvc.get("/api/movies")
                // THEN
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    jsonPath("$[0].name") { value("Jurassic Park") }
                }
        }

        @Test
        fun findAllWithFilters() {
            // GIVEN
            every { database.findAll(listOf(8)) } returns listOf(JURASSIC_PARK)

            mockMvc.get("/api/movies?rating=8") {
                header(HttpHeaders.ACCEPT_LANGUAGE, "FR-CA")
            }
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
    inner class DeleteTest {
        @BeforeEach
        fun setupDelete() {
            every { database.delete(any()) } returns Unit
        }

        @Test
        fun deleteExisting() {
            // GIVEN
            every { database.getOne(any()) } returns JURASSIC_PARK
            // WHEN
            mockMvc.delete("/api/movies/Inception")
                // THEN
                .andExpect { status { isNoContent() } }
        }

        @Test
        fun deleteNonExisting() {
            // GIVEN
            every { database.getOne(any()) } returns null
            // WHEN
            mockMvc.delete("/api/movies/Inception")
                // THEN
                .andExpect { status { isNotFound() } }
        }
    }
}

private val JURASSIC_PARK = Movie("Jurassic Park", 1993, 91, listOf("VO", "VFF", "VFQ"))
private const val JURASSIC_PARK_JSON =
    """{"name": "Jurassic Park", "rating": 91, "releaseDate": 1993, "languages": ["VO", "VFF", "VFQ"]}"""
