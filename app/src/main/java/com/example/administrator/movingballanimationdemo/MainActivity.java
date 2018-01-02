package com.example.administrator.movingballanimationdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.movingballanimationdemo.custom.CustomDynamicImageView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import at.wirecube.additiveanimations.helper.EaseInOutPathInterpolator;

public class MainActivity extends AppCompatActivity {

    Context context;
    CustomDynamicImageView ballCustomDynamicImageView;                                              // 球
    RelativeLayout rootViewRelativeLayout;                                                          // 背景
    LinearLayout discLinearLayout;                                                                  // 圓盤
    double computableX;                                                                             // 運算過後的x
    double computableY;                                                                             // 運算過後的y
    int rootViewRelativeLayoutWidth;
    int rootViewRelativeLayoutHeight;
    int discLinearLayoutWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        rootViewRelativeLayout = findViewById(R.id.rootViewRelativeLayout);
        ballCustomDynamicImageView = findViewById(R.id.ballCustomDynamicImageView);
        discLinearLayout = findViewById(R.id.discLinearLayout);

        /** 了解背景的實際 寬度與高度, 取得圓盤實際寬度 */
        new Handler().postDelayed(new Runnable() {
            public void run() {
                rootViewRelativeLayoutWidth = rootViewRelativeLayout.getWidth();
                rootViewRelativeLayoutHeight = rootViewRelativeLayout.getHeight();
                discLinearLayoutWidth = discLinearLayout.getWidth();

                new StyleableToast
                        .Builder(context)
                        .text("原圓盤中心點座標: (" + rootViewRelativeLayoutWidth / 2 + ", " + rootViewRelativeLayoutHeight / 2 + ")")
                        .textColor(Color.WHITE)
                        .backgroundColor(0xff00743f)                                                 // 關於顏色的轉換, #00743f → 0xff00743f, 0x: 代表颜色整数的标记, ff: 代表透明度, 00743f：表示颜色
                        .show();
            }
        }, 500);

        /** 點擊背景 想做些什麼 */
        rootViewRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                /** 判斷事件類型, 並做出希望的行為 */
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {                        // MotionEvent.ACTION_DOWN: 第一个手指 初次接触到屏幕时触发

                    /**  將手指點擊的座標 與原中心點座標運算 */
                    if (motionEvent.getX() < rootViewRelativeLayoutWidth / 2) {
                        computableX = rootViewRelativeLayoutWidth / 2 - motionEvent.getX();
                    } else {
                        computableX = motionEvent.getX() - rootViewRelativeLayoutWidth / 2;
                    }
                    if (motionEvent.getY() < rootViewRelativeLayoutHeight / 2) {
                        computableY = rootViewRelativeLayoutHeight / 2 - motionEvent.getY();
                    } else {
                        computableY = motionEvent.getY() - rootViewRelativeLayoutHeight / 2;
                    }

                    /** 透過畢氏定理, 取得手指點擊座標 到中心點的直線距離, 並進行判斷 距離是否小於圓的半徑 */
                    double theDistanceBetweenTheTwo = Math.sqrt(computableX * computableX + computableY * computableY);
                    if (theDistanceBetweenTheTwo <= discLinearLayoutWidth / 2) {

                        /** 以球的中心點為基準, 滑行到 手指點擊的座標, 並顯示自定義中心點 相對的座標 */
                        ballCustomDynamicImageView.animate().setInterpolator(EaseInOutPathInterpolator.create())
                                .setDuration(800)
                                .x(motionEvent.getX() - ballCustomDynamicImageView.getWidth() / 2)
                                .y(motionEvent.getY() - ballCustomDynamicImageView.getHeight() / 2)
                                .start();

                        new StyleableToast
                                .Builder(context)
                                .text("相對座標: (" + (int) (motionEvent.getX() - rootViewRelativeLayoutWidth / 2) + ", " + (int) (rootViewRelativeLayoutHeight / 2 - motionEvent.getY()) + ")")
                                .textColor(Color.WHITE)
                                .backgroundColor(0xff00743f)                                         // 關於顏色的轉換, #00743f → 0xff00743f, 0x: 代表颜色整数的标记, ff: 代表透明度, 00743f：表示颜色
                                .show();
                    }
                }

                return false;
            }
        });
    }
}