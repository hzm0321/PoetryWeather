package cn.hzmeurasia.poetryweather.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemStateChangedListener;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocatedCity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.adapter.CardAdapter;
import cn.hzmeurasia.poetryweather.db.CityDb;
import cn.hzmeurasia.poetryweather.entity.SearchCityEvent;
import cn.hzmeurasia.poetryweather.service.MyService;
import de.hdodenhof.circleimageview.CircleImageView;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static interfaces.heweather.com.interfacesmodule.bean.Lang.CHINESE_SIMPLIFIED;


public class MainActivity extends AppCompatActivity {

    static {
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉刷新";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "众里寻她千百度";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "蓦然回首";
        ClassicsHeader.REFRESH_HEADER_FINISH = "那人却在灯火阑珊处";
    }

    private static final String TAG = "MainActivity";

    private String provinceName;
    private String cityCode;
    private String districtName;
    private String personName;
    private boolean isFirst;
    TextView tvName;
    QMUITipDialog tipDialog;
    CircleImageView navCircleImageView;
    public static final int UPDATE_TEXT = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch(message.what) {
                case UPDATE_TEXT:
                    showMessagePositiveDialog();
                    isFirst();
                    break;
                default:
                    break;
            }
        }
    };


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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            districtName = aMapLocation.getDistrict();
                            if (districtName.substring(districtName.length()-1).equals("区")) {
                                districtName = districtName.substring(0, districtName.length() - 1);
                                Log.d(TAG, "onSuccess: 修改后区县位置"+districtName);
                            }
                            Log.d(TAG, "onLocationChanged: "+districtName);
                            HeWeather.getSearch(MainActivity.this, districtName, "cn", 1, CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {
                                @Override
                                public void onError(Throwable throwable) {
                                    Log.i(TAG, "onError: ",throwable);
                                    Toast.makeText(MainActivity.this,"城市搜索失败",Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onSuccess(Search search) {
                                    //可在其中解析aMapLocation获取相应内容。
                                    Log.d(TAG, "onLocationChanged: "+aMapLocation.getProvince());
                                    Log.d(TAG, "onLocationChanged: "+aMapLocation.getDistrict());
                                    provinceName = aMapLocation.getProvince();

                                    for (Basic basic : search.getBasic()) {
                                        String cid = basic.getCid();
                                        cityCode = cid.substring(2);
                                        Log.d(TAG, "onSuccess: cityCode"+cityCode);
                                    }
                                    if (!getFirst()) {
                                        Message message = new Message();
                                        message.what = UPDATE_TEXT;
                                        handler.sendMessage(message);
                                    }
                                }
                            });
                        }
                    }).start();
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

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rv_main)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.fab_add)
    FloatingActionButton fab;

    List<CityDb> cityDbList = new ArrayList<>();
    private CardAdapter cardAdapter;
    @BindView(R.id.nv_left)
    NavigationView navigationView;
    @BindView(R.id.rf_main)
    RefreshLayout mPullRefreshLayout;
    @BindView(R.id.header)
    ClassicsHeader header;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        //沉浸式状态栏
        Window window = MainActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //开启服务
        Intent intent = new Intent(this, MyService.class);
        startService(intent);

        //刷新左侧导航栏姓名
        showName();

        //主页城市列表本地数据库创建
        LitePal.getDatabase();

        //启用toolbar
        setSupportActionBar(toolbar);

        //注册EventBus事件
        EventBus.getDefault().register(this);

        //注册高德地图定位组件
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        checkLocationPower();

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
        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            //显示出按钮
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            //载入按钮图片
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_person1);
//        }
        //悬浮按钮点击事件
        fab.setOnClickListener(view -> {
//            Intent intent1 = new Intent(MainActivity.this, SearchCityActivity.class);
//            Log.d(TAG, "定位区县"+districtName);
//            startActivity(intent1);
            showSearchCity();
        });

        //设置RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        //设置侧滑菜单
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                SwipeMenuItem deleteItem = new SwipeMenuItem(MainActivity.this)
                        .setBackgroundColor(getResources().getColor(R.color.qmui_config_color_red))
                        .setText("删除")
                        .setTextColorResource(R.color.qmui_config_color_white)
                        .setTextSize(18)
                        .setHeight(height)
                        .setWidth(220);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        });
        //设置侧滑菜单的点击事件
        recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                final boolean[] isCancle = {false};
                menuBridge.closeMenu();
                Handler handler = new Handler();
                Snackbar.make(recyclerView,"正在删除本地该城市天气数据",Snackbar.LENGTH_SHORT)
                        .setAction("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isCancle[0] = true;
                                Toast.makeText(MainActivity.this,"已取消删除",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isCancle[0]) {
                            int menuPosition = menuBridge.getAdapterPosition();
                            cityDbList.remove(menuPosition);
                            cardAdapter.notifyItemRemoved(menuPosition);
                            isCardEmpty();
                            updateDatabases();
                        }
                    }
                },1800);

            }
        });
        //拖拽排序
        recyclerView.setLongPressDragEnabled(true);
        recyclerView.setOnItemStateChangedListener(new OnItemStateChangedListener() {
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                mPullRefreshLayout.setEnableRefresh(true);
            }
        });
        //拖拽监听
        recyclerView.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                mPullRefreshLayout.setEnableRefresh(false);
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

        //---------------------设置左侧导航及其事件----------------------------
        Resources resource = getBaseContext().getResources();
        //设置列表字体颜色
        ColorStateList csl = resource.getColorStateList(R.color.navigation_menu_item_color);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(null);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.nav_home:
                    drawerLayout.closeDrawers();
                    Intent intentHome = new Intent(MyApplication.getContext(), MainActivity.class);
                    //栈顶调用
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentHome);
                    break;
                case R.id.nav_personalInformation:
                    drawerLayout.closeDrawers();
                    Intent intentPerson = new Intent(MyApplication.getContext(), PersonActivity.class);
                    intentPerson.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentPerson);
                    break;
                case R.id.nav_poetry:
                    drawerLayout.closeDrawers();
                    Intent intentPoetry = new Intent(MyApplication.getContext(), PoetryActivity.class);
                    intentPoetry.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentPoetry);
                    break;
                default:
                    Toast.makeText(MainActivity.this, "该页面正在开发中,敬请期待", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    break;
            }
            return true;
        });

        //查询数据库
        cityDbList.clear();
        cityDbList = LitePal.findAll(CityDb.class);
        //判断是否添加了城市
        isCardEmpty();
        cardAdapter = new CardAdapter(cityDbList);
        recyclerView.setAdapter(cardAdapter);

        //------------------------刷新监听----------------------------
        mPullRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                for (CityDb cityDb : cityDbList) {
                    refreshCityList(cityDb);
                }
                cardAdapter.notifyDataSetChanged();
                Log.d(TAG, "onRefresh: 页面已更新");
                mPullRefreshLayout.finishRefresh();
            }
        });
    }

    private void refreshCityList(CityDb cityDb) {
        int i = cityDbList.indexOf(cityDb);
        HeWeather.getWeatherNow(this, cityDb.getCityDb_cid(), new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ",throwable);
            }

            @Override
            public void onSuccess(List<Now> list) {
                Log.d(TAG, "onSuccess: 刷新成功"+cityDb.getCityDb_cityName()+list.get(0).getNow().getCond_txt());
                cityDbList.get(i).setCityDb_txt(list.get(0).getNow().getCond_txt());
                cityDbList.get(i).setCityDb_temperature(list.get(0).getNow().getTmp()+"℃");
            }
        });
    }

    /**
     * 判断是否添加了城市
     */
    private void isCardEmpty() {
        Log.d(TAG, "isCardEmpty: "+cityDbList.size());
        if (cityDbList.size() == 0) {
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
            case R.id.github:
                Uri uri = Uri.parse("https://github.com/hzm0321/PoetryWeather");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.update:
                SharedPreferences preferences = getSharedPreferences("control", MODE_PRIVATE);
                try {
                    int getVersionCode = preferences.getInt("versionCode",0);
                    int nowVersionCOde = getPackageManager().getPackageInfo(getPackageName(),0).versionCode;
                    if (getVersionCode > nowVersionCOde) {
                        Toast.makeText(MainActivity.this, "此软件已发布更新,请到GitHub本项目地址下载最新版", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,"已是最新版",Toast.LENGTH_SHORT).show();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.about:
                Intent intent1 = new Intent(this, AboutActivity.class);
                startActivity(intent1);
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
    public void searchCityEvent(final SearchCityEvent searchCityEvent) {
        Log.d(TAG, "Event: "+ searchCityEvent.getCityCode());
        //显示加载框
        showLoading();
        HeWeather.getWeatherNow(this, searchCityEvent.getCityCode(), new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ",throwable);
                Toast.makeText(MainActivity.this,"天气获取失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<Now> list) {
                Log.i(TAG, "onSuccess: "+ new Gson().toJson(list));
                if (isRepeatCity(searchCityEvent.getCityCode())) {
                    Toast.makeText(MainActivity.this, "您已添加过该城市", Toast.LENGTH_SHORT).show();
                    closeLoading();
                } else {
                    SharedPreferences preferences = getSharedPreferences("control", MODE_PRIVATE);
                    int city_bg_number = preferences.getInt("city_bg", 0);
                    int numCity = new Random().nextInt(city_bg_number);
                    String cityBgUrl = "http://www.hzmeurasia.cn/PoetryWeather/city_background/main_city_bg" +
                            numCity +
                            ".jpg";
                    CityDb cityDb = new CityDb();
                    cityDb.setCityDb_cid(list.get(0).getBasic().getCid());
                            cityDb.setCityDb_cityName(list.get(0).getBasic().getLocation());
                            cityDb.setCityDb_txt(list.get(0).getNow().getCond_txt());
                            cityDb.setCityDb_temperature(list.get(0).getNow().getTmp()+"℃");
                            cityDb.setCityDb_imageId(cityBgUrl);
                            cityDb.save();
                    cityDbList.add(new CityDb(list.get(0).getBasic().getCid(), list.get(0).getBasic().getLocation(),
                            list.get(0).getNow().getCond_txt(), list.get(0).getNow().getFl(), cityBgUrl));
                    //刷新Card视图
                    cardAdapter.notifyDataSetChanged();
                    isCardEmpty();
                    closeLoading();
                }

            }
        });
    }

    /**
     * 判断主页面添加城市是否重复
     */
    private boolean isRepeatCity(String code) {
        for (CityDb cityDb : cityDbList) {
            if (cityDb.getCityDb_cid().equals(code)) {
                return true;
            }
        }
        return false;
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


    /**
     * nav读取本地缓存name数据
     */
    private void showName() {
        SharedPreferences sharedPreferences = getSharedPreferences("person", MODE_PRIVATE);
        personName = sharedPreferences.getString("name", "诗语天气");
        View myView = navigationView.getHeaderView(0);
        tvName = myView.findViewById(R.id.tv_nav_name);
        navCircleImageView = myView.findViewById(R.id.circle_image_nav_head);
        tvName.setText(personName);
        String path = "/sdcard/myHead/";
        File imageHead = new File(path+ "head.jpg");
        if (imageHead.exists()) {
            Glide.with(this)
                    .load(path + "head.jpg")
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(navCircleImageView);
        } else {
            Glide.with(this)
                    .load(R.drawable.head)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(navCircleImageView);
        }
    }

    /**
     * 显示加载进度框
     */
    private void showLoading() {
        tipDialog = new QMUITipDialog.Builder(MainActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载城市和天气数据......")
                .create();
        tipDialog.show();

    }

    /**
     * 关闭加载进度框
     */
    private void closeLoading() {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
    }

    /**
     * 是否添加定位城市弹框
     */
    private void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                .setTitle("获取到定位")
                .setMessage("欢迎您~\n" +
                        "来自"+districtName+"的网友\n" +
                        "是否添加"+districtName+"到您的天气城市列表")
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        searchCityEvent(new SearchCityEvent(districtName));
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void isFirst() {
        SharedPreferences.Editor editor = getSharedPreferences("isFirst", MODE_PRIVATE).edit();
        editor.putBoolean("first", true);
        editor.apply();
    }

    private boolean getFirst() {
        SharedPreferences preferences = getSharedPreferences("isFirst", MODE_PRIVATE);
        isFirst = preferences.getBoolean("first", false);
        return isFirst;
    }

    /**
     * 显示搜索城市界面
     */
    private void showSearchCity() {
        //添加热门城市
        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        hotCities.add(new HotCity("西安", "陕西", "101050311"));
        CityPicker.from(MainActivity.this)
                .enableAnimation(true)
                .setLocatedCity(new LocatedCity(districtName,provinceName,cityCode))
                .setHotCities(hotCities)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        Toast.makeText(MyApplication.getContext(),data.getName(),Toast.LENGTH_SHORT).show();
                        //组合cityCode
                        String cid = "CN" + data.getCode();
                        //发送EventBus事件
                        EventBus.getDefault().post(new SearchCityEvent(cid));
                    }

                    @Override
                    public void onLocate() {

                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }

    /**
     * 权限检查，申请定位所需的权限
     */
    private void checkLocationPower() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    mLocationClient.startLocation();
                } else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //更新数据
        updateDatabases();
        //解除注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showName();
    }


}
