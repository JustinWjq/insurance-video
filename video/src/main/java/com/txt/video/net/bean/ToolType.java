package com.txt.video.net.bean;

/**
 * Created by JustinWjq
 *
 * @date 2021/2/3.
 * description：
 */
public class ToolType {
    String name ;
    String color;
    boolean isSelect;

    public ToolType(String name, String type,boolean isSelect) {
        this.name = name;
        this.color = type;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
