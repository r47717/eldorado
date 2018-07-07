package ru.r47717.eldorado.core.router;


public class SegmentData {
    int position;
    private boolean isParameter;
    private String name;
    private String value;

    SegmentData() {
        isParameter = false;
    }

    SegmentData(SegmentData that) {
        this.position = that.position;
        this.isParameter = that.isParameter;
        this.name = that.name;
        this.value = that.value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean getIsParameter() {
        return isParameter;
    }

    public void setIsParameter(boolean parameter) {
        isParameter = parameter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

