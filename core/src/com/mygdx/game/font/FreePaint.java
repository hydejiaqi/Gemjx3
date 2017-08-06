package com.mygdx.game.font;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by hydej on 2017/8/5.
 */

public class FreePaint {

    private int textSize = 30;// 字号
    private Color color = Color.WHITE;// 颜色
    private boolean isFakeBoldText = false;// 是否粗体
    private boolean isUnderlineText = false;// 是否下划线
    private boolean isStrikeThruText = false;// 是否删除线
    private Color strokeColor = null;// 描边颜色
    private int strokeWidth = 3;// 描边宽度

    public String getName() {
        StringBuffer name = new StringBuffer();
        name.append(textSize);
        name.append("_");
        name.append(color.toIntBits());
        name.append("_");
        name.append(booleanToInt(isFakeBoldText));
        name.append("_");
        name.append(booleanToInt(isUnderlineText));
        if (strokeColor != null) {
            name.append("_");
            name.append(strokeColor.toIntBits());
            name.append("_");
            name.append(strokeWidth);
        }
        return name.toString();
    }

    private int booleanToInt(boolean b) {
        return b == true ? 0 : 1;
    }

    public FreePaint() {
    }

    public FreePaint(int textSize, Color color, Color stroke, int strokeWidth,
                     boolean bold, boolean line, boolean thru) {
        this.textSize = textSize;
        this.color = color;
        this.strokeColor = stroke;
        this.strokeWidth = strokeWidth;
        this.isFakeBoldText = bold;
        this.isUnderlineText = line;
        this.isStrikeThruText = thru;
    }

    public FreePaint(int size) {
        this.textSize = size;
    }

    public FreePaint(Color color) {
        this.color = color;
    }

    public FreePaint(int size, Color color) {
        this.textSize = size;
        this.color = color;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean getFakeBoldText() {
        return isFakeBoldText;
    }

    public void setFakeBoldText(boolean isFakeBoldText) {
        this.isFakeBoldText = isFakeBoldText;
    }

    public boolean getUnderlineText() {
        return isUnderlineText;
    }

    public void setUnderlineText(boolean isUnderlineText) {
        this.isUnderlineText = isUnderlineText;
    }

    public boolean getStrikeThruText() {
        return isStrikeThruText;
    }

    public void setStrikeThruText(boolean isStrikeThruText) {
        this.isStrikeThruText = isStrikeThruText;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
}



