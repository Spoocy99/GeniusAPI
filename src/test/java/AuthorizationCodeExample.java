import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.GeniusClientBuilder;
import dev.spoocy.genius.data.Lyrics;
import dev.spoocy.genius.data.SearchSong;
import dev.spoocy.genius.core.TextFormat;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AuthorizationCodeExample {

    public static void main(String[] args) {
        String SECRET_STATE = "100";

        GeniusClient client = new GeniusClientBuilder()
                .setClientId("your_client_id")                                      // Set the Client ID
                .setClientSecret("your_client_secret")                              // Set the Client Secret
                .setAuthType(GeniusClientBuilder.AuthType.AUTHORIZATION_CODE)      // Set the Auth Type
                .setCallbackUrl("https://localhost")                                // Set the Callback URL
                .setUserAgent("Mozilla/5.0")                                        // Set the User Agent
                .build();

        System.out.println("Authenticate here and paste code: " + client.buildAuthorizationUrl(SECRET_STATE));  // Print the URL to authenticate

        final String code;
        try (Scanner in = new Scanner(System.in, StandardCharsets.UTF_8)) {
            code = in.nextLine();
            System.out.println();
        }
        client.setAuthorizationCode(code);      // Set the Authorization Code, otherwise no requests can be made

        client.searchSong()                     // Search for a specific song by ID
                .setId("378195")                // Set the ID of the song
                .addFormat(TextFormat.PLAIN)    // Add formats for the description
                .addFormat(TextFormat.HTML)
                .subscribe(song -> {
                    System.out.println(String.format("Title: %s", song.getTitle()));
                    System.out.println(String.format("Artist: %s", song.getArtist().getName()));
                    System.out.println(String.format("URL: %s", song.getUrl()));
                    System.out.println(String.format("Description: %s", song.getDescription().getPlain()));
        });

        client.searchArtist()                   // Search for a specific artist by ID
                .setId("16775")                 // Set the ID of the artist
                .addFormat(TextFormat.HTML)     // Add formats for the description
                .subscribe(artist -> {
                    System.out.println(String.format("Name: %s", artist.getName()));
                    System.out.println(String.format("Facebook: %s", artist.getFacebookName()));
                    System.out.println(String.format("URL: %s", artist.getUrl()));
                    System.out.println(String.format("Description: %s", artist.getDescription().getHtml()));
        });

        client.search()                         // Search Songs by a specific query
                .setQuery("Kendrick Lamar")     // Set the query
                .doOnError(e -> {
                    System.out.println("Error while searching for Songs: " + e.getMessage());
                })
                .subscribe(search -> {
                    if(search.getResults().isEmpty()) {               // Check if there are any results
                        System.out.println("No results found!");
                        return;
                    }
                    SearchSong firstResult = search.getResults().get(0);                    // List of Results
                    System.out.println(String.format("Title: %s", firstResult.getTitle()));
                    System.out.println(String.format("Artist: %s", firstResult.getArtist().getName()));
                    System.out.println(String.format("URL: %s", firstResult.getUrl()));
                });



        String url = "https://genius.com/Kendrick-lamar-humble-lyrics";
        client.lyrics()                                          // Get the lyrics of a song
                .setSongUrl(url)                                 // Set the url of the song
                .setSongName("Kendrick Lamar humble")            // OR set the name of the song (if url is set, this will be ignored)
                .doOnError(e -> {
                    System.out.println("Error while getting lyrics: " + e.getMessage());
                })
                .subscribe(lyrics -> {
                    System.out.println(lyrics.getAsPlain());
                });
    }


}
