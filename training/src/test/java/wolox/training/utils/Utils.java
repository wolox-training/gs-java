package wolox.training.utils;

public class Utils {

    public static final String BODY_WITH_USER =
        "{\"username\": \"guille1234\", \"name\": \"guille\""
            + ", \"password\": \"hola1234\", \"birthdate\": \"1993-12-06\"}";
    public static final String BODY_WITH_BOOK_UPDATED =
        "{\"id\": \"1\", \"year\": \"1990\""
            + ", \"title\": \"el señor de los anillos\""
            + ", \"subtitle\": \"las dos torres\""
            + ", \"pages\": \"30\""
            + ", \"publisher\": \"epublisher 1234\""
            + ", \"genre\": \"ficcion\""
            + ", \"author\": \"un autor\""
            + ", \"isbn\": \"isgn 12\""
            + ", \"image\": \"one image\""
            + "}";
    public static final String BODY_WITH_BOOK_CREATED =
        "{\"id\": \"1\", \"year\": \"1900\""
            + ", \"title\": \"Harry potter\""
            + ", \"subtitle\": \"la camara secreta\""
            + ", \"pages\": \"30\""
            + ", \"publisher\": \"epublisher 1234\""
            + ", \"genre\": \"ficcion\""
            + ", \"author\": \"J.K rowling\""
            + ", \"isbn\": \"isgn 12\""
            + ", \"image\": \"one image\""
            + "}";

    public static final String BODY_CHANGE_PASSWORD =
        "{\"oldPassword\": \"hola1234\""
            + ", \"newPassword\": \"hola5555\""
            + ", \"confirmNewPassword\": \"hola5555\""
            + "}";

    public static final String BODY_CHANGE_PASSWORD_WITH_DIFFERENTS_PASSWORD =
        "{\"oldPassword\": \"hola1234\""
            + ", \"newPassword\": \"hola5555\""
            + ", \"confirmNewPassword\": \"hola4444\""
            + "}";

    public static final String BODY_CHANGE_PASSWORD_WITH_BAD_OLD_PASSWORD =
        "{\"oldPassword\": \"hola5678\""
            + ", \"newPassword\": \"hola5555\""
            + ", \"confirmNewPassword\": \"hola4444\""
            + "}";

    public static final String RESQUEST_ISBN_BOOK = "/api/books?bibkeys=ISBN:0385472579&format=json&jscmd=data";
    public static final String RESQUEST_BAD_ISBN_BOOK = "/api/books?bibkeys=ISBN:0385472&format=json&jscmd=data";
    public static final String RESPONSE_ISBN_BOOK = "{\"ISBN:0385472579\": "
        + "{\"publishers\": [{\"name\": \"Anchor Books\"}], \"pagination\": \"159 p. :\","
        + " \"identifiers\": {\"lccn\": [\"93005405\"], \"openlibrary\": [\"OL1397864M\"], "
        + "\"isbn_10\": [\"0385472579\"], \"librarything\": [\"192819\"], \"goodreads\": [\"979250\"]}, "
        + "\"subtitle\": \"shouts of nothingness\", \"title\": \"Zen speaks\","
        + " \"url\": \"https://openlibrary.org/books/OL1397864M/Zen_speaks\", \"number_of_pages\": 159, "
        + "\"cover\": {\"small\": \"https://covers.openlibrary.org/b/id/240726-S.jpg\", "
        + "\"large\": \"https://covers.openlibrary.org/b/id/240726-L.jpg\", "
        + "\"medium\": \"https://covers.openlibrary.org/b/id/240726-M.jpg\"}, "
        + "\"subjects\": [{\"url\": \"https://openlibrary.org/subjects/caricatures_and_cartoons\","
        + " \"name\": \"Caricatures and cartoons\"}, {\"url\": \"https://openlibrary.org/subjects/zen_buddhism\", "
        + "\"name\": \"Zen Buddhism\"}], \"publish_date\": \"1994\", \"key\": \"/books/OL1397864M\", "
        + "\"authors\": [{\"url\": \"https://openlibrary.org/authors/OL223368A/Zhizhong_Cai\", "
        + "\"name\": \"Zhizhong Cai\"}], \"classifications\": {\"dewey_decimal_class\": [\"294.3/927\"], "
        + "\"lc_classifications\": [\"BQ9265.6 .T7313 1994\"]}, \"publish_places\": [{\"name\": \"New York\"}]}}";
}
