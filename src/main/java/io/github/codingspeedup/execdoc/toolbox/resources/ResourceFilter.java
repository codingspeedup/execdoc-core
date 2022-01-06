package io.github.codingspeedup.execdoc.toolbox.resources;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceFilter {

    public static final String REGEX_MARKER = "/";

    private final String plainPattern;
    private final Pattern regexPattern;

    private ResourceFilter(String plainPattern) {
        this.plainPattern = plainPattern;
        this.regexPattern = null;
    }

    private ResourceFilter(Pattern regexPattern) {
        this.plainPattern = null;
        this.regexPattern = regexPattern;
    }

    public static ResourceFilter from(String pattern) {
        if (StringUtils.isNotBlank(pattern)) {
            if (pattern.startsWith(REGEX_MARKER)) {
                int lastMarkerIndex = pattern.lastIndexOf(REGEX_MARKER);
                if (lastMarkerIndex > 1) {
                    return new ResourceFilter(Pattern.compile(pattern.substring(1, lastMarkerIndex)));
                }
            }
            return new ResourceFilter(pattern.toLowerCase(Locale.ROOT));
        } else {
            return new ResourceFilter((String) null);
        }
    }

    public boolean accept(Resource resource) {
        if (regexPattern != null) {
            Matcher matcher = regexPattern.matcher(resource.toString());
            return matcher.find();
        } else if (plainPattern != null) {
            return resource.toString().toLowerCase(Locale.ROOT).contains(plainPattern);
        }
        return true;
    }

    @Override
    public String toString() {
        if (regexPattern != null) {
            return REGEX_MARKER + regexPattern.pattern() + REGEX_MARKER;
        }
        return plainPattern;
    }

}
