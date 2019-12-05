package cn.zdh.svg.empty.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * path 绘制封装类
 */
public class ProvinceItem {
    //路径
    private Path path;
    //绘制的颜色
    private int drawColor;

    public ProvinceItem(Path path) {
        this.path = path;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }


    /**
     * 绘制path
     *
     * @param canvas   画布
     * @param paint    画笔
     * @param isSelect 是否点击
     */
    public void drawItem(Canvas canvas, Paint paint, boolean isSelect) {

        if (isSelect){
            //画省的边界

            //清除阴影
            paint.clearShadowLayer();
            //设置画笔宽
            paint.setStrokeWidth(1);
            //设置画笔样式
            paint.setStyle(Paint.Style.FILL);
            //设置画笔颜色
            paint.setColor(drawColor);
            //绘制
            canvas.drawPath(path, paint);


            //设置画笔颜色
            paint.setColor(Color.BLACK);
            //设置画笔样式
            paint.setStyle(Paint.Style.STROKE);
            //设置阴影
            paint.setShadowLayer(8, 0, 0, 0xffffff);
            //绘制path
            canvas.drawPath(path, paint);


        }else {
            //画省的边界

            //设置画笔宽
            paint.setStrokeWidth(2);
            //设置画笔颜色
            paint.setColor(Color.BLACK);
            //设置画笔样式
            paint.setStyle(Paint.Style.FILL);
            /**
             * radius 模糊半径 值越大越模糊
             * dx 阴影的横向偏移距离 正数向右偏移  负数向左偏移
             * dy 阴影的纵向偏移距离 正数向下偏移  负数向上偏移
             * color 阴影颜色
             *
             */
            paint.setShadowLayer(8, 0, 0, 0xffffff);
            //绘制path
            canvas.drawPath(path, paint);


            //画省的填充

            //清除阴影
            paint.clearShadowLayer();
            //设置颜色
            paint.setColor(getDrawColor());
            //设置样式
            paint.setStyle(Paint.Style.FILL);
            //设置画笔宽
            paint.setStrokeWidth(2);
            //绘制path
            canvas.drawPath(path, paint);
        }



    }


    /**
     * 判断点击 坐标是否在地图上面
     */
    public boolean isTouch(float x, float y) {
        //创建一个矩形
        RectF rectF = new RectF();
        //获取得到当前省份的矩形边界
        path.computeBounds(rectF, true);

        //创建一个区域对象
        Region region = new Region();
        //将path对象放到region区域对象中(path对象, region对象--》他需要左上右下信息)
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));

        //判断坐标是否在地图的区域里面
        return region.contains((int) x,(int) y);


    }


}
