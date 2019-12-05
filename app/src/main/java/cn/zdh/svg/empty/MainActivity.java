package cn.zdh.svg.empty;

import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androipathview.PathView;

import cn.zdh.svg.R;


/**
 * 第三方框架
 */
public class MainActivity extends AppCompatActivity {

    private PathView pathView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        pathView = findViewById(R.id.pathView);

        //设置动画
        setPathView();
    }

    private void setPathView() {
        pathView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathView.getPathAnimator().
                        //pathView.getSequentialPathAnimator().
                        //延迟动画开始时间
                                delay(100).
                        //动画执行时间
                                duration(5000).
                        //插入器
                                interpolator(new AccelerateDecelerateInterpolator()).
                        listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
                            @Override
                            public void onAnimationEnd() {
                                //动画结束监听
//                pathView.getPathAnimator().
//                        delay(100).
//                        duration(5000).start();
                            }
                        }).listenerStart(new PathView.AnimatorBuilder.ListenerStart() {
                    @Override
                    public void onAnimationStart() {
                        //动画开始监听

                    }
                }).start();
            }
        });


        //Java代码设置path属性
//        final Path path = makeConvexArrow(50, 100);
//        pathView.setPath(path);
//        pathView.setFillAfter(true);
//        pathView.useNaturalColors();


    }

    //java代码 设置动画轨迹
    private Path makeConvexArrow(float length, float height) {
        final Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo(length / 4f, 0.0f);
        path.lineTo(length, height / 2.0f);
        path.lineTo(length / 4f, height);
        path.lineTo(0.0f, height);
        path.lineTo(length * 3f / 4f, height / 2f);
        path.lineTo(0.0f, 0.0f);
        path.close();
        return path;
    }


}
