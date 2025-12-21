package dev.spoocy.genius.core.auth;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public enum Scope {
    /**
     * Grants access to the following endpoints: <br>
     * <ul>
     *     <li>{@code GET /account}</li>
     * </ul>
     */
    ME("me"),

    /**
     * Grants access to the following endpoints: <br>
     * <ul>
     *     <li>{@code POST /annotations}</li>
     * </ul>
     */
    CREATE_ANNOTATIONS("create_annotations"),

    /**
     * Grants access to the following endpoints: <br>
     * <ul>
     *     <li>{@code PUT /annotations/:id}</li>
     *     <li>{@code DELETE /annotations/:id}</li>
     * </ul>
     */
    MANAGE_ANNOTATIONS("manage_annotations"),

    /**
     * Grants access to the following endpoints: <br>
     * <ul>
     *     <li>{@code PUT /annotations/:id/upvote}</li>
     *     <li>{@code PUT /annotations/:id/downvote}</li>
     *     <li>{@code PUT /annotations/:id/unvote}</li>
     * </ul>
     */
    VOTE("vote");

    private final String key;

    /**
     * @param key The key of the scope as specified
     *            in the Genius API documentation.
     */
    Scope(@NotNull String key) {
        this.key = key;
    }

    /**
     * Get the key of the scope.
     *
     * @return The key of the scope.
     */
    @NotNull
    public String getKey() {
        return key;
    }

    /**
     * Format the given scopes into a single string
     * to provide in the http request.
     *
     * @param scopes The scopes to format
     *
     * @return The formatted scopes.
     */
    public static String format(@NotNull Scope... scopes) {
        StringBuilder sb = new StringBuilder();
        for (Scope scope : scopes) {
            sb.append(scope.getKey()).append(" ");
        }
        return sb.toString().trim();
    }

}
