package com.mut.iotdemotest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tools.ThreadTools;
import com.mut.iotdemotest.utils.EditTextClearTools;

import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;

public class IDActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_userName;
    private EditText et_password;
    private ImageView iv_pwdClear;
    private CheckBox cb_checkbox;
    private Button btn_login;
    private ImageView iv_userClear;
    final String REMEMBER_PWD_PREF = "rememberPwd";
    final String ACCOUNT_PREF = "account";
    final String PASSWORD_PREF = "password";
    private String userName;
    private String password;
    private SharedPreferences preference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
        initView();
        remember();
    }

    private void remember() {
        //从 SharedPreferences 中获取【是否记住密码】参数
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = preference.getBoolean(REMEMBER_PWD_PREF, false);


        if (isRemember) {//设置【账号】与【密码】到文本框，并勾选【记住密码】
            et_userName.setText(preference.getString(ACCOUNT_PREF, ""));
            et_password.setText(preference.getString(PASSWORD_PREF, ""));
            cb_checkbox.setChecked(true);
        }


    }

    private void initView() {
        et_userName = (EditText) findViewById(R.id.et_userName);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_pwdClear = (ImageView) findViewById(R.id.iv_pwdClear);
        iv_userClear = (ImageView) findViewById(R.id.iv_unameClear);
        cb_checkbox = (CheckBox) findViewById(R.id.cb_checkbox);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);


        EditTextClearTools.addClearListener(et_userName, iv_userClear);
        EditTextClearTools.addClearListener(et_password, iv_pwdClear);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (checkId() != 0) {
                    SharedPreferences.Editor editor = preference.edit();
                    if (cb_checkbox.isChecked()) {//记住账号与密码
                        editor.putBoolean(REMEMBER_PWD_PREF, true);
                        editor.putString(ACCOUNT_PREF, userName);
                        editor.putString(PASSWORD_PREF, password);
                    } else {//清空数据
                        editor.clear();
                    }
                    editor.apply();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("id", checkId());
                    startActivity(intent);
                    finish();
                } else {
                    showToast_error("请输入正确账号密码");
                    // Toast.makeText(this, "请输入正确账号密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Integer checkId() {
        userName = et_userName.getText().toString().trim();
        password = et_password.getText().toString().trim();
        if (userName.equals("admin1") && password.equals("123")) {
            return 1;
        }
        if (userName.equals("admin2") && password.equals("123")) {
            return 2;
        }
        if (userName.equals("admin3") && password.equals("123")) {
            return 3;
        }
        if (userName.equals("xulaoshi") && password.equals("123")) {
            return 4;
        }
        if (userName.equals("wangxiang") && password.equals("123")) {
            return 5;
        } else
            return 0;


    }

    private void submit() {
        // validate
        String userName = et_userName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "  用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, " 密码", Toast.LENGTH_SHORT).show();
            return;
        }



    }

    public void showToast_error(final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();
                Toasty.error(getBaseContext(), message, Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
