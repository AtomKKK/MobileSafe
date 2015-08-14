package irac.com.mobliesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/8/14.
 * 自定义一个TextView 天生带焦点
 */
public class FocusTextView extends TextView {
    public FocusTextView(Context context) {
        super(context);
    }

    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *
     * 当前并没有焦点，只是欺骗Android系统以获得焦点的情况处理
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
