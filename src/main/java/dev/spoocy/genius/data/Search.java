package dev.spoocy.genius.data;

import dev.spoocy.common.config.Config;
import dev.spoocy.common.config.SectionArray;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class Search {

    private final List<SearchSong> results;

    public Search(Config data) {
        this.results = new ArrayList<>();
        SectionArray<? extends Config> hits = data.getSectionArray("hits");

        for(int i = 0; i < hits.length(); i++) {
            Config hit = hits.get(i);
            Config result = hit.getSection("result");
            this.results.add(new SearchSong(result));
        }
    }

    public boolean hasResults() {
        return !this.results.isEmpty();
    }

    @Override
    public String toString() {
        return "Search{" +
                "results=" + results +
                '}';
    }
}
