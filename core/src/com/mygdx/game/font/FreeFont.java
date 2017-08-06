package com.mygdx.game.font;



import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.PixmapPacker.Page;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by hydej on 2017/8/5.
 */

public class FreeFont  implements Disposable {

    private HashMap<String, BitmapFont> fonts = new HashMap<String, BitmapFont>();// font列表
    private HashMap<BitmapFont, FreePaint> paints = new HashMap<BitmapFont, FreePaint>();// 画笔列表
    private HashMap<String, FreeFontParameter> pars = new HashMap<String, FreeFontParameter>();// 配置列表
    public static String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*";
    private int pageWidth = 512;
    private FreeListener listener;

    public FreeFont(FreeListener listener) {
        this.listener = listener;
    }

    private HashMap emojis = new HashMap();

    public void addEmoji(String str, String imgname) {
        emojis.put(str, imgname);
    }

    public BitmapFont getFont(String characters) {
        return getFont(new FreePaint(), characters);
    }

    public BitmapFont getFont(FreePaint vpaint, String characters) {
        characters = characters.replaceAll("(?s)(.)(?=.*\\1)", "");
        BitmapFont font = (BitmapFont) fonts.get(vpaint.getName());
        if (font == null) {
// 至少要保证创建一个字符
            font = createBitmapFont(vpaint, characters.length() == 0 ? "l"
                    : characters);
            fonts.put(vpaint.getName(), font);
            paints.put(font, vpaint);
        } else {
// 判断font里是否有相应的字符，没有的字符需要创建
            if (characters.length() != 0)
                appendChars(vpaint, characters);
        }
        return font;
    }

    private void setGlyph(String txt, int c, Rectangle rect, int pIndex,
                          BitmapFont.BitmapFontData data) {
        BitmapFont.Glyph glyph = new BitmapFont.Glyph();
        glyph.id = c;
        glyph.page = pIndex;
        glyph.srcX = (int) rect.x;
        glyph.srcY = (int) rect.y;
        glyph.width = (int) rect.width;
        glyph.height = (int) rect.height;
        glyph.xadvance = glyph.width;
        data.setGlyph(c, glyph);
    }

    private BitmapFont createBitmapFont(FreePaint vpaint, String characters) {
        FreeFontParameter parameter =(FreeFontParameter) pars.get(vpaint.getName());
        if (parameter == null) {
            parameter = new FreeFontParameter();
            parameter.size = vpaint.getTextSize();
            parameter.packer = new PixmapPacker(pageWidth, pageWidth,
                    Format.RGBA8888, 2, false);
            parameter.characters = characters;
            pars.put(vpaint.getName(), parameter);
        }
        PixmapPacker packer = parameter.packer;
        FreeData data = new FreeData();
        data.down = -parameter.size;
        data.ascent = -parameter.size;
        data.capHeight = parameter.size;
        String packPrefix = parameter.size + "_";
        for (int i = 0, len = parameter.characters.length(); i < len; i++) {
            String txt = parameter.characters.substring(i, i + 1);
            String emj = (String) emojis.get(txt);
            Pixmap pixmap = null;
            if (emj == null) {
                pixmap = listener.getFontPixmap(txt, vpaint);
            } else {
                pixmap = new Pixmap(Gdx.files.internal(emj));
            }
            int c = txt.charAt(0);
            String name = packPrefix + c;
            Rectangle rect = packer.pack(name, pixmap);
            pixmap.dispose();
            int pIndex = packer.getPageIndex(name);
            setGlyph(txt, c, rect, pIndex, data);
        }
        BitmapFont.Glyph spaceGlyph = data.getGlyph(' ');
        if (spaceGlyph == null) {
            spaceGlyph = new Glyph();
            Glyph xadvanceGlyph = data.getGlyph('l');
            if (xadvanceGlyph == null)
                xadvanceGlyph = data.getFirstGlyph();
            spaceGlyph.xadvance = xadvanceGlyph.xadvance;
            spaceGlyph.id = (int) ' ';
            data.setGlyph(' ', spaceGlyph);
        }
        data.spaceWidth = spaceGlyph != null ? spaceGlyph.xadvance
                + spaceGlyph.width : 1;
        Array pages = packer.getPages();
        for (int i = 0; i < pages.size; i++) {
            Page p = (Page) pages.get(i);
            p.updateTexture(parameter.minFilter, parameter.magFilter, false);
            data.getTextureRegions().add(new TextureRegion(p.getTexture()));
        }
        return new BitmapFont(data, data.getTextureRegions(), false);
    }

    // 增加字符纹理
    public void appendChars(BitmapFont font, String characters) {
        if (characters.length() != 0)
            appendChars(paints.get(font), characters);
    }

    // 增加字符纹理
    public void appendChars(FreePaint vpaint, String characters) {
        characters = characters.replaceAll("(?s)(.)(?=.*\\1)", "");
        FreeFontParameter parameter = (FreeFontParameter) pars.get(vpaint.getName());
        if (parameter == null)
            throw new IllegalArgumentException("BitmapFont未创建过");
        BitmapFont font = (BitmapFont) fonts.get(vpaint.getName());
        BitmapFont.BitmapFontData data = font.getData();
        Array regions = font.getRegions();
        PixmapPacker packer = parameter.packer;
        String packPrefix = parameter.size + "_";
// 拥有过的字符不要再创建
        boolean isUpdata = false;// 是否提交了新字符
        for (int i = 0, len = characters.length(); i < len; i++) {
            String txt = characters.substring(i, i + 1);
            int c = txt.charAt(0);
            String name = packPrefix + c;
            if (packer.getRect(name) != null)
                continue;
            isUpdata = true;
            String emj = (String) emojis.get(txt);
            Pixmap pixmap = null;
            if (emj == null) {
                pixmap = listener.getFontPixmap(txt, vpaint);
            } else {
                pixmap = new Pixmap(Gdx.files.internal(emj));
            }
            Rectangle rect = packer.pack(name, pixmap);
            pixmap.dispose();
            int pIndex = packer.getPageIndex(name);
            setGlyph(txt, c, rect, pIndex, data);
        }
        if (!isUpdata)
            return;
        Array pages = packer.getPages();
        for (int i = 0, regSize = regions.size - 1; i < pages.size; i++) {
            PixmapPacker.Page p = (PixmapPacker.Page)pages.get(i);
            if (i > regSize) {
                p.updateTexture(parameter.minFilter, parameter.magFilter, false);
                regions.add(new TextureRegion(p.getTexture()));
            } else if (p.updateTexture(parameter.minFilter,
                    parameter.magFilter, false)) {
                regions.set(i, new TextureRegion(p.getTexture()));
            }
        }
        for (BitmapFont.Glyph[] page : data.glyphs) {
            if (page == null)
                continue;
            for (BitmapFont.Glyph glyph : page) {
                if (glyph == null)
                    continue;
                TextureRegion region = (TextureRegion) regions.get(glyph.page);
                if (region == null) {
                    throw new IllegalArgumentException(
                            "BitmapFont texture region array cannot contain null elements.");
                }
                data.setGlyphRegion(glyph, region);
            }
        }
    }

    public static class FreeData extends BitmapFont.BitmapFontData {
        Array regions = new Array();

        public Array getTextureRegions() {
            return regions;
        }
    }

    public void dispose() {
        for (BitmapFont font : fonts.values()) {
            font.dispose();
            font = null;
        }
        fonts.clear();
        for (FreeFontParameter par : pars.values()) {
            par.characters = null;
            par.packer.dispose();
            par = null;
        }
        pars.clear();
        for (@SuppressWarnings("unused")
                FreePaint v : paints.values()) {
            v = null;
        }
        paints.clear();
    }

    public static class FreeFontParameter {
        public int size = 30;
        public String characters = DEFAULT_CHARS;
        public PixmapPacker packer = null;
        public boolean flip = false;
        public boolean genMipMaps = false;
        public TextureFilter minFilter = TextureFilter.Linear;
        public TextureFilter magFilter = TextureFilter.Linear;
    }
}





