package com.aleksandarbroker.insurance.functions;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aleksandarbroker.insurance.R;

/**
 * @author DMS
 */
public class CustomDialog {

    public static class Builder {

        Context mContext;

        LayoutInflater inflater;
        View view;

        PopupWindow popup;

        TextView dialogMessage;

        DatePicker datePicker;

        ListView listView;

        int i = 0;
        View.OnTouchListener customPopUpTouchListenr = new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                Log.d("POPUP", "Touch false");
                return false;
            }

        };

        public Builder(Context context) {

            this.mContext = context;

            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.dialog, null);

            popup = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
            popup.setContentView(view);
            popup.setAnimationStyle(R.style.Animation);

            dialogMessage = new TextView(context);
            datePicker = new DatePicker(context);
            listView = new ListView(context);

        }

        public void setContentView(View view) {

            popup.setContentView(view);

        }

        public void setTitle(CharSequence title) {

            TextView dialogTitle = (TextView) view.findViewById(R.id.myTitle);
            dialogTitle.setText(title);

        }

        public void setTitle(int title) {

            TextView dialogTitle = (TextView) view.findViewById(R.id.myTitle);
            dialogTitle.setText(title);

        }

        public void setMessage(CharSequence message) {

            dialogMessage.setText(message);
            dialogMessage.setTextAppearance(mContext, R.style.Message);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            textParams.gravity = Gravity.CENTER;
            textParams.setMargins(dpToPx(20), dpToPx(25), dpToPx(20), dpToPx(25));

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.message);

            addTo.addView(dialogMessage, textParams);

        }

        private void addBorder() {

            LinearLayout addBorder = (LinearLayout) view.findViewById(R.id.main);
            LinearLayout.LayoutParams borderParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            TextView border = new TextView(mContext);

            borderParams.width = LayoutParams.MATCH_PARENT;
            borderParams.height = dpToPx(2);

            border.setBackgroundResource(R.color.dialog_border);

            addBorder.addView(border, borderParams);

        }

        public void show() {

            popup.setOutsideTouchable(false);
            popup.setTouchable(true);
            popup.setTouchInterceptor(customPopUpTouchListenr);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    popup.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
            }, 100L);

        }

        public void dismiss() {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    popup.dismiss();

                }
            }, 100L);
            //Looper.loop();
        }

        public void addButton(Context context, String buttonText, View.OnClickListener listener) {

            if (i == 0) {

                addBorder();

            }

            i++;

            Button myButton = new Button(context);
            myButton.setText(buttonText);
            myButton.setBackgroundResource(R.drawable.button_dialog);
            myButton.setOnClickListener(listener);

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.main);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            addTo.addView(myButton, buttonParams);

        }

        public void addButtonGroup(Context context, String buttonText, View.OnClickListener listener) {

            if (i == 0) {

                addBorder();

            }

            i++;

            Button myButton = new Button(context);
            myButton.setText(buttonText);
            myButton.setBackgroundResource(R.drawable.button_dialog);
            myButton.setOnClickListener(listener);

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.btngroup);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams borderParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            if (buttonText.length() > 4) {

                myButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            }

            if (i == 3) {

                View border = new View(context);

                borderParams.width = dpToPx(2);
                borderParams.height = LayoutParams.MATCH_PARENT;

                border.setBackgroundResource(R.color.dialog_border_left);

                addTo.addView(border, borderParams);

            }

            buttonParams.width = 0;
            buttonParams.weight = 2;
            buttonParams.gravity = Gravity.CENTER;

            addTo.addView(myButton, buttonParams);

            if (i == 1) {

                View border = new View(context);

                borderParams.width = dpToPx(2);
                borderParams.height = LayoutParams.MATCH_PARENT;

                border.setBackgroundResource(R.color.dialog_border_left);
                addTo.addView(border, borderParams);

            }

        }

        public void addDatePicker() {

            LinearLayout.LayoutParams dpParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.message);

            boolean tabletSize = mContext.getResources().getBoolean(R.bool.isTablet);

            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                dpParams.setMargins(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));

            } else if (!tabletSize) {

                dpParams.setMargins(dpToPx(20), dpToPx(-30), dpToPx(20), dpToPx(-30));

            }

            datePicker.setCalendarViewShown(false);

            addTo.addView(datePicker, dpParams);

        }

        public DatePicker getDatePicker() {

            return datePicker;

        }

        public void disableKeyboard() {

            datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        }

        public void addListView() {

            LinearLayout.LayoutParams lvParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.main);
            FrameLayout k = (FrameLayout) view.findViewById(R.id.frame);


            listView.setSelector(R.color.dialog_btn_hover);
            addTo.addView(listView, lvParams);

        }

        public ListView getListView() {

            return listView;

        }

        public void addLoader(String message) {

            dialogMessage.setText(message);
            dialogMessage.setTextAppearance(mContext, R.style.Message);

            ProgressBar progressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyle);

            LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            progressParams.width = 0;
            progressParams.weight = 1;
            progressParams.setMargins(dpToPx(10), dpToPx(20), dpToPx(0), dpToPx(20));

            textParams.width = 0;
            textParams.weight = 3;
            textParams.gravity = Gravity.CENTER;
            textParams.setMargins(dpToPx(0), dpToPx(20), dpToPx(10), dpToPx(20));

            LinearLayout addTo = (LinearLayout) view.findViewById(R.id.message);

            addTo.addView(progressBar, progressParams);
            addTo.addView(dialogMessage, textParams);

        }

        public int dpToPx(int dp) {
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }

    }

}
