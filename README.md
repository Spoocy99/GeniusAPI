# Java Genius API Wrapper
This is a java wrapper for the [Genius API](https://docs.genius.com). <br>

Feature overview:
* Supports: Songs & Artists, Annotations & Referents, Web Pages, Search
* Lyrics searching
* Uses [Reactor](https://projectreactor.io/)
* Basic authentication (Client Access Token & Access Token)
* Compiled for `Java 11`

## Download

[![Version](https://repo.coding-stube.de/api/badge/latest/releases/dev/spoocy/genius/GeniusAPI?color=0F81C2&name=Version&prefix=v)](https://repo.coding-stube.de/#/releases/dev/spoocy/genius/GeniusAPI)

Be sure to replace the **VERSION** key below with the version shown above!
```xml
<repository>
    <id>spoocyDev-releases</id>
    <name>Coding Stube Repository</name>
    <url>https://repo.coding-stube.de/releases</url>
</repository>
```

```xml
<dependency>
    <groupId>dev.spoocy.genius</groupId>
    <artifactId>GeniusAPI</artifactId>
    <version>VERSION</version>
</dependency>
```

## Gettings started
- Please also check out the [Genius API Documentation](https://docs.genius.com).
- Make sure you know how [Reactor](https://projectreactor.io/) works!
- You will need to **authenticate** using one of the methods below before making any requests to the Genius API.

* [Authentication](#Authentication)
* [Usage](#Usage)
  * [Annotations](#Getting-an-annotation)
  * [Referents](#Getting-Referents)
  * [Songs](#Getting-a-Song)
  * [Artists](#Getting-an-Artist)
  * [Artist's Songs](#Getting-an-Artists-songs)
  * [Web Pages](#Getting-a-web-page)
  * [Account](#Getting-the-current-account)
  * [Search](#Performing-a-search)
  * [Lyrics](#Searching-for-Lyrics)
* [Lyrics Errors](#Lyrics-Errors)

## Authentication

Please see [Genius's Authentication Documentation](https://docs.genius.com/#/authentication-h1) for more information about Authentication.

> [!IMPORTANT]
> You will need to create a Genius API Client. You can do so at https://genius.com/api-clients <br>

There are two types of access tokens you can use to authenticate to the Genius API:
* **Client Access Token**: Use this if your application doesn't include user-specific behavior (read-only endpoints that don't require scopes).
* **User Access Token (OAuth Authorization Code flow)**: Use this if you need to make requests on behalf of individual users (requires scopes).

### Authenticating using a Client Access Token

* **client_id**: Your application's Client ID
* **client_secret**: Your application's Client Secret
* **client_access_token**: Your application's Client Access Token (You can get a client access token by clicking "Generate Access Token" on the API Client management page.)
```java
GeniusClient client = GeniusClient.builder()
        .clientId("your_client_id")                 // Your application's Client ID
        .clientSecret("your_client_secret")         // Your application's Client Secret
        .accessToken("your_client_access_token")    // Your application's Client Access Token
        .build();
```

### Authenticating using the Authorization Code (OAuth) flow

`Used for making API calls on behalf of individual users.`
* **client_id**: Your application's Client ID
* **client_secret**: Your application's Client Secret
* **redirect_uri**: The URI Genius will redirect the user to after they've authorized your application; it must be the same as the one set for the API Client on the management page
* **state**: A value that will be returned with the code redirect for maintaining arbitrary state through the authorization process
* **scopes**: A list of scopes of access to request from the user (see [Genius Scopes](https://docs.genius.com/#authentication) for more information)

1) Create the GeniusClient with your client_id and client_secret:
```java
GeniusClient client = GeniusClient.builder()
        .clientId("your_client_id")                 // Your application's Client ID
        .clientSecret("your_client_secret")         // Your application's Client Secret
        .build();
```

2) Build the authorization URI and redirect the user to it:
```java
// Build the authorization URI for the user to authorize your application
URI authorizationUri = client.authorizationCodeUri(
        SECRET_STATE,                           // A secret state value
        "http://your_redirect_uri.com",         // Your redirect URI
        Scope.ME, Scope.VOTE                    // The scopes you are requesting
)
.block(); // Example only. Avoid blocking in production code.
```

3) Request an Access Token using the Authorization Code received from the redirect:
```java
GeniusAccessToken token = client.accessToken(
       code,                               // The Authorization Code received from the redirect
       "http://your_redirect_uri.com",     // Set your Redirect URI (the same as above!!!)
       Scope.ME, Scope.VOTE                // Set the required Scopes (the same as above!!!)
).block();

client.setAccessToken(token);               // Set the Authorization Code, otherwise no requests can be made
```

## Usage

Requests return [Mono](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html) instances from Reactor to wait for results.

The following show very basic examples of how to use the various endpoints.

### Getting an annotation:
```java
client.getAnnotation(3037171L, TextFormat.PLAIN, TextFormat.HTML)   // annotation id
        .doOnError(e -> System.out.println("Error: " + e.getMessage()))
        .subscribe(data -> System.out.println("Body: " + data.getAnnotation().getBody(TextFormat.PLAIN)));
```
Request Builder: ```client.annotation()```

### Getting Referents:
```java
client.getReferentsBySongId(378195L, 5, 1, TextFormat.PLAIN)        // with song id
        .doOnError(e -> System.out.println("Error: " + e.getMessage()))
        .subscribe(data -> System.out.println("Amount: " + data.getReferents().size()));

client.getReferentsByWebPage(10347L, 5, 1, TextFormat.PLAIN)        // with web page id
        .doOnError(e -> System.out.println("Error: " + e.getMessage()))
        .subscribe(data -> System.out.println("Amount: " + data.getReferents().size()));
```
Request Builder: ```client.referents()```

### Getting a Song:
```java
client.getSong(
        378195L,                                    // song id
        TextFormat.PLAIN, TextFormat.HTML           // optional description formats
        )
        .doOnError(e -> {
            System.out.println("Error while searching for Song: " + e.getMessage());
            e.printStackTrace();
        })
        .subscribe(song -> {
            System.out.printf("Title: %s%n", song.getTitle());
            System.out.printf("Description: %s%n", song.getDescription(TextFormat.PLAIN));
        });
```
Request Builder: ```client.song()```

### Getting an Artist:
```java
client.getArtist(
        16775L,             // artist id
        TextFormat.HTML     // optional description formats
        )
        .doOnError(e -> {
            System.out.println("Error while searching for Artist: " + e.getMessage());
            e.printStackTrace();
        })
        .subscribe(artist -> {
            System.out.printf("Name: %s%n", artist.getName());
            System.out.printf("Description: %s%n", artist.getDescription(TextFormat.PLAIN.HTML));
        });
```
Request Builder: ```client.artist()

### Getting an Artist's songs
```java
client.getArtistSongs(16775L, Sort.POPULARITY, 5, 1)
        .doOnError(e -> System.out.println("Error: " + e.getMessage()))
        .subscribe(data -> System.out.println("Amount: " + data.getSongs().size()));
```
Request Builder: ```client.artistSongs()```

### Getting a web page:
```java
client.getWebPage("https://docs.genius.com", null, null)
        .doOnError(e -> System.out.println("Error: " + e.getMessage()))
        .subscribe(data -> System.out.println("Web Page ID: " + data.getId()));
```
Request Builder: ```client.webPagesLookup()```

### Getting the current account:
```java
client.getAccount(TextFormat.PLAIN)
        .doOnError(e -> System.out.println("Error: " + e.getMessage()))
        .subscribe(data -> System.out.println("Account Name: " + data.getName()));
```
Request Builder: ```client.webPagesLookup()```

### Performing a search:
```java
client.search("Kendrick Lamar")
      .doOnError(e -> System.out.println("Error while searching: " + e.getMessage()))
      .subscribe(search -> {
          if (search.getHits().isEmpty()) {
              System.out.println("No results found!");
              return;
          }

          Search.Hit first = search.getHits().get(0);
          System.out.printf("Title: %s%n", first.getResult().getTitle());
          System.out.printf("Artist: %s%n", first.getResult().getArtistNames());
      });
```
Request Builder: ```client.search()```

### Searching for Lyrics:
```java
client.getLyricsByQuery("Kendrik Lamar Humble").
        doOnError(e -> System.out.println("Error while getting lyrics: " + e.getMessage()))
        .subscribe(lyrics -> System.out.println(lyrics.getPlain()));

String url = "https://genius.com/Kendrick-lamar-humble-lyrics";
client.getLyricsByUrl(url)
      .doOnError(e -> System.out.println("Error while getting lyrics: " + e.getMessage()))
      .subscribe(lyrics -> System.out.println(lyrics.getPlain()));

// Or build the request manually:
client.lyrics()
        .url(url)       // e.g. "https://genius.com/Kendrick-lamar-humble-lyrics"   (query will be ignored if url is set)
        .path(url)      // e.g. "Kendrick-lamar-humble-lyrics"                      (query will be ignored if url is set)
        .query(url)     // e.g. "Kendrick Lamar Humble" (Search Query)
        .build()
        .doOnError(e -> System.out.println("Error while getting lyrics: " + e.getMessage()))
        .subscribe(lyrics -> System.out.println(lyrics.getPlain()));
```
Request Builder: ```client.lyrics()```

## Examples

* Authentication
  * Access Token: [link](src/examples/java/authorization/AccessTokenExample.java)
  * Authorization Code: [link](src/examples/java/authorization/AuthorizationCodeExample.java)
* Requests
  * Account: [link](src/examples/java/requests/GetAccountExample.java)

---
## Lyrics Errors
- `GeniusException`: Could not find Lyrics Container. This happens when Genius changes their website. Report this to the developer and/or try to set the container ids directly via the LyricsScraper.

This means that the library could not find the lyrics container on the website because their names changed.

Try using `unknownContainerNames()` in the lyrics request builder to attempt to find the correct lyrics:
```java
client.lyrics()
      .url(url)
      .unknownContainerNames() // try this
      .build()
      .doOnError(e -> System.out.println("Error while getting lyrics: " + e.getMessage()))
      .subscribe(lyrics -> System.out.println(lyrics.getPlain()));
```
This will scan the whole site and guess the container names. This does not guarantee successful extraction, but may work. 
This will result in a slower extraction process so it should on be used when the lyrics extraction fails with the default Extractor.

You can also try to create your own extractor implementing the 'LyricsExtractor' interface or set the container names directly:
```java
LyricsExtractor extractor = new DirectLyricsExtractor.Builder()
        .lyrics_container("lyrics_container_name")
        .lyrics_container_header("lyrics_container_header_name")
        .lyrics_title_container("lyrics_title_container_name")
        .build();
```
You will have to inspect the Genius website in order to find the new container ids.