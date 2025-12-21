package requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetAccountExample {

    private final GeniusClient client = GeniusClient.builder()
            .clientId("your_client_id")
            .clientSecret("your_client_secret")
            .accessToken("your_access_token")
            .build();

    private void execute() {

        client.getAccount(TextFormat.PLAIN)
                .doOnError(e -> {

                    System.out.println("Error: " + e.getMessage());

                })

                .subscribe(account -> {

                    System.out.println("Name: " + account.getName());
                    System.out.println("Email: " + account.getEmail());
                    System.out.println("About me: " + account.getAboutMe(TextFormat.PLAIN));

                });

    }

}
