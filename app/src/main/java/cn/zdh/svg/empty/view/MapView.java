package cn.zdh.svg.empty.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cn.zdh.svg.R;

/**
 * 自定义view实现
 * 加载svg后缀的文件
 */
public class MapView extends View {
    //上下文
    private Context context;
    //画笔
    private Paint paint;
    //所有省份的集合
    private List<ProvinceItem> list;
    //绘制地图的颜色
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFF00FFFF};
    //适配比例
    private float scale = 1.0f;

    //保存图片信息
    private RectF pathRectF;

    //被选中的省  通过省对象是否为空判断是否被选中
    private ProvinceItem select = null;

    public MapView(Context context) {
        super(context);
        init(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);


        //启动线程 解析xml
        loadThread.start();

    }


    /**
     * 线程 解析xml
     */
    private Thread loadThread = new Thread() {
        @Override
        public void run() {
            //定义一个输入流接收 svg资源
            InputStream inputStream = context.getResources().openRawResource(R.raw.china);
            List<ProvinceItem> itemList = new ArrayList<>();
            //图像矩形坐标
            float left = -1;
            float top = -1;
            float right = -1;
            float bottom = -1;

            try {
                //获取DocumentBuilderFactory实例  解析（Java的类）
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

                //获取DocumentBuilder对象
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                //解析输入流
                Document document = documentBuilder.parse(inputStream);

                //获取xml文件的根目录
                Element rootElement = document.getDocumentElement();
                //获取根节点下面的某个节点 (节点名称)-->返回path节点集合
                NodeList pathList = rootElement.getElementsByTagName("path");

                //遍历所有path节点
                for (int i = 0; i < pathList.getLength(); i++) {
                    //获取每一个path的Element节点
                    Element element = (Element) pathList.item(i);
                    //获取path节点中的android：pathData 属性值
                    String pathData = element.getAttribute("android:pathData");
                    //解析pathData转换成path
                    Path path = PathParser.createPathFromPathData(pathData);

                    //把path添加到封装类
                    ProvinceItem provinceItem = new ProvinceItem(path);
                    //添加到集合
                    itemList.add(provinceItem);


                    //获取控件的宽高
                    RectF rect = new RectF();
                    //获取到每个省份的边界
                    path.computeBounds(rect, true);
                    //遍历取出每个path中的left取所有的最小值
                    left = left == -1 ? rect.left : Math.min(left, rect.left);
                    //遍历取出每个path中的right取所有的最大值
                    right = right == -1 ? rect.right : Math.max(right, rect.right);
                    //遍历取出每个path中的top取所有的最小值
                    top = top == -1 ? rect.top : Math.min(top, rect.top);
                    //遍历取出每个path中的bottom取所有的最大值
                    bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);


                }

                //保存图片矩形信息
                pathRectF = new RectF(left, top, right, bottom);

                list = itemList;

                //通知重绘
                handler.sendEmptyMessage(1);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };

    /**
     * 设置每个省颜色
     * 通知重新测量 绘制
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //判断集合是否为空
            if (list == null) {
                return;
            }

            int size = list.size();
            //遍历集合
            for (int i = 0; i < size; i++) {
                int color = Color.WHITE;
                int flag = i % 4;
                switch (flag) {
                    case 1:
                        color = colorArray[0];
                        break;
                    case 2:
                        color = colorArray[1];
                        break;
                    case 3:
                        color = colorArray[2];
                        break;
                    default:
                        color = colorArray[3];
                        break;

                }

                //设置每个省的颜色
                list.get(i).setDrawColor(color);

            }
            //重绘
            //回调onMeasure 和onLayout 方法
            requestLayout();
            //在主线程调用这个方法 回调onDraw
            invalidate();


        }
    };


    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);


        //计缩放比例
        if (pathRectF != null) {
            double mapWidth = pathRectF.width();
            scale = (float) (w / mapWidth);
        }

        //保存测量信息 如果没有调用super方法必须调用这个方法
//        setMeasuredDimension(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));


    }


    /**
     * 绘制路径
     *
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //判断list是否为空
        if (list != null && list.size() > 0) {
            //保存画布
            canvas.save();
            //设置画布缩放比例（目的适配屏幕）
            canvas.scale(scale, scale);
            //遍历集合
            for (ProvinceItem provinceItem : list) {

                //判断省是否被 点击
                if (select != provinceItem) {
                    //调用绘制方法
                    provinceItem.drawItem(canvas, paint, false);
                }
            }

            //判断省是否被 点击
            if (select != null) {
                select.drawItem(canvas, paint, true);
            }

        }


    }


    /**
     * 处理点击事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //getX是控件左上角为坐标原点
        //getRawX是以屏幕左上角为坐标原点
        handlerTouch(event.getX(), event.getY());

        Log.e("zdh", "----------getX" + getX() + "---------------getRowX" + event.getRawX());
        return super.onTouchEvent(event);

    }

    /**
     * 用于点击事件处理
     *
     * @param x 点击X坐标
     * @param y 点击y坐标
     */
    private void handlerTouch(float x, float y) {
        //如果集合为空 或者没有数据 不处理触摸事件
        if (list == null || list.size() == 0) {
            return;
        }

        //定义一个空的 被选中的省份
        ProvinceItem selectItem = null;

        //遍历集合
        for (ProvinceItem provinceItem : list) {
            //注意 X Y需要等比缩放
            if (provinceItem.isTouch(x / scale, y / scale)) {
                //赋值
                selectItem = provinceItem;
            }
        }

        //给省对象赋值 (我点击一次就要改变UI 所有就在for循环外判断赋值)
        if (selectItem != null) {
            select = selectItem;
            invalidate();
        }


    }
}
