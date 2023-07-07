package com.ldy.ex2.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ldy.ex2.DAO.UserDao;
import com.ldy.ex2.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_userName;
    private SharedPreferences config;
    private SharedPreferences.Editor editor;
    private String userName;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn_addUser = findViewById(R.id.btn_addUser);
        Button btn_login = findViewById(R.id.btn_login);
        edit_userName = findViewById(R.id.edit_userName);
        btn_login.setOnClickListener(this);
        btn_addUser.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        userDao = UserDao.getInstance(this);
//        判断是否获取了使用相机的权限，如果没有则申请该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        config = getSharedPreferences("config", MODE_PRIVATE);
        editor = config.edit();
        userName = config.getString("userName", "default");
        edit_userName.setText(userName);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_addUser) {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("userName", edit_userName.getText().toString());
            startActivity(intent);
        } else if (id == R.id.btn_login) {
            userName = edit_userName.getText().toString();
            if (userName.isEmpty()) {
                Toast.makeText(this, "登录失败，输入为空", Toast.LENGTH_SHORT).show();
            } else if (!checkLogIn(userName)) {
                Toast.makeText(this, "登录失败，用户名不存在", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("userName", userName);
                editor.commit();
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userID", userDao.getID(userName));
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        }
    }

    private boolean checkLogIn(String userName) {
        return userDao.checkExist(userName);
    }
}