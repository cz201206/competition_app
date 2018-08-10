package site.lool.android.competition.activity.level3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import site.lool.android.competition.R;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");

        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ((ImageView)findViewById(R.id.image_activity)).setImageBitmap(bm);
    }

    //region 响应区

    public void upload_ok(View view){

    }

    public void upload_cancel(View view){
        this.finish();
    }

    //endregion
}
