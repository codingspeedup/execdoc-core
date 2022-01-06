package io.github.codingspeedup.execdoc.blueprint.master.sheets;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class SheetNameComparator implements Comparator<String> {

    private final List<String> markers;
    private final List<Integer> permut = new ArrayList<>();

    public SheetNameComparator(List<String> markers) {
        this.markers = markers;
        IntStream.range(0, markers.size()).forEach(permut::add);
        permut.sort((o1, o2) -> -Integer.compare(markers.get(o1).length(), markers.get(o2).length()));
    }

    public static boolean isModelSheet(String marker, String name) {
        return marker.endsWith(BlueprintMaster.INSTANTIABLE_SHEET_MARKER) && !StringUtils.isBlank(name)
                       || !marker.endsWith(BlueprintMaster.INSTANTIABLE_SHEET_MARKER) && StringUtils.isBlank(name);
    }

    @Override
    public int compare(String o1, String o2) {
        Object[] indexName1 = split(o1);
        Object[] indexName2 = split(o2);
        int cmp = Integer.compare((int) indexName1[0], (int) indexName2[0]);
        if (cmp == 0) {
            cmp = ((String) indexName1[1]).compareTo((String) indexName2[1]);
        }
        return cmp;
    }

    private Object[] split(String o1) {
        int index = markers.size() * 2;
        String name = o1;
        for (int markerIndex : permut) {
            String marker = markers.get(markerIndex);
            if (o1.startsWith(marker)) {
                name = o1.substring(marker.length());
                if (isModelSheet(marker, name)) {
                    index = markerIndex;
                } else if (StringUtils.isBlank(name)) {
                    index = markerIndex + markers.size();
                } else {
                    name = o1;
                }
                break;
            }
        }
        return new Object[]{index, name};
    }

}
