package lk.techtalks.kotlin.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import springfox.documentation.swagger2.annotations.EnableSwagger2
import javax.persistence.*
import javax.validation.constraints.NotNull

@SpringBootApplication
@EnableSwagger2
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

@Entity
@Table(name = "book")
class Book(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id : Long, @NotNull var title : String, var isbn : String? = null, @NotNull var author : String) {
    constructor() : this(0L, "", null, "")
}

interface BookRepository : JpaRepository<Book, Long>

@RestController
@RequestMapping("/books")
class BookController(var bookRepository: BookRepository) {

    @GetMapping
    fun get(@PageableDefault(size = 10, page = 0) pageableDefault: Pageable) = bookRepository.findAll(pageableDefault);

    @PostMapping
    fun post(@RequestBody book : Book) = bookRepository.save(book);

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id : Long) = bookRepository.delete(id);

    @PutMapping("/{id}")
    fun put(@PathVariable id : Long, @RequestBody book: Book) {
        book.id = id;
        bookRepository.save(book)
    }
}
