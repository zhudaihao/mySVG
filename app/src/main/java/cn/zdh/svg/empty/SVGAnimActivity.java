package cn.zdh.svg.empty;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import cn.zdh.svg.R;


/**
 * 矢量动画
 */
public class SVGAnimActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_anim);
        imageView = findViewById(R.id.imageView);

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void load(View view) {
        //获取矢量动画对象
        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) imageView.getDrawable();
        //启动动画
        drawable.start();
    }
}
