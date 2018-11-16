package com.mashpy.nstuinfo.mOtherUtils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;

/**
 * Created by whoami on 10/29/2018.
 */

public class StringUtil {

    // UNUSED METHOD
    public static void removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for(URLSpan span:spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
    }

    public static String getColorfulText(String s) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString redSpannable= new SpannableString(s);
        redSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
        builder.append(redSpannable);
        return builder.toString();
    }

}
