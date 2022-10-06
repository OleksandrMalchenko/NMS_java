package com.example.nms_pyth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    EditText et1, et2;
    Button Btn;
    TextView tv;
    byte[] test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.edit1);
        et2 = findViewById(R.id.edit2);
        Btn = findViewById(R.id.button);
        tv = findViewById(R.id.result);

        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("nms");


        //final PyObject obj=null;
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   test =  readAsset(MainActivity.this, "filename1824.bin");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PyObject obj = pyobj.callAttr("non_max_suppression",test,0.25,0.45,"",false,false,"",300,0);
//                tv.setText(obj.toString());
            }
        });
    }

    public static byte[] readAsset(Context context, String filename)
            throws IOException {
        InputStream in = context.getResources().getAssets().open(filename);
        try {
            return readAllBytes(in);
        } finally {
            in.close();
        }
    }

    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyAllBytes(in, out);
        return out.toByteArray();
    }

    public static int copyAllBytes(InputStream in, OutputStream out)
            throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        return byteCount;
    }
}