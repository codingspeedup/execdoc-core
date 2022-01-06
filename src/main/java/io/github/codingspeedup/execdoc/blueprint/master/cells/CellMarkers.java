package io.github.codingspeedup.execdoc.blueprint.master.cells;

public class CellMarkers {

    // GLOBAL MARKERS

    public static final String ANCHOR_MARKER = "\u2318 ";
    public static final String PREDICATE_MARKER = ":";
    public static final String COMMENT_MARKER = "#";
    public static final String CHECK_MARKER = "\u2714";
    public static final String TOC_TYPE_MARKER = "\u22B2";

    public static final String TYPE_SUFFIX = "Type";

    // UI MARKERS

    public static final String NOT_L10N_MARKER = "!";
    public static final String CUSTOM_START_MARKER = "<";
    public static final String CUSTOM_END_MARKER = ">";

    // https://plantuml.com/salt
    public static final String BUTTON_START_MARKER = "[";
    public static final String BUTTON_END_MARKER = "]";
    public static final String RADIO_BUTTON_MARKER = "()";
    public static final String CHECKBOX_MARKER = "[]";
    public static final String TEXT_MARKER = "\"";
    public static final String DROP_LIST_MARKER = "ˆ";
    public static final String ICON_START_MARKER = CUSTOM_START_MARKER + "&";
    public static final String ICON_END_MARKER = CUSTOM_END_MARKER;

}
