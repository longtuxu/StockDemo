package demo.stock.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;


public class MainActivity extends Activity implements View.OnClickListener {
    private EditText et_current_price, et_ratio, et_result, et_target_price;
    private Button bt_clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        et_current_price = findViewById(R.id.et_current_price);
        et_ratio = findViewById(R.id.et_ratio);
        et_result = findViewById(R.id.et_result);
        bt_clean = findViewById(R.id.cleanButton);
        et_target_price = findViewById(R.id.et_target_price);

        bt_clean.setOnClickListener(this);


        // 为et_current_price设置TextWatcher
        et_current_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateAndUpdateResult();
            }
        });

        // 为et_ratio设置TextWatcher
        et_ratio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateAndUpdateResult();
            }
        });

        // 为et_current_price设置TextWatcher
        et_current_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateRatio();
            }
        });

        // 为et_target_price设置TextWatcher
        et_target_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateRatio();
            }
        });
    }

    private void calculateRatio() {
        try {
            // 获取现价和目标价
            double currentPrice = Double.parseDouble(et_current_price.getText().toString());
            double targetPrice = Double.parseDouble(et_target_price.getText().toString());

            // 防止除数为零的情况
            if (currentPrice == 0) {
                et_ratio.setText(""); // 或者设置一个提示信息
                return;
            }

            // 计算涨跌幅，结果为小数形式
            double ratio = (targetPrice - currentPrice) / currentPrice;

            // 设置et_ratio的文本，转换为百分比形式
            et_ratio.setText(String.format(Locale.getDefault(), "%.2f%%", ratio*100));
        } catch (NumberFormatException e) {
            // 如果解析失败，清除结果显示或做其他错误处理
            et_ratio.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        et_ratio.setText("");
        et_result.setText("");
        et_target_price.setText("");
    }


    private void calculateAndUpdateResult() {
        try {
            // 获取现价和涨跌幅
            double currentPrice = Double.parseDouble(et_current_price.getText().toString());
            double ratio = Double.parseDouble(et_ratio.getText().toString()) * 0.01;

            // 根据涨跌幅计算结果价
            double resultPrice;
            if (ratio >= 0) {
                // 涨幅
                resultPrice = currentPrice * (1 + ratio);
            } else {
                // 跌幅
                resultPrice = currentPrice * (1 - Math.abs(ratio));
            }

            // 设置结果价，保留两位小数
            et_result.setText(String.format(Locale.getDefault(), "%.2f", resultPrice));
        } catch (NumberFormatException e) {
            // 如果解析失败，清除结果显示或做其他错误处理
            et_result.setText("");
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 当用户触碰屏幕任何位置时调用此方法
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                // 如果当前焦点在EditText上，则隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
