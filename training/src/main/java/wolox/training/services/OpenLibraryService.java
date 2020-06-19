package wolox.training.services;

import java.util.Optional;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.models.Book;
import wolox.training.models.DTO.BookDTO;

@Service
public class OpenLibraryService {

    private final RestTemplate restTemplate;

    public OpenLibraryService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public Optional<Book> bookInfo(String isbn) {
        Book bookToReturn = null;

        String url = "https://openlibrary.org/api/books?bibkeys={isbn}&format=json&jscmd=data";
        //make a request
        ResponseEntity<String> response = this.restTemplate
            .exchange(url, HttpMethod.GET, null, String.class, isbn);
        //check status ok
        if (response.getStatusCode() == HttpStatus.OK && !response.getBody().equals("{}")) {

            JSONObject json = new JSONObject(response.getBody());

            JSONObject rootNode = json.getJSONObject(isbn);

            //create a BookDto
            BookDTO bookDTO = new BookDTO(rootNode, isbn);

            //create a book model
            bookToReturn = new Book(bookDTO);

        }

        return Optional.ofNullable(bookToReturn);
    }
}
