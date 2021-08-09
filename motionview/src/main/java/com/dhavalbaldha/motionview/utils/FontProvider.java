package com.dhavalbaldha.motionview.utils;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * extracting Typeface from Assets is a heavy operation,
 * we want to make sure that we cache the typefaces for reuse
 */
public class FontProvider {

    private static final String DEFAULT_FONT_NAME = "Helvetica";

    private final Map<String, Typeface> typefaces;
    private final Map<String, String> fontNameToTypefaceFile;
    private final Resources resources;
    private final List<String> fontNames;

    public FontProvider(Resources resources, Map<String, String> fontNameToTypefaceFile) {
        this.resources = resources;
        typefaces = new HashMap<>();
        this.fontNameToTypefaceFile = fontNameToTypefaceFile;
        fontNames = new ArrayList<>(fontNameToTypefaceFile.keySet());
    }

    /**
     * @param typefaceName must be one of the font names provided from {@link FontProvider#getFontNames()}
     * @return the Typeface associated with {@code typefaceName}, or {@link Typeface#DEFAULT} otherwise
     */
    public Typeface getTypeface(@Nullable String typefaceName) {
        if (TextUtils.isEmpty(typefaceName)) {
            return Typeface.DEFAULT;
        } else {
            if (typefaces.get(typefaceName) == null) {
                typefaces.put(typefaceName,
                        Typeface.createFromAsset(resources.getAssets(), "fonts/" + fontNameToTypefaceFile.get(typefaceName)));
            }
            return typefaces.get(typefaceName);
        }
    }

    /**
     * use {@link FontProvider#getTypeface(String) to get Typeface for the font name}
     *
     * @return list of available font names
     */
    public List<String> getFontNames() {
        return fontNames;
    }

    /**
     * @return Default Font Name - <b>Helvetica</b>
     */
    public String getDefaultFontName() {
        return DEFAULT_FONT_NAME;
    }
}