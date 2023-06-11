package com.godokan.rutiner;

public class ListItem {
    private final String id;
    private final String type;
    private final String name;
    private final String context;
    private final String date;
    private final String flag;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }

    public String getDate() {
        return date;
    }

    public String getFlag() {
        return flag;
    }

    public ListItem(String id, String type, String name, String context, String date, String flag) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.context = context;
        this.date = date;
        this.flag = flag;
    }
}
