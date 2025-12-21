import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.auth.GeniusAccessToken;
import dev.spoocy.genius.core.auth.Scope;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AuthorizationCodeExample {

    public static void main(String[] args) {
        String SECRET_STATE = "100";

        GeniusClient client = GeniusClient.builder()
                .clientId("your_client_id")                 // Set your Client ID
                .clientSecret("your_client_secret")         // Set your Client Secret
                .build();

        URI authorizationUri = client.authorizationCodeUri(         // Get the Authorization Code URI
                SECRET_STATE,                                       // Secret State
                "http://your_redirect_uri.com",                     // Set your Redirect URI
                Scope.ME, Scope.VOTE                                // Set the required Scopes
        ).block();  // Example Only. Never block in production code.

        System.out.println("Authenticate here and paste code: " + authorizationUri.toString());

        final String code;
        try (Scanner in = new Scanner(System.in, StandardCharsets.UTF_8)) {
            code = in.nextLine();
            System.out.println();
        }

        GeniusAccessToken token = client.accessToken(
                code,                                       // The Authorization Code received from the redirect
                "http://your_redirect_uri.com",             // Set your Redirect URI (the same as above!!!)
                Scope.ME, Scope.VOTE                // Set the required Scopes (the same as above!!!)
        ).block();

        client.setAccessToken(token);      // Set the Authorization Code, otherwise no requests can be made

        // start making requests
        client.getArtist(16775L, TextFormat.PLAIN)
                .subscribe(artist -> {
                    System.out.printf("Artist Name: %s%n", artist.getName());
                    System.out.printf("Artist ID: %d%n", artist.getId());
                    System.out.printf("Artist URL: %s%n", artist.getUrl());
        });
    }


}
