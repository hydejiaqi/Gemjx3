package com.mygdx.game.font;

import com.badlogic.gdx.graphics.Pixmap;

/**
 * Created by hydej on 2017/8/5.
 */

public interface FreeListener {

    // 返回一个字符纹理
    public Pixmap getFontPixmap(String txt, FreePaint vpaint);
}



