package dev.spoocy.genius.core.impl;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.Sort;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.auth.AccessTokenRequest;
import dev.spoocy.genius.core.auth.AuthorizationCodeUriRequest;
import dev.spoocy.genius.core.auth.Scope;
import dev.spoocy.genius.core.http.SearchType;
import dev.spoocy.genius.core.http.requests.*;
import dev.spoocy.genius.core.http.u_requests.SectionsSearchRequest;
import dev.spoocy.utils.common.text.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractClient implements GeniusClient {

    @Override
    public @NotNull AuthorizationCodeUriRequest.Builder authorizationCodeUriRequest() {
        return new AuthorizationCodeUriRequest.Builder();
    }

    @Override
    public @NotNull AuthorizationCodeUriRequest authorizationCodeUri(
            @NotNull String state,
            @NotNull String redirectUri,
            @NotNull Scope scope,
            @NotNull Scope... scopes
    ) {
        return authorizationCodeUriRequest()
                .state(state)
                .redirectUri(redirectUri)
                .scope(scope, scopes)
                .build();
    }

    @Override
    public @NotNull AccessTokenRequest.Builder accessTokenRequest() {
        return new AccessTokenRequest.Builder(this.getClientId(), this.getClientSecret());
    }

    @Override
    public @NotNull AccessTokenRequest accessToken(
            @NotNull String authorizationCode,
            @NotNull String redirectUri,
            @NotNull Scope scope,
            @NotNull Scope... scopes
    ) {
        return accessTokenRequest()
                .authorizationCode(authorizationCode)
                .redirectUri(redirectUri)
                .scope(scope, scopes)
                .build();
    }

    @Override
    public GetArtistSongsRequest.Builder artistSongs() {
        return new GetArtistSongsRequest.Builder(this);
    }

    @Override
    public @NotNull GetArtistRequest.Builder artist() {
        return new GetArtistRequest.Builder(this);
    }

    @Override
    public @NotNull GetAnnotationRequest.Builder annotation() {
        return new GetAnnotationRequest.Builder(this);
    }

    @Override
    public @NotNull GetWebPagesLookupRequest.Builder webPagesLookup() {
        return new GetWebPagesLookupRequest.Builder(this);
    }

    @Override
    public @NotNull GetSongRequest.Builder song() {
        return new GetSongRequest.Builder(this);
    }

    @Override
    public @NotNull SearchRequest.Builder search() {
        return new SearchRequest.Builder(this);
    }

    @Override
    public @NotNull GetReferentsRequest.Builder referents() {
        return new GetReferentsRequest.Builder(this);
    }

    @Override
    public @NotNull GetAccountRequest getAccount(@NotNull TextFormat... format) {
        return new GetAccountRequest.Builder(this)
                .formats(format)
                .build();
    }

    @Override
    public @NotNull GetWebPagesLookupRequest getWebPage(
            @Nullable String raw_annotatable_url,
            @Nullable String canonical_url,
            @Nullable String og_url
    ) {
        GetWebPagesLookupRequest.Builder builder = webPagesLookup();
        boolean valid = false;

        if (!StringUtils.isNullOrEmpty(raw_annotatable_url)) {
            valid = true;
            builder.raw_annotatable_url(raw_annotatable_url);
        }
        if (!StringUtils.isNullOrEmpty(canonical_url)) {
            valid = true;
            builder.canonical_url(canonical_url);
        }
        if (!StringUtils.isNullOrEmpty(og_url)) {
            valid = true;
            builder.og_url(og_url);
        }

        if (!valid) {
            throw new IllegalArgumentException("At least one URL parameter must be provided.");
        }

        return builder.build();
    }

    @Override
    public @NotNull GetSongRequest getSong(long song_id, @NotNull TextFormat... format) {
        return song()
                .id(song_id)
                .formats(format).build();
    }

    @Override
    public @NotNull GetReferentsRequest getReferentsByWebPage(
            long web_page_id,
            int perPage,
            int page,
            @NotNull TextFormat... format
    ) {
        return referents()
                .web_page_id(web_page_id)
                .per_page(perPage)
                .page(page)
                .formats(format)
                .build();
    }

    @Override
    public @NotNull GetReferentsRequest getReferentsBySongId(
            long song_id,
            int perPage,
            int page,
            @NotNull TextFormat... format
    ) {
        return referents()
                .song_id(song_id)
                .per_page(perPage)
                .page(page)
                .formats(format)
                .build();
    }

    @Override
    public @NotNull GetArtistSongsRequest getArtistSongs(long id, @NotNull Sort sort, int perPage, int page) {
        return artistSongs()
                .id(id)
                .sort(sort)
                .per_page(perPage)
                .page(page)
                .build();
    }

    @Override
    public @NotNull GetArtistRequest getArtist(long artist_id, @NotNull TextFormat... format) {
        return artist()
                .id(String.valueOf(artist_id))
                .formats(format).build();
    }

    @Override
    public @NotNull GetAnnotationRequest getAnnotation(long annotation_id, @NotNull TextFormat... formats) {
        return annotation()
                .id(annotation_id)
                .formats(formats)
                .build();
    }

    @Override
    public @NotNull SearchRequest search(@NotNull String query) {
        return search()
                .query(query)
                .build();
    }

    @Override
    public @NotNull LyricsRequest.Builder lyrics() {
        return new LyricsRequest.Builder(this);
    }

    @Override
    public @NotNull LyricsRequest getLyricsByQuery(@NotNull String query) {
        return lyrics()
                .query(query)
                .build();
    }

    @Override
    public @NotNull LyricsRequest getLyricsByUrl(@NotNull String url) {
        return lyrics()
                .url(url)
                .build();
    }

    @Override
    public @NotNull SectionsSearchRequest.Builder usearch() {
        return new SectionsSearchRequest.Builder(this);
    }

    @Override
    public @NotNull SectionsSearchRequest searchAlbum(@NotNull String query, int perPage, int page) {
        return usearch()
                .type(SearchType.ALBUM)
                .query(query)
                .per_page(perPage)
                .page(page)
                .build();
    }

    @Override
    public @NotNull SectionsSearchRequest searchArticle(@NotNull String query, int perPage, int page) {
        return usearch()
                .type(SearchType.ARTICLE)
                .query(query)
                .per_page(perPage)
                .page(page)
                .build();
    }

    @Override
    public @NotNull SectionsSearchRequest searchArtist(@NotNull String query, int perPage, int page) {
        return usearch()
                .type(SearchType.ARTIST)
                .query(query)
                .per_page(perPage)
                .page(page)
                .build();
    }

    @Override
    public @NotNull SectionsSearchRequest searchLyric(@NotNull String query, int perPage, int page) {
        return usearch()
                .type(SearchType.LYRIC)
                .query(query)
                .per_page(perPage)
                .page(page)
                .build();
    }

    @Override
    public @NotNull SectionsSearchRequest searchMulti(@NotNull String query, int perPage, int page) {
        return usearch()
                .type(SearchType.MULTI)
                .query(query)
                .per_page(perPage)
                .page(page)
                .build();
    }

    @Override
    public @NotNull SectionsSearchRequest searchSong(@NotNull String query, int perPage, int page) {
        return usearch()
                .type(SearchType.SONG)
                .query(query)
                .per_page(perPage)
                .page(page)
                .build();
    }

    @Override
    public @NotNull SectionsSearchRequest searchUSer(@NotNull String query, int perPage, int page) {
        return usearch()
                .type(SearchType.USER)
                .query(query)
                .per_page(perPage)
                .page(page)
                .build();
    }

    @Override
    public @NotNull SectionsSearchRequest searchVideo(@NotNull String query, int perPage, int page) {
        return usearch()
                .type(SearchType.VIDEO)
                .query(query)
                .per_page(perPage)
                .page(page)
                .build();
    }

}
