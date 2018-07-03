package ru.r47717.eldorado.core.router;

public class SegmentData {
    int position;
    public boolean isParameter;
    String name;
    public String value;

    SegmentData() {
        isParameter = false;
    }

    SegmentData(SegmentData that) {
        this.position = that.position;
        this.isParameter = that.isParameter;
        this.name = that.name;
        this.value = that.value;
    }
}

