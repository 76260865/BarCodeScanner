package barcode.jason.com.barcodescanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;


public class MainActivity extends ActionBarActivity {

    private TextView txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtContent = (TextView) findViewById(R.id.txt_content);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureLayout(R.layout.custom_capture_layout);
        integrator.setLegacyCaptureLayout(R.layout.custom_legacy_capture_layout);
        integrator.initiateScan();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = createImage("sadasdasdasdasd");
        imageView.setImageBitmap(bitmap);
    }

    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;

    // 生成QR图
    private Bitmap createImage(String text) {
        try {

            if (TextUtils.isEmpty(text)) {
                return null;
            }
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                txtContent.setText(result.getContents());
            }
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
