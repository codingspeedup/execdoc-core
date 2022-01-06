package io.github.codingspeedup.execdoc.reporters.codexray;

import io.github.codingspeedup.execdoc.toolbox.utilities.UuidUtility;
import lombok.Getter;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XRaySection {

    @Getter
    private final String anchor;
    @Getter
    private final String heading;
    @Getter
    private final int level;
    @Getter
    private final StringBuilder content = new StringBuilder();

    public XRaySection(String heading, int level) {
        this.heading = heading;
        this.level = level;
        this.anchor = UuidUtility.nextUuid();
        content.append("<a name=\"").append(anchor).append("\"></a>");
        content.append("<h").append(level).append(">").append(heading).append("</h").append(level).append(">\n");
    }

    public String getTocEntry() {
        return IntStream.range(2, level).mapToObj(i -> "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").collect(Collectors.joining())
                       + "<a href=\"#" + anchor + "\">" + heading + "</a>";
    }

}
