package wolox.training.models.DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

@Data
public class BookDTO {

    @Getter
    @Setter
    private String genre;
    private String authors;
    private String Image;
    private String title;
    private String subtitle;
    private String publisher;
    private String year;
    private Integer pages;
    private String isbn;

    public BookDTO(JSONObject rootNode, String isbn) {
        this.setGenre(this.setPropertiesOfList((JSONArray) rootNode.get("subjects")));
        this.setAuthors(this.setPropertiesOfList((JSONArray) rootNode.get("authors")));
        this.setImage(((JSONObject) rootNode.get("cover")).get("small").toString());
        this.setTitle(rootNode.get("title").toString());
        this.setSubtitle(rootNode.get("subtitle").toString());
        this.setPublisher(this.setPropertiesOfList((JSONArray) rootNode.get("publishers")));
        this.setYear(rootNode.get("publish_date").toString());
        this.setPages(Integer.valueOf(rootNode.get("number_of_pages").toString()));
        this.setIsbn(getNumberOfAlphanumericString(isbn));
    }

    private String getNumberOfAlphanumericString(String alphanumericString) {
        Pattern regex = Pattern.compile("\\d+");
        Matcher numberFound = regex.matcher(alphanumericString);
        while (numberFound.find()) {
            return numberFound.group(0);
        }
        return "0";
    }

    private String setPropertiesOfList(JSONArray list) {

        List<String> joinProperties = new ArrayList<String>();
        String delim = "-";
        list.forEach(prop -> {
                JSONObject property = (JSONObject) prop;
                if (property.has("name")) {
                    joinProperties.add(property.get("name").toString().replace("\"", ""));
                }
            }
        );
        return String.join(delim, joinProperties);
    }

}
