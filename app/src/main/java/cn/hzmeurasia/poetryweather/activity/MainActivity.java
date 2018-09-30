package cn.hzmeurasia.poetryweather.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.adapter.CardAdapter;
import cn.hzmeurasia.poetryweather.db.CityDb;
import cn.hzmeurasia.poetryweather.entity.CardEntity;
import cn.hzmeurasia.poetryweather.entity.LocationEvent;
import cn.hzmeurasia.poetryweather.entity.SearchCityEntity;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String privinceName;
    private String cityName;
    private String districtName;


    /**
     * 声明AMapLocationClient类对象
     */
    public AMapLocationClient mLocationClient = null;
    /**
     * 声明定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析aMapLocation获取相应内容。
                    Log.d(TAG, "onLocationChanged: "+aMapLocation.getCity());
                    Log.d(TAG, "onLocationChanged: "+aMapLocation.getDistrict());
                    privinceName = aMapLocation.getProvince();
                    cityName = aMapLocation.getCity();
                    districtName = aMapLocation.getDistrict();
                    mLocationClient.stopLocation();
                    //销毁定位客户端，同时销毁本地定位服务。
                    mLocationClient.onDestroy();
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
    /**
     * 声明AMapLocationClientOption对象,用来设置发起定位的模式和相关参数
     */
    public AMapLocationClientOption mLocationOption = null;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView tvTip;

    private List<CityDb> cityDbList = new ArrayList<>();
    private CardAdapter cardAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //控件初始化
        tvTip = findViewById(R.id.tv_tip);
        //注册和风天气
        HeConfig.init("HE1808181021011344","c6a58c3230694b64b78facdebd7720fb");
        HeConfig.switchToFreeServerNode();
        //主页城市列表本地数据库创建
        LitePal.getDatabase();
        //启用toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //注册EventBus事件
        EventBus.getDefault().register(this);

        //注册高德地图定位组件
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //启动定位
        mLocationClient.startLocation();
        //停止定位
//        mLocationClient.stopLocation();

        //载入左滑提示按钮
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //显示出按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            //载入按钮图片
            actionBar.setHomeAsUpIndicator(R.drawable.ic_person1);
        }
        //悬浮按钮点击事件
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SearchCityActivity.class);
                startActivity(intent);
            }
        });

        //判断是否添加了城市
        isCardEmpty();
        //设置RecyclerView
        SwipeMenuRecyclerView recyclerView = findViewById(R.id.rv_main);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        //设置侧滑菜单
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MainActivity.this)
                        .setBackgroundColor(getColor(R.color.bluishWhite))
                        .setText("删除")
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setWidth(200);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        });
        //设置侧滑菜单的点击事件
        recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int menuPositon = menuBridge.getAdapterPosition();
                cityDbList.remove(menuPositon);
                cardAdapter.notifyItemRemoved(menuPositon);

            }
        });
        //拖拽排序
        recyclerView.setLongPressDragEnabled(true);
        //拖拽监听
        recyclerView.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                int fromPosition = srcHolder.getAdapterPosition();
                int toPosition = targetHolder.getAdapterPosition();
                //item被拖拽时,交换数据,并更新adapter
                Collections.swap(cityDbList, fromPosition, toPosition);
                cardAdapter.notifyItemMoved(fromPosition,toPosition);
                return true;
            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {

            }
        });
        //查询数据库
        cityDbList = LitePal.findAll(CityDb.class);
        cardAdapter = new CardAdapter(cityDbList);
        recyclerView.setAdapter(cardAdapter);
    }



    /**
     * 判断是否添加了城市
     */
    private void isCardEmpty() {
        Log.d(TAG, "isCardEmpty: "+LitePal.count(CityDb.class));
        if (LitePal.count(CityDb.class) == 0) {
            tvTip.setVisibility(View.VISIBLE);
        } else {
            tvTip.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * EventBus事件处理
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void searchCityEvent(final SearchCityEntity searchCityEntity) {
        Log.d(TAG, "Event: "+searchCityEntity.getCityCode());
        HeWeather.getWeatherNow(this, searchCityEntity.getCityCode(), new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ",throwable);
                Toast.makeText(MainActivity.this,"天气获取失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<Now> list) {
                Log.i(TAG, "onSuccess: "+ new Gson().toJson(list));
                if (LitePal.where("cityDb_cid = ?", searchCityEntity.getCityCode())
                        .count(CityDb.class) > 0) {
                    Toast.makeText(MainActivity.this,"您已添加过该城市",Toast.LENGTH_SHORT).show();
                }else {
                    for (Now now : list) {
                        CityDb cityDb = new CityDb();
                        cityDb.setCityDb_cid(now.getBasic().getCid());
                        cityDb.setCityDb_cityName(now.getBasic().getLocation());
                        cityDb.setCityDb_txt(now.getNow().getCond_txt());
                        cityDb.setCityDb_temperature(now.getNow().getFl());
                        cityDb.setCityDb_imageId(R.drawable.bg);
                        cityDb.save();
                        cityDbList.add(new CityDb(now.getBasic().getCid(), now.getBasic().getLocation(),
                                now.getNow().getCond_txt(), now.getNow().getFl(), R.drawable.bg));
                    }
                    //刷新Card视图
                    cardAdapter.notifyDataSetChanged();
                    isCardEmpty();
                }

            }
        });
    }

    /**
     * 同步list中数据到本地数据库
     */
    private void updateDatabases() {
        LitePal.deleteAll(CityDb.class);
        for (CityDb cityDb : cityDbList) {
            Log.d(TAG, "updateDatabases: "+cityDbList.size());
            CityDb updateCityDb = new CityDb();
            updateCityDb.setCityDb_cid(cityDb.getCityDb_cid());
            updateCityDb.setCityDb_cityName(cityDb.getCityDb_cityName());
            updateCityDb.setCityDb_txt(cityDb.getCityDb_txt());
            updateCityDb.setCityDb_temperature(cityDb.getCityDb_temperature());
            updateCityDb.setCityDb_imageId(cityDb.getCityDb_imageId());
            updateCityDb.save();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //更新数据
        updateDatabases();
        //解除注册EventBus
        EventBus.getDefault().unregister(this);
    }


}
