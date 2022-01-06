package io.github.codingspeedup.execdoc.blueprint.master.cells;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CellComment {

    public static final String DOC_START = "/*";
    public static final String DOC_END = "*/";
    public static final String ANNOTATION_MARKER = "@";
    public static final String TAG_VALUE_SEPARATOR = "=";

    public static final String ATTRIBUTE_BPID = "bpid";

    @Getter
    @Setter
    private String documentation;

    private Map<String, String> annotations;

    private Map<String, String> attributes;

    public static CellComment parse(String comment) {
        if (comment == null) {
            comment = "";
        }
        CellComment cc = new CellComment();
        int docStart = comment.indexOf(DOC_START);
        int docEnd = comment.indexOf(DOC_END, docStart);
        if (docStart >= 0 || docEnd >= 0) {
            if (docStart < 0) {
                docStart = 0;
            }
            if (docEnd < 0) {
                docEnd = comment.length();
            } else {
                docEnd += DOC_END.length();
            }
            String documentation = comment.substring(docStart, docEnd);
            if (documentation.length() >= DOC_START.length() + DOC_END.length()) {
                if (documentation.startsWith(DOC_START)) {
                    documentation = documentation.substring(DOC_START.length());
                }
                if (documentation.endsWith(DOC_END)) {
                    documentation = documentation.substring(0, documentation.length() - DOC_END.length());
                }
                cc.setDocumentation(documentation);
                comment = comment.substring(0, docStart) + comment.substring(docEnd);
            }
        }
        String[] lines = comment.split("\n");
        for (String line : lines) {
            if (line != null) {
                line = StringUtils.stripStart(line, null);
                if (line.startsWith(ANNOTATION_MARKER)) {
                    line = line.substring(ANNOTATION_MARKER.length()).trim();
                    if (!line.isEmpty()) {
                        String name = line;
                        String value = "";
                        int opIdx = line.indexOf("(");
                        if (opIdx >= 0) {
                            int cpIdx = line.lastIndexOf(")");
                            name = line.substring(0, opIdx);
                            value = line.substring(opIdx + 1, cpIdx);
                        }
                        name = StringUtils.trimToEmpty(name);
                        cc.getAnnotations().put(name, value);
                    }
                } else if (line.contains(TAG_VALUE_SEPARATOR)) {
                    String[] tagValue = line.split(TAG_VALUE_SEPARATOR, 2);
                    String tag = tagValue[0].trim();
                    if (!tag.isEmpty()) {
                        String value = "";
                        if (tagValue.length > 1) {
                            value = tagValue[1];
                        }
                        cc.getAttributes().put(tag, value);
                    }
                }
            }
        }
        return cc;
    }

    public Map<String, String> getAnnotations() {
        if (annotations == null) {
            annotations = new LinkedHashMap<>();
        }
        return annotations;
    }

    public Map<String, String> getAttributes() {
        if (attributes == null) {
            attributes = new LinkedHashMap<>();
        }
        return attributes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(documentation)) {
            sb.append(DOC_START);
            sb.append(documentation);
            sb.append(DOC_END);
        }
        if (MapUtils.isNotEmpty(annotations)) {
            if (sb.length() > 0) {
                sb.append("\n\n");
            }
            List<String> aList = new ArrayList<>();
            Map<String, String> aMap = new HashMap<>();
            for (Map.Entry<String, String> entry : annotations.entrySet()) {
                String tag = entry.getKey().trim();
                if (StringUtils.isNotBlank(entry.getKey())) {
                    if (aMap.containsKey(tag)) {
                        throw new UnsupportedOperationException("Duplicate key " + tag);
                    }
                    aMap.put(tag, entry.getValue());
                    aList.add(tag);
                }
            }
            Collections.sort(aList);
            for (String tag : aList) {
                sb.append("@").append(tag);
                if (StringUtils.isNotBlank(aMap.get(tag))) {
                    sb.append("(").append(aMap.get(tag)).append(")");
                }
                sb.append("\n");
            }
        }
        if (MapUtils.isNotEmpty(attributes)) {
            if (sb.length() > 0) {
                if (sb.charAt(sb.length() - 1) != '\n') {
                    sb.append("\n");
                }
                sb.append("\n");
            }
            List<String> aList = new ArrayList<>();
            Map<String, String> aMap = new HashMap<>();
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                String tag = entry.getKey().trim();
                if (StringUtils.isNotBlank(entry.getKey())) {
                    if (aMap.containsKey(tag)) {
                        throw new UnsupportedOperationException("Duplicate key " + tag);
                    }
                    aMap.put(tag, entry.getValue());
                    aList.add(tag);
                }
            }
            Collections.sort(aList);
            for (String tag : aList) {
                sb.append(tag).append(TAG_VALUE_SEPARATOR).append(aMap.get(tag)).append("\n");
            }
        }
        return sb.toString();
    }

}
