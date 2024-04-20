package dev.spoocy.genius.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
@AllArgsConstructor
public class Description {
    private String plain;
    private String html;

    @Override
    public String toString() {
        return "Description{" +
                "plain='" + plain + '\'' +
                ", html='" + html + '\'' +
                '}';
    }
}
