package dev.spoocy.genius.core.data;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.utils.common.text.StringUtils;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.SectionArray;
import dev.spoocy.utils.config.documents.JsonConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractModelParser<T> {

    public T parseResponse(final String json) {
        return this.parse(
                new JSONObject(json)
                        .getJSONObject("response")
        );
    }

    public T parse(final JSONObject jsonOBject) {
        return this.parse(new JsonConfig(jsonOBject));
    }

    public T parse(@NotNull Config data) {
        return this.parse0(data);
    }

    protected abstract T parse0(@NotNull Config data);

    @NotNull
    public List<T> parseList(@NotNull SectionArray<? extends Config> config) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < config.length(); i++) {
            list.add(this.parse(config.get(i)));
        }

        return list;
    }

    @Nullable
    protected Boolean booleanOrNull(final Config data, final String path) {
        if (!data.isSet(path)) {
            return null;
        }

        return data.getBoolean(path);
    }

    @Nullable
    protected Integer integerOrNull(final Config data, final String path) {
        if (!data.isSet(path)) {
            return null;
        }

        return data.getInt(path);
    }

    @Nullable
    protected Long longOrNull(final Config data, final String path) {
        if (!data.isSet(path)) {
            return null;
        }

        return data.getLong(path);
    }

    @Nullable
    protected Double doubleOrNull(final Config data, final String path) {
        if (!data.isSet(path)) {
            return null;
        }

        return data.getDouble(path);
    }

    @Nullable
    protected String stringOrNull(final Config data, final String path) {
        if (!data.isSet(path)) {
            return null;
        }

        return data.getString(path);
    }

    @Nullable
    protected String[] stringArrayOrNull(final Config data, final String path) {
        if (!data.isSet(path)) {
            return null;
        }

        List<String> list = data.getStringList(path);
        return list.toArray(String[]::new);
    }

    @NotNull
    protected Map<TextFormat, String> parseFormats(@NotNull Config data) {
        Map<TextFormat, String> map = new HashMap<>();

        for (TextFormat format : TextFormat.values()) {
            String bodyContent = data.getString(format.getKey(), "");

            if (!StringUtils.isNullOrEmpty(bodyContent)) {
                map.put(format, bodyContent);
            }
        }

        return map;
    }

}
