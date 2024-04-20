package dev.spoocy.genius.data;

import dev.spoocy.common.config.Document;
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

    public Search(Document data) {
        this.results = new ArrayList<>();
        SectionArray<? extends Document> hits = data.getSectionArray("hits");

        for(int i = 0; i < hits.length(); i++) {
            Document hit = hits.get(i);
            Document result = hit.getSection("result");
            this.results.add(new SearchSong(result));
        }
    }

    @Override
    public String toString() {
        return "Search{" +
                "results=" + results +
                '}';
    }
}
