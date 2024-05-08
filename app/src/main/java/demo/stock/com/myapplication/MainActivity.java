package demo.stock.com.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity
{
    private EditText et_current_price, et_ratio, et_result;
    private Button bt_copy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        et_current_price = findViewById(R.id.et_current_price);
        et_ratio = findViewById(R.id.et_ratio);
        et_result = findViewById(R.id.et_result);
        bt_copy = findViewById(R.id.copyButton);

        et_current_price.addTextChangedListener(new DecimalMultiplyTextWatcher());
        et_ratio.addTextChangedListener(new DecimalMultiplyTextWatcher());
        et_ratio.addTextChangedListener(new DecimalMultiplyTextWatcher());

        //复制按钮，监听
        bt_copy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String result = et_result.getText().toString();
                copyToClipboard(result);
            }
        });
    }

    private class DecimalMultiplyTextWatcher implements TextWatcher
    {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            // Not used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            // Not used
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            try
            {
                // 现价
                double currentPrice = Double.parseDouble(et_current_price.getText().toString());
                // 涨幅，点数
                double ratio = Double.parseDouble(String.valueOf(1 + Double.parseDouble(et_ratio.getText().toString()) * 0.01));
                // 止盈价（目标价）
                double goalPrice = currentPrice * ratio;
                // 把目标价格转化成后两位数的格式，并且四舍五入
                BigDecimal bg = new BigDecimal(goalPrice);
                bg = bg.setScale(2, RoundingMode.HALF_UP); // 四舍五入到小数点后两位
                String copyResult = String.valueOf(bg.doubleValue());
                // 更新止盈价
                et_result.setText(copyResult);
            } catch (NumberFormatException e)
            {
            }
        }
    }


    // 复制文本到剪贴板的方法
    private void copyToClipboard(String textToCopy)
    {
        // 获取剪贴板管理器
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个ClipData对象
        ClipData clip = ClipData.newPlainText("copied text", textToCopy);
        // 将ClipData对象复制到剪贴板
        clipboard.setPrimaryClip(clip);
        // 显示复制成功的提示信息
        Toast.makeText(this, textToCopy, Toast.LENGTH_SHORT).show();
    }

}
