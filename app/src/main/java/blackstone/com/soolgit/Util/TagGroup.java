package blackstone.com.soolgit.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import blackstone.com.soolgit.DataClass.ThemeData;
import blackstone.com.soolgit.R;

public class TagGroup extends ViewGroup {

    private MyUtil mUtil;

    private final int default_border_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_text_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_background_color = Color.WHITE;
    private final int default_checked_border_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_checked_text_color = Color.WHITE;
    private final int default_checked_marker_color = Color.WHITE;
    private final int default_checked_background_color = Color.rgb(0x49, 0xC1, 0x20);
    private final float default_border_stroke_width;
    private final float default_text_size;
    private final float default_horizontal_spacing;
    private final float default_vertical_spacing;
    private final float default_horizontal_padding;
    private final float default_vertical_padding;

    private int borderColor;
    private int textColor;
    private int backgroundColor;
    private int checkedBorderColor;
    private int checkedTextColor;
    private int checkedMarkerColor;
    private int checkedBackgroundColor;
    private float borderStrokeWidth;
    private float textSize;
    private int horizontalSpacing;
    private int verticalSpacing;
    private int horizontalPadding;
    private int verticalPadding;

    private OnTagClickListener mOnTagClickListener;
    private InternalTagClickListener mInternalTagClickListener = new InternalTagClickListener();

    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tagGroupStyle);
    }

    public TagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mUtil = new MyUtil(context);
        default_border_stroke_width = dp2px(0.5f);
        default_text_size = sp2px(13.0f);
        default_horizontal_spacing = dp2px(8.0f);
        default_vertical_spacing = dp2px(4.0f);
        default_horizontal_padding = dp2px(12.0f);
        default_vertical_padding = dp2px(3.0f);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagGroup, defStyleAttr, R.style.TagGroup);
        try {
            borderColor = a.getColor(R.styleable.TagGroup_atg_borderColor, default_border_color);
            textColor = a.getColor(R.styleable.TagGroup_atg_textColor, default_text_color);
            backgroundColor = a.getColor(R.styleable.TagGroup_atg_backgroundColor, default_background_color);
            checkedBorderColor = a.getColor(R.styleable.TagGroup_atg_checkedBorderColor, default_checked_border_color);
            checkedTextColor = a.getColor(R.styleable.TagGroup_atg_checkedTextColor, default_checked_text_color);
            checkedMarkerColor = a.getColor(R.styleable.TagGroup_atg_checkedMarkerColor, default_checked_marker_color);
            checkedBackgroundColor = a.getColor(R.styleable.TagGroup_atg_checkedBackgroundColor, default_checked_background_color);
            borderStrokeWidth = a.getDimension(R.styleable.TagGroup_atg_borderStrokeWidth, default_border_stroke_width);
            textSize = a.getDimension(R.styleable.TagGroup_atg_textSize, default_text_size);
            horizontalSpacing = (int) a.getDimension(R.styleable.TagGroup_atg_horizontalSpacing, default_horizontal_spacing);
            verticalSpacing = (int) a.getDimension(R.styleable.TagGroup_atg_verticalSpacing, default_vertical_spacing);
            horizontalPadding = (int) a.getDimension(R.styleable.TagGroup_atg_horizontalPadding, default_horizontal_padding);
            verticalPadding = (int) a.getDimension(R.styleable.TagGroup_atg_verticalPadding, default_vertical_padding);
        } finally {
            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int row = 0;
        int rowWidth = 0;
        int rowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                rowWidth += childWidth;
                if (rowWidth > widthSize) {
                    rowWidth = childWidth;
                    height += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = childHeight;
                    row++;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
                rowWidth += horizontalSpacing;
            }
        }
        height += rowMaxHeight;

        height += getPaddingTop() + getPaddingBottom();

        if (row == 0) {
            width = rowWidth;
            width += getPaddingLeft() + getPaddingRight();
        } else {
            width = widthSize;
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int childLeft = parentLeft;
        int childTop = parentTop;

        int rowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                if (childLeft + width > parentRight) { // Next line
                    childLeft = parentLeft;
                    childTop += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = height;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, height);
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);

                childLeft += width + horizontalSpacing;
            }
        }
    }

    public String[] getTags() {
        final int count = getChildCount();
        final List<String> tagList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final TagView tagView = getTagAt(i);
            tagList.add(tagView.getText().toString());
        }

        return tagList.toArray(new String[tagList.size()]);
    }

    public void setTags(ArrayList<ThemeData> dataList) {
        removeAllViews();
        for (final ThemeData data : dataList) {
            appendTag(data);
        }
    }

    protected TagView getTagAt(int index) {
        return (TagView) getChildAt(index);
    }

    public ArrayList<ThemeData> getCheckedTags() {
        final int count = getChildCount();
        final ArrayList<ThemeData> arrayList = new ArrayList();
        for (int i = 0; i < count; i++) {
            final TagView tag = getTagAt(i);
            if (tag.isChecked) {
                arrayList.add(tag.data);
            }
        }
        return arrayList;
    }

    protected void appendTag(ThemeData data) {
        for(ThemeData themeData : mUtil.getFILTERTHEME()) {
            if(themeData.getTHEME_ID() == data.getTHEME_ID()) {
                data.setTHEME_CHECKED(true);
                final TagView newTag = new TagView(getContext(), data);
                newTag.setOnClickListener(mInternalTagClickListener);
                newTag.setChecked(true);
                addView(newTag);
                return;
            }
        }
        final TagView newTag = new TagView(getContext(), data);
        newTag.setOnClickListener(mInternalTagClickListener);
        addView(newTag);
    }

    protected float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    protected float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TagGroup.LayoutParams(getContext(), attrs);
    }

    public void setOnTagClickListener(OnTagClickListener l) {
        mOnTagClickListener = l;
    }

    public interface OnTagClickListener {
        void onTagClick(int checkedCount);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    class InternalTagClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            final TagView tag = (TagView) v;
            if (tag.data.getTHEME_CHECKED()) {
                tag.setChecked(false);
                tag.data.setTHEME_CHECKED(false);
            } else {
                tag.setChecked(true);
                tag.data.setTHEME_CHECKED(true);
            }
            if (mOnTagClickListener != null) {
                mOnTagClickListener.onTagClick(getCheckedTags().size());
            }
        }
    }

    class TagView extends TextView {

        private static final int CHECKED_MARKER_STROKE_WIDTH = 4;
        private boolean isChecked = false;
        private ThemeData data;
        private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Paint mCheckedMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private RectF mLeftCornerRectF = new RectF();
        private RectF mRightCornerRectF = new RectF();
        private RectF mHorizontalBlankFillRectF = new RectF();
        private RectF mVerticalBlankFillRectF = new RectF();
        private Path mBorderPath = new Path();

        {
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(borderStrokeWidth);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            mCheckedMarkerPaint.setStyle(Paint.Style.FILL);
            mCheckedMarkerPaint.setStrokeWidth(CHECKED_MARKER_STROKE_WIDTH);
            mCheckedMarkerPaint.setColor(checkedMarkerColor);
        }

        public TagView(Context context, ThemeData data) {
            super(context);
            this.data = data;
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding*3/2);
            setLayoutParams(new TagGroup.LayoutParams(
                    TagGroup.LayoutParams.WRAP_CONTENT,
                    TagGroup.LayoutParams.WRAP_CONTENT));
            setGravity(Gravity.CENTER);
            setText(data.getTHEME_NM());
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            setClickable(true);
            invalidatePaint();
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
            invalidatePaint();
        }

        @Override
        protected boolean getDefaultEditable() {
            return true;
        }

        private void invalidatePaint() {
            mBorderPaint.setPathEffect(null);
            if (isChecked) {
                mBorderPaint.setColor(checkedBorderColor);
                mBackgroundPaint.setColor(checkedBackgroundColor);
                setTextColor(checkedTextColor);
            } else {
                mBorderPaint.setColor(borderColor);
                mBackgroundPaint.setColor(backgroundColor);
                setTextColor(textColor);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawArc(mLeftCornerRectF, -180, 90, true, mBackgroundPaint);
            canvas.drawArc(mLeftCornerRectF, -270, 90, true, mBackgroundPaint);
            canvas.drawArc(mRightCornerRectF, -90, 90, true, mBackgroundPaint);
            canvas.drawArc(mRightCornerRectF, 0, 90, true, mBackgroundPaint);
            canvas.drawRect(mHorizontalBlankFillRectF, mBackgroundPaint);
            canvas.drawRect(mVerticalBlankFillRectF, mBackgroundPaint);

            canvas.drawPath(mBorderPath, mBorderPaint);
            super.onDraw(canvas);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            int left = (int) borderStrokeWidth;
            int top = (int) borderStrokeWidth;
            int right = (int) (left + w - borderStrokeWidth * 2);
            int bottom = (int) (top + h - borderStrokeWidth * 2);

            int d = bottom - top;

            mLeftCornerRectF.set(left, top, left + d, top + d);
            mRightCornerRectF.set(right - d, top, right, top + d);

            mBorderPath.reset();
            mBorderPath.addArc(mLeftCornerRectF, -180, 90);
            mBorderPath.addArc(mLeftCornerRectF, -270, 90);
            mBorderPath.addArc(mRightCornerRectF, -90, 90);
            mBorderPath.addArc(mRightCornerRectF, 0, 90);

            int l = (int) (d / 2.0f);
            mBorderPath.moveTo(left + l, top);
            mBorderPath.lineTo(right - l, top);

            mBorderPath.moveTo(left + l, bottom);
            mBorderPath.lineTo(right - l, bottom);

            mBorderPath.moveTo(left, top + l);
            mBorderPath.lineTo(left, bottom - l);

            mBorderPath.moveTo(right, top + l);
            mBorderPath.lineTo(right, bottom - l);

            mHorizontalBlankFillRectF.set(left, top + l, right, bottom - l);
            mVerticalBlankFillRectF.set(left + l, top, right - l, bottom);

        }
    }
}
