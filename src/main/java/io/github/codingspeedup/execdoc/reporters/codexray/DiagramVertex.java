package io.github.codingspeedup.execdoc.reporters.codexray;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Objects;

public abstract class DiagramVertex {

    public static final int FLAG_BLANK = 0b0000_0000_0000_0000_0000_0000_0000_0000;

    private int flags = FLAG_BLANK;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String urlTooltip;

    public void setFlags(int... flags) {
        if (ArrayUtils.isNotEmpty(flags)) {
            for (int flag : flags) {
                this.flags |= flag;
            }
        }
    }

    public boolean isFlag(int flag) {
        return this.flags == flag || (this.flags & flag) > 0;
    }

    public abstract String getVertexId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagramVertex that = (DiagramVertex) o;
        return getVertexId().equals(that.getVertexId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVertexId());
    }

    @Override
    public String toString() {
        return getVertexId();
    }

}
