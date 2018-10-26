package cn.hzmeurasia.poetryweather.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.activity.WeatherActivity;

/**
 * 类名: ImageUtil<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/25 12:55
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() ;
    public static String pathfile = FILE_SAVEPATH + "/ScreenPoetryWeather.png";


    public static void getBitmapByView(Context mContext, final FrameLayout frameLayout) {
        //创建屏幕大小的Bitmap
        Bitmap bitmap = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        frameLayout.draw(canvas);
        //获取头页面图片
        Bitmap head = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.screen_head);
        Bitmap all = toConformBitmap(head, bitmap);
        File savedir = new File(FILE_SAVEPATH);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(pathfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "getBitmapByView: 保存失败 ");
        }
        try {
            if (null != out) {
                all.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
            Log.d(TAG, "getBitmapByView: 保存成功");
        } catch (IOException e) {
            Log.d(TAG, "getBitmapByView: 保存失败");
        }
    }

    /**
     * 合并图片
     * @param head
     * @param bitmap
     * @return
     */
    private static Bitmap toConformBitmap(Bitmap head, Bitmap bitmap) {
        if (head == null) {
            return null;
        }

        int headWidth = head.getWidth();
        int middleWidth = bitmap.getWidth();

        int headHeight = head.getHeight();
        int middleHeight = bitmap.getHeight();

        //生成新Bitmap
        Bitmap newBitmap = Bitmap.createBitmap(middleWidth, headHeight + middleHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        // 在 0，0坐标开始画入headBitmap
        cv.drawBitmap(head, 0, 0, null);

        //因为手机不同图片的大小的可能小了 就绘制白色的界面填充剩下的界面
        if (headWidth < middleWidth) {
            Bitmap ne = Bitmap.createBitmap(middleWidth - headWidth, headHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(ne);
            canvas.drawColor(Color.WHITE);
            cv.drawBitmap(ne, headWidth, 0, null);
        }
        // 在 0，headHeight坐标开始填充课表的Bitmap
        cv.drawBitmap(bitmap, 0, headHeight, null);
//        在 0，headHeight + kebiaoheight坐标开始填充课表的Bitmap
//        cv.drawBitmap(san, 0, headHeight + kebiaoheight, null);
        // 保存
        cv.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        cv.restore();
        //回收
        head.recycle();
        bitmap.recycle();
        return newBitmap;
    }


}
