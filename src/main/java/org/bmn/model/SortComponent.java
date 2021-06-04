package org.bmn.model;

import java.util.Comparator;

public class SortComponent implements Comparator<Component> {

    @Override
    public int compare(Component o1, Component o2) {
        String name1 = o1.getName();
        String name2 = o2.getName();

        return name1.compareTo(name2);
    }
}
