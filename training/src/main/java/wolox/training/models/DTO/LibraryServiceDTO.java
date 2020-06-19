package wolox.training.models.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

public class LibraryServiceDTO {

    private String genre;
    private String authors;
    private String Image;
    private String title;
    private String subtitle;
    private String publisher;
    private String year;
    private Integer pages;
    private String isbn;

    public void mapToDto(
        JsonNode genre,
        JsonNode authors,
        String Image,
        String title,
        String subtitle,
        JsonNode publisher,
        String year,
        Integer pages,
        String isbn) {

        this.setGenre(setPropertiesOfList(genre));
        this.setAuthors(setPropertiesOfList(authors));
        this.setImage(Image);
        this.setTitle(title);
        this.setSubtitle(subtitle);
        this.setPublisher(setPropertiesOfList(publisher));
        this.setYear(year);
        this.setPages(pages);
        this.setIsbn(isbn);

    }

    private String setPropertiesOfList(JsonNode list) {

        List<String> joinProperties = new ArrayList<String>();
        String delim = "-";
        if (list.isArray()) {
            for (final JsonNode objNode : list) {
                joinProperties.add(objNode.get("name").toString().replace("\"", ""));
            }
        }
        return String.join(delim, joinProperties);
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}
