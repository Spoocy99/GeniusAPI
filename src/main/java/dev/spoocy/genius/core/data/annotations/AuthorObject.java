package dev.spoocy.genius.core.data.annotations;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.account.UserObject;
import dev.spoocy.genius.model.Annotation;
import dev.spoocy.genius.model.User;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation for {@link Annotation.Author}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AuthorObject implements Annotation.Author {

    /**
     * Path: annotation.authors.attribution
     */
    @NotNull
    private final Integer attribution;

    /**
     * Path: annotation.authors.pinned_role
     */
    @NotNull
    private final String pinnedRole;

    /**
     * Path: annotation.authors.user
     */
    @NotNull
    private final User user;

    public AuthorObject(
            @NotNull Integer attribution,
            @NotNull String pinnedRole,
            @NotNull User user
    ) {

        this.attribution = attribution;
        this.pinnedRole = pinnedRole;
        this.user = user;
    }

    @Override
    public @NotNull Integer getAttribution() {
        return this.attribution;
    }

    @Override
    public @NotNull String getPinnedRole() {
        return this.pinnedRole;
    }

    @Override
    public @NotNull User getUser() {
        return this.user;
    }

    public static class Parser extends AbstractModelParser<Annotation.Author> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public Annotation.Author parse0(@NotNull Config data) {
            return new AuthorObject(
                    data.getInt("attribution", 0),
                    data.getString("pinned_role", "null"),
                    UserObject.Parser.INSTANCE.parse(data.getSection("user"))
            );
        }
    }

}
