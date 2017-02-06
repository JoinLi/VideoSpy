package ly.com.videospy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

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

import ly.com.videospy.R;
import ly.com.videospy.adapter.MovieAdapter;
import ly.com.videospy.bean.MovieBean;
import ly.com.videospy.util.Constant;
import ly.com.videospy.util.LogUtil;
import ly.com.videospy.util.Util;
import okhttp3.Call;


public class MovieActivity extends AppCompatActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private EasyRecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<MovieBean> list = new ArrayList<MovieBean>();
    private int page = 1;
    private LinearLayout liner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        liner = (LinearLayout) findViewById(R.id.liner);
        liner.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("视频选择");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this, 0.5f), Util.dip2px(this, 72), 0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new MovieAdapter(this);
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
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(MovieActivity.this, PlayMovieActivity.class);
                intent.putExtra("url",adapter.getAllData().get(position).getMovie_api());
                startActivity(intent);
            }
        });
        recyclerView.setRefreshListener(this);  //下拉刷新
//        adapter.addAll(list);
        onRefresh();

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
        String path = Constant.Movie_Number_Path + getIntent().getStringExtra("url");
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
                                Elements links = doc.select("DIV.c-box");
                                Elements elements = links.select("LI");
                                for (Element element : elements) {
                                    MovieBean bean = new MovieBean();
                                    bean.setMovie_title(element.select("A").attr("title"));
                                    bean.setMovie_api(element.select("A").attr("href"));
                                    LogUtil.m("视频名称" + element.select("A").attr("title"));
                                    LogUtil.m("视频接口" + element.select("A").attr("href"));
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}