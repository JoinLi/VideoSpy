package ly.com.videospy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import ly.com.videospy.activity.MovieActivity;
import ly.com.videospy.adapter.PersonAdapter;
import ly.com.videospy.bean.InforBean;
import ly.com.videospy.util.ClearEditText;
import ly.com.videospy.util.Constant;
import ly.com.videospy.util.LogUtil;
import ly.com.videospy.util.Util;
import okhttp3.Call;


public class MainActivity extends AppCompatActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private EasyRecyclerView recyclerView;
    private PersonAdapter adapter;
    private List<InforBean> list = new ArrayList<InforBean>();
    private int page = 1;
    private ClearEditText mClearEditText;
    private String context = "鬼吹灯";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit_qd);
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this, 0.5f), Util.dip2px(this, 72), 0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new PersonAdapter(this);
        recyclerView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {

            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });
        recyclerView.setRefreshListener(this);  //下拉刷新
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(MainActivity.this, MovieActivity.class);
                intent.putExtra("url",adapter.getAllData().get(position).getMovie_url());
                startActivity(intent);
            }
        });
        adapter.addAll(list);
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {

        if (TextUtils.isEmpty(filterStr)) {

            adapter.addAll(list);


        } else {
            context = filterStr;
            page = 1;
            initData();

        }


    }

    @Override
    public void onLoadMore() {
        adapter.stopMore();
    }

    @Override
    public void onRefresh() {
        page = 1;
        initData();

    }


    private void initData() {
        String path = Constant.MoviePath + context;
        try {
            OkHttpUtils
                    .get()
                    .url(path)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (page == 1) {
                                adapter.clear();
                                list.clear();

                            } else {

                                adapter.stopMore();
                            }
                        }

                        @Override
                        public void onResponse(String string, int id) {
                            try {
                                if (page == 1) {//暂无数据
                                    adapter.clear();
                                    list.clear();
                                }

                                Document doc = Jsoup.parse(string);
                                Elements links = doc.select("div.plist").select("ul.list_tab_img");
                                Elements elements = links.select("li");
                                for (Element element : elements) {
                                    InforBean bean = new InforBean();
                                    bean.setMovie_url(element.select("a").attr("href"));
                                    bean.setImg_url(element.select("img").attr("src"));
                                    bean.setTitle(element.select("b").text());
                                    element.select("b").text();
                                    LogUtil.m("链接" + element.select("a").attr("href"));
                                    LogUtil.m("图片" + element.select("img").attr("src"));
                                    LogUtil.m("标题" + element.select("b").text());
                                    list.add(bean);

                                }
                                adapter.addAll(list);
                            } catch (Exception e) {
                                adapter.stopMore();
                                e.printStackTrace();

                            }


                        }

                    });

        } catch (Exception e) {
            adapter.stopMore();
            e.printStackTrace();

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
        switch (item.getItemId()) {
//            case R.id.action_mode:
//                //切换日夜间模式
//                mNightModeHelper.toggle();
//                return true;

            case R.id.action_settings:
                //设置
//                startActivity(new Intent(MainActivity.this, MoreActivity.class));
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    private void SnackerShow() {
//        Snackbar.make( getCurrentFocus(),getResources().getString(R.string.snacker_message), Snackbar.LENGTH_LONG)
//                .show();
    }

    private void SnackerShowMessage(int string) {
        Snackbar.make(getCurrentFocus(), getResources().getString(string), Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * 点击两次退出
     */
    // 点击两次退出
    boolean isExit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出",
                        Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        isExit = false;

                    }

                }, 2000);

                return false;
            }

        }

        return super.onKeyDown(keyCode, event);
    }


}