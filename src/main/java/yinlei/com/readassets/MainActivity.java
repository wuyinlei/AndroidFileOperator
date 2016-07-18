package yinlei.com.readassets;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_read_asset, btn_read_raw, btn_write, btn_read, btn_write_in, btn_write_out;
    private EditText mText, text_out;
    private TextView show_text, show_text_out;

    private String fileName = "test";
    final File sdcard = Environment.getExternalStorageDirectory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initListener();
    }

    /**
     * 事件监听
     */
    private void initListener() {
        btn_read_asset.setOnClickListener(this);
        btn_read_raw.setOnClickListener(this);
        btn_read.setOnClickListener(this);
        btn_write.setOnClickListener(this);
        //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
        //<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        btn_write_in.setOnClickListener(this);
        btn_write_out.setOnClickListener(this);
    }

    /**
     * 初始化布局控件
     */
    private void initView() {
        show_text = (TextView) findViewById(R.id.show_text);
        btn_read_asset = (Button) findViewById(R.id.btn_read_asset);
        btn_read_raw = (Button) findViewById(R.id.btn_read_raw);
        mText = (EditText) findViewById(R.id.edit_text);
        btn_read = (Button) findViewById(R.id.btn_read);
        btn_write = (Button) findViewById(R.id.btn_write);
        //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
        //<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        show_text_out = (TextView) findViewById(R.id.show_text_out);
        text_out = (EditText) findViewById(R.id.edit_text_out);
        btn_write_in = (Button) findViewById(R.id.btn_write_in);
        btn_write_out = (Button) findViewById(R.id.btn_write_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_read_asset:
                readAssets();
                break;
            case R.id.btn_read_raw:
                readRaw();
                break;
            case R.id.btn_write:
                writeInStorage();
                break;
            case R.id.btn_read:
                readInStorage();
                break;
            case R.id.btn_write_in:
                writeExternal();
                break;
            case R.id.btn_write_out:
                readExternal();
                break;

        }
    }

    /**
     * 读取外部存储
     */
    private void readExternal() {
        File myfile = new File(sdcard, "This is my file.txt");
        if (myfile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(myfile);
                InputStreamReader isr = new InputStreamReader(fis, "utf-8");
                char input[] = new char[fis.available()];
                isr.read(input);
                String str = new String(input);
                show_text_out.setText(str);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 写入到外部存储
     */
    private void writeExternal() {
        File myFile = new File(sdcard, "This is my file.txt");
        if (!sdcard.exists()) {
            Toast.makeText(MainActivity.this, "当前系统不具备SD卡目录", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            myFile.createNewFile();
            Toast.makeText(MainActivity.this, "文件创建完成", Toast.LENGTH_SHORT).show();
            FileOutputStream fos = new FileOutputStream(myFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
            osw.write(text_out.getText().toString());
            osw.flush();
            fos.flush();
            osw.close();
            fos.close();
            Toast.makeText(MainActivity.this, "文件已经写入完成", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入到内部存储
     */
    private void writeInStorage() {
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
            osw.write(mText.getText().toString());
            osw.flush();
            fos.flush();
            osw.close();
            fos.close();
            Toast.makeText(MainActivity.this, "写入完成", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取内部存储
     */
    private void readInStorage() {
        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            char input[] = new char[fis.available()];
            isr.read(input);
            isr.close();
            fis.close();
            String readed = new String(input);
            show_text.setText(readed);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取raw资源文件
     */
    private void readRaw() {
        try {
            InputStream is = getResources().openRawResource(R.raw.raw);
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String str_raw = "";

            while ((str_raw = br.readLine()) != null) {
                Log.d("MainActivity", str_raw);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 去读assets资源文件
     */
    private void readAssets() {
        try {
            InputStream is = getResources().getAssets().open("asset.txt");
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String str_asset = "";
            while ((str_asset = br.readLine()) != null) {
                Log.d("MainActivity", str_asset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
