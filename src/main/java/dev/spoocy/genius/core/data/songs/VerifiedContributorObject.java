package dev.spoocy.genius.core.data.songs;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.account.UserObject;
import dev.spoocy.genius.core.data.artists.ArtistObject;
import dev.spoocy.genius.model.Artist;
import dev.spoocy.genius.model.Song;
import dev.spoocy.genius.model.User;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation for {@link Song.VerifiedContributor}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class VerifiedContributorObject implements Song.VerifiedContributor {

    /**
     * Path: response.song.verified_contributors.contributions
     */
    @NotNull
    private final String[] contributions;

    /**
     * Path: response.song.verified_contributors.artist
     */
    @NotNull
    private final Artist artist;

    /**
     * Path: response.song.verified_contributors.user
     */
    @NotNull
    private final User user;

    public VerifiedContributorObject(
            @NotNull String[] contributions,
            @NotNull Artist artist,
            @NotNull User user
    ) {
        this.contributions = contributions;
        this.artist = artist;
        this.user = user;
    }


    @Override
    public @NotNull String[] getContributions() {
        return this.contributions;
    }

    @Override
    public @NotNull Artist getArtist() {
        return this.artist;
    }

    @Override
    public @NotNull User getUsers() {
        return this.user;
    }


    public static class Parser extends AbstractModelParser<Song.VerifiedContributor> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public Song.VerifiedContributor parse0(@NotNull Config data) {
            return new VerifiedContributorObject(
                    data.getStringList("contributions").toArray(String[]::new),
                    ArtistObject.Parser.INSTANCE.parse(data.getSection("artist")),
                    UserObject.Parser.INSTANCE.parse(data.getSection("user"))
            );
        }
    }

}
