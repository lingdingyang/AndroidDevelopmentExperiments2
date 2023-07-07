package com.ldy.ex2.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ldy.ex2.DAO.UserDao;
import com.ldy.ex2.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences.Editor editor;
    private EditText edit_newUserName;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        String userName = getIntent().getStringExtra("userName");
        Button btn_back = findViewById(R.id.btn_back);
        Button btn_reg = findViewById(R.id.btn_reg);
        edit_newUserName = findViewById(R.id.edit_newUserName);
        edit_newUserName.setText(userName);
        btn_back.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        editor = config.edit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userDao = UserDao.getInstance(this);
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) {
            finish();
        } else if (id == R.id.btn_reg) {
            String newUserName = edit_newUserName.getText().toString();
            if (newUserName.isEmpty()) {
                Toast.makeText(this, "注册失败，用户名为空", Toast.LENGTH_SHORT).show();
            } else if (!checkUserName(newUserName)) {
                Toast.makeText(this, "注册失败，用户名存在", Toast.LENGTH_SHORT).show();
            } else {
                userDao.insert(newUserName);
                editor.putString("userName", newUserName);
                editor.commit();
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean checkUserName(String newUserName) {
        return !userDao.checkExist(newUserName);
    }
}