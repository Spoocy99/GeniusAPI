

# Java Genius API Wrapper
This is a java wrapper for the [Genius API](https://docs.genius.com). <br>
This library can be used with Java 11 or newer.

## Download

[![Release](https://repo.coding-stube.de/api/badge/latest/releases/dev/spoocy/genius/GeniusAPI?color=0F81C2&name=release&prefix=v)](https://repo.coding-stube.de/#/releases/dev/spoocy/genius/GeniusAPI)

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

## Authentication

> [!IMPORTANT]
> You will need to create a Genius API Client. You can do so at https://genius.com/api-clients <br>

See https://docs.genius.com/#/authentication-h1 for more information about Authentication.

___

### Authenticating to the Genius API by using your Client access token:
* **client_id**: Your application's Client ID
* **client_secret**: Your application's Client Secret
* **client_access_token**: Your application's Client Access Token (can be found on the Genius API Client management page)
```java
GeniusClient client = new GeniusClientBuilder()
        
        // Set your Client ID
        .setClientId("your_client_id")       
        
        // Set your Client Secret
        .setClientSecret("your_client_secret")
        
        // Set the Auth Type you want to use. In this case by providing a Client Access Token
        .setAuthType(GeniusClientBuilder.AuthType.CLIENT_ACCESS_TOKEN)
        
        // Set the User Agent
        .setUserAgent("Mozilla/5.0")
        
        .build();
```
After building the Client, you will need to providing the client with the Client Access Token. <br>
```java
// Set the Client Access Token for the client to use. Before doing this, no requests can be made.
client.setAuthorizationCode("your_client_access_token");
```
Implementation Example: [AccessTokenExample](src/test/java/AccessTokenExample.java)

___

### Authenticating to the Genius API by getting and providing an Access Token:
`Used for making API calls on behalf of individual users.`
* **client_id**: Your application's Client ID
* **client_secret**: Your application's Client Secret
* **redirect_uri**: The URI Genius will redirect the user to after they've authorized your application; it must be the same as the one set for the API Client on the management page
* **state**: A value that will be returned with the code redirect for maintaining arbitrary state through the authorization process
```java
GeniusClient client = new GeniusClientBuilder()
        
        // Set your Client ID
        .setClientId("your_client_id")
        
        // Set your Client Secret
        .setClientSecret("your_client_secret")
        
        // Set the Auth Type you want to use. In this case by getting an Access Token
        .setAuthType(GeniusClientBuilder.AuthType.AUTHORIZATION_CODE)
        
        // Set your redirect URI
        .setCallbackUrl("your_redirect_uri")
        
        // Set the User Agent
        .setUserAgent("Mozilla/5.0")
        
        .build();
```
After building the Client, you will need to authorize using the Authorization URL and providing the client with the code. <br>
```java
String SECRET_STATE = "100";                                // The state value to be returned with the code (see above)
String url = client.buildAuthorizationUrl(SECRET_STATE);    // Builds the Authorization URL using the provided credentials and the state

// Set the Authorization Code for the client to use. Before doing this, n qo requests can be made.
client.setAuthorizationCode("code_received_from_the_authorization_url");
```
Implementation Example: [AuthorizationCodeExample](src/test/java/AuthorizationCodeExample.java)

## Usage

Getting a Lyrics of a song.
```java
String url = "https://genius.com/Kendrick-lamar-humble-lyrics";
client.lyrics()                          // Get the lyrics of a song
    .setSongUrl(url)                     // Set the URL of the song
    .setSongName("Eine gute Frau")       // OR set the name of the song (if url is set, this will be ignored)
    .doOnError(e -> {
        System.out.println("Error while getting lyrics: " + e.getMessage());
    })
        .subscribe(lyrics -> {
            System.out.println(lyrics.getAsPlain());
        });
```
___

Searching for a list of song by providing a query.
```java
client.search()
    
    // Set the query
    .setQuery("Kendrick Lamar")
    
    .subscribe(search -> {
        
        // Results might be empty if nothing is found
        if(search.getResults().isEmpty()) {
            System.out.println("No results found!");
            return;
        }
        
        // Search provides a list of results
        SearchSong firstResult = search.getResults().get(0);
        System.out.println(String.format("ID: %s", firstResult.getId()));
        System.out.println(String.format("Artist: %s", firstResult.getArtist().getName()));
        System.out.println(String.format("URL: %s", firstResult.getUrl()));
    });
```
___

Searching for a song by providing the id of a song.
```java
client.searchSong()
    
    // Set the id of the song
    .setId("378195")
    
    .subscribe(song -> {
        System.out.println(String.format("Title: %s", song.getTitle()));
    });
```
___

Searching for a artist by providing the id of the artist.
```java
client.searchArtist() 
    
    // Set the id of the song
    .setId("16775")
    
    .subscribe(artist -> {
        System.out.println(String.format("Name: %s", artist.getName()));
    });
```
___

Gettings Descriptions of Artists and Songs.
```java
//Artists and Songs may have descriptions. For those you need to provide how you want those to be formatted.
client.searchSong()
    .setId("378195")
    
    // Set the format of the description
    .addFormat(DescriptionFormat.HTML)
    
    // You may add multiple formats
    .addFormat(DescriptionFormat.PLAIN)
    
    .subscribe(song -> {
         // You can only get Description if you provided the format, otherwise it will be null
        System.out.println(String.format("Description: %s", song.getDescription().getPlain()));
    });
```

## Calling Results

Calling results in sync:
```java
Search result = client.search().setQuery("Kendrick Lamar").execute();
```

Calling results in async:
```java
client.search().setQuery("Kendrick Lamar").subscribe(search -> {
    System.out.println(search.getResults().get(0).getTitle());
});
```

## Examples

The following examples are minimal implementations but show how the library works.
  - Making Requests using a Client Access Token: [link](src/examples/java/AccessTokenExample.java)
  - Making Requests using an Access Token: [link](src/examples/java/AuthorizationCodeExample.java)
