/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aleksandarbroker.insurance.functions;

import android.content.Context;
import android.content.res.Resources;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.aleksandarbroker.insurance.R;

import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * @author Martin
 */
public class CustomDatePicker {

    Context mContext;

    public CustomDatePicker(Context context) {

        this.mContext = context;

    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static void setRange(DatePicker picker, int num, int num2) {

        Calendar c = Calendar.getInstance();

        long res = num * 86400000L;
        long res2 = num2 * 86400000L;

        picker.setMinDate(c.getTimeInMillis() - res);
        picker.setMaxDate(c.getTimeInMillis() - res2);

    }

    public void prepareDialog(DatePicker dp) {
        //mDialogTitle.setText(mTitle);
        try {
            Field datePickerFields[] = dp.getClass().getDeclaredFields();
            for (Field field : datePickerFields) {
                if ("mSpinners".equals(field.getName())) {
                    field.setAccessible(true);
                    Object spinnersObj = new Object();
                    spinnersObj = field.get(dp);
                    LinearLayout mSpinners = (LinearLayout) spinnersObj;
                    NumberPicker monthPicker = (NumberPicker) mSpinners.getChildAt(0);
                    NumberPicker dayPicker = (NumberPicker) mSpinners.getChildAt(1);
                    NumberPicker yearPicker = (NumberPicker) mSpinners.getChildAt(2);
                    setDividerColor(monthPicker);
                    setDividerColor(dayPicker);
                    setDividerColor(yearPicker);
                    break;
                }
            }
        } catch (Exception ex) {
            //Log.v(TAG, &quot;Unable to change date dialog style.&quot;);
        }
    }

    private void setDividerColor(NumberPicker picker) {
        Field[] numberPickerFields = NumberPicker.class.getDeclaredFields();
        for (Field field : numberPickerFields) {
            if (field.getName().equals("mSelectionDivider")) {
                field.setAccessible(true);
                try {
                    field.set(picker, mContext.getResources().getDrawable(R.drawable.dividers));
                } catch (IllegalArgumentException e) {
                    //Log.v(TAG, "Illegal Argument Exception");
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    //Log.v(TAG, "Resources NotFound");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    //Log.v(TAG, "Illegal Access Exception");
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
