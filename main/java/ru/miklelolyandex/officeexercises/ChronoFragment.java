package ru.miklelolyandex.officeexercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.io.Console;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChronoFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_chrono,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            int myInt = bundle.getInt("setCalendar", 0);
//            if (myInt == 1) { setCalendarEvent(); }
//        }
        String events = readEvents();
        getDates(events);
    }

    private void getDates(String events) {
        List<EventDay> eventsDates = new ArrayList<>();
        Boolean first = false;
        Boolean second = false;
        int year = 0;
        int month = 0;
        int day = 0;
        for (char c: events.toCharArray()) {
            if (c == ' '){
                if (!first) {
                    first = true;
                } else { second = true; }
            }
            else if (c == ','){
                setCalendarEvent(year, month, day, eventsDates);
                year = 0;
                month = 0;
                day = 0;
                first = false;
                second= false;
            } else {
                if (!first) {
                    year = year*10 + (c - '0');
                }
                if (first && !second) {
                    month = month *10 + (c-'0');
                }
                if (second) {
                    day = day*10 + (c-'0');
                }
            }
        }
        setEvents(eventsDates);
    }

    private String readEvents(){
       FileInputStream inputStream;
       String text ="";
        try {
            inputStream = getContext().openFileInput("events");
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            text = new String (bytes);
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    private void setCalendarEvent(int year, int month, int day, List<EventDay> events ){
        Log.d("year",String.valueOf(year));
        Log.d("month",String.valueOf(month));
        Log.d("day",String.valueOf(day));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        events.add(new EventDay(calendar, R.drawable.ic_dot_black_24dp));
    }

    private void setEvents(List<EventDay> events) {
        CalendarView calendarView = getView().findViewById(R.id.calendarView);
        calendarView.setEvents(events);
    }
}