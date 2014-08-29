package com.guguluk.sakus.util;

import com.guguluk.sakus.dto.Line;

import java.util.Comparator;

/**
 * Created by guguluk on 29.08.2014.
 */
public class LineListComparator implements Comparator<Line> {
    @Override
    public int compare(Line o1, Line o2) {
        return o1.getName().split("-")[0].compareTo(o2.getName().split("-")[0]);
    }
}
