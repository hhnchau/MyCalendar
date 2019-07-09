package com.example.chauhn.mycalendar.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.chauhn.mycalendar.R;
import com.example.chauhn.mycalendar.interfaces.OnDayViewClickListener;
import com.example.chauhn.mycalendar.objects.CalendarDate;
import com.example.chauhn.mycalendar.objects.CalendarMonth;
import com.example.chauhn.mycalendar.utils.Utils;

import java.util.Calendar;


public class CalendarMonthView extends FrameLayout implements View.OnClickListener {

    private GridLayout mGridLayout;
    private ViewGroup mLayoutDays;
    private OnDayViewClickListener mListener;
    private CalendarDate mSelectedDate;

    public CalendarMonthView(Context context) {
        super(context);
        init();
    }

    public void setOnDayViewClickListener(OnDayViewClickListener listener) {
        mListener = listener;
    }

    public void setSelectedDate(CalendarDate selectedDate) {
        mSelectedDate = selectedDate;
    }

    private void init() {
        inflate(getContext(), R.layout.view_calendar_month, this);
        mGridLayout = findViewById(R.id.view_calendar_month_grid);
        mLayoutDays = findViewById(R.id.view_calendar_month_layout_days);
    }

    public void buildView(CalendarMonth calendarMonth) {
        buildDaysLayout();
        buildGridView(calendarMonth);
    }

    private void buildDaysLayout() {
        String[] days;
        days = getResources().getStringArray(R.array.days_sunday_array);

        for (int i = 0; i < mLayoutDays.getChildCount(); i++) {
            TextView textView = (TextView) mLayoutDays.getChildAt(i);
            textView.setText(days[i]);
        }
    }

    private void buildGridView(CalendarMonth calendarMonth) {
        int row = CalendarMonth.NUMBER_OF_WEEKS_IN_MONTH;
        int col = CalendarMonth.NUMBER_OF_DAYS_IN_WEEK;
        mGridLayout.setRowCount(row);
        mGridLayout.setColumnCount(col);

        int screenWidth = Utils.getScreenWidth(getContext());
        int width = screenWidth / col;

        for (CalendarDate date : calendarMonth.getDays()) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = width;
            params.height = LayoutParams.WRAP_CONTENT;

            CalendarDayView dayView = new CalendarDayView(getContext(), date);
            dayView.setContentDescription(date.toString());
            dayView.setLayoutParams(params);
            dayView.setOnClickListener(this);
            decorateDayView(dayView, date, calendarMonth.getMonth());
            mGridLayout.addView(dayView);
        }
    }

    private void decorateDayView(CalendarDayView dayView, CalendarDate day, int month) {
        boolean isPast = true;
        Calendar currentCalendar = Calendar.getInstance();
        int currentDay = currentCalendar.get(Calendar.DAY_OF_WEEK);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);

        //So sanh voi ngay hien tai -> khon cho pheo chon ngay qua khu
        // day < currentCalendar < day

        if (isPast) {

        } else if (day.getMonth() != month) {
            dayView.setOtherMothTextColor();
        } else {
            dayView.setThisMothTextColor();
        }

        if (mSelectedDate != null && mSelectedDate.isDateEqual(day)) {
            dayView.setPurpleSolidOvalBackground();
        } else {
            dayView.unsetPurpleSolidOvalBackground();
        }
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onDayViewClick((CalendarDayView) view);
        }
    }
}
