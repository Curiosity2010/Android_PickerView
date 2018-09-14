package com.android_final_demo_proj.view.Picker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android_final_demo_proj.R;

public class PickerViewActivity extends Activity {


        private String[] cities=new String[]{"郑州","洛阳","许昌","开封","新乡","南阳","信阳","平顶山","驻马店","周口","三门峡","鹤壁"};
//    private String[] cities=new String[]{"男","女"};
//    private String[] cities=new String[]{"春","夏","秋","冬"};
//    private String[] cities = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    //    private String[] cities=new String[]{};
//    private String[] cities=new String[]{"167cm","168cm","169cm","170cm","171cm","172cm","173cm","174cm","175cm","176cm"};
    PickerViewV1_2 pickerViewV;

    Button getCurrentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickerview_activity);

        pickerViewV = findViewById(R.id.pickerView);
        PickerViewV1_2.PickerViewAdapter adapter = new PickerViewV1_2.PickerViewAdapter(cities);
        pickerViewV.setPickerViewAdapter(adapter);


        getCurrentItem = findViewById(R.id.getCurrentItem);
        getCurrentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = pickerViewV.getSelectItem();
                Toast.makeText(PickerViewActivity.this, item, Toast.LENGTH_LONG).show();
            }
        });
    }
}
