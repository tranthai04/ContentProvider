package com.example.btcontent;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.btcontent.R;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;
    private TextView tvMessages;
    private Button btnShowMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessages = findViewById(R.id.tvMessages);
        btnShowMessages = findViewById(R.id.btnShowMessages);
        String header = "Tên: Trần Gia Thái - MSV : 22115141122120";
        ((TextView) findViewById(R.id.tvHeader)).setText(header);

        // Kiểm tra quyền truy cập SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
        }

        // Thiết lập sự kiện cho nút
        btnShowMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    readSMS();
                } else {
                    Toast.makeText(MainActivity.this, "Quyền truy cập SMS bị từ chối", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void readSMS() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms"), null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder smsBuilder = new StringBuilder();
            do {
                String smsBody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                smsBuilder.append(smsBody).append("\n\n");
            } while (cursor.moveToNext());
            tvMessages.setText(smsBuilder.toString());
            cursor.close();
        } else {
            tvMessages.setText("Không có tin nhắn.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu quyền được cấp, bạn có thể đọc SMS ngay lập tức
            } else {
                Toast.makeText(this, "Quyền truy cập SMS bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
