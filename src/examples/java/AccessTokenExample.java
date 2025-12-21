import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AccessTokenExample {

    public static void main(String[] args) {
        GeniusClient client = GeniusClient.builder()
                .clientId("your_client_id")
                .clientSecret("your_client_secret")
                .accessToken("your_access_token")       // You can get a client access token by clicking "Generate Access Token" on the API Client management page
                .build();

        // start making requests
        client.getArtist(16775L, TextFormat.PLAIN)
                .subscribe(artist -> {
                    System.out.printf("Artist Name: %s%n", artist.getName());
                    System.out.printf("Artist ID: %d%n", artist.getId());
                    System.out.printf("Artist URL: %s%n", artist.getUrl());
        });

    }


}
