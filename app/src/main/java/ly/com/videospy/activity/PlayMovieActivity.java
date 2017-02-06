package ly.com.videospy.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import ly.com.videospy.R;
import ly.com.videospy.util.Constant;
import ly.com.videospy.util.HtmlUtil;
import okhttp3.Call;

/**
 *
 */
public class PlayMovieActivity extends AppCompatActivity {
    private WebView mWebView;
    //    private CircleProgressView circle_progress;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_jingjia_context);
        initView();

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("播放视频");
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            //设置返回按钮
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
//        circle_progress = (CircleProgressView) findViewById(R.id.circle_progress);
//        showProgress();
        mWebView = (WebView) findViewById(R.id.notice_web_view);
        mWebView.setInitialScale(200);//为25%，最小缩放等级
//        mWebView.setWebChromeClient(client);
        // 设置WebView的类型
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        initData(Constant.Movie_Number_Path + getIntent().getStringExtra("url"));
        mWebView.loadUrl(Constant.Movie_Number_Path + getIntent().getStringExtra("url"));
    }

//    @Override
//    public void onCreateCustomToolBar(Toolbar toolbar) {
//        super.onCreateCustomToolBar(toolbar);
//        toolbar.showOverflowMenu();
//        View v = getLayoutInflater().inflate(R.layout.toobar_button, toolbar);
//        TextView title = (TextView) v.findViewById(R.id.title_tooblar);
//        toolbar.setText("公告详情");
//        LinearLayout return_b = (LinearLayout) v.findViewById(R.id.return_b);
//        return_b.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }

//    /**
//     * 进度条设置
//     */
//    private WebChromeClient client = new WebChromeClient() {
//        public void onProgressChanged(WebView view, int newProgress) {
//            super.onProgressChanged(view, newProgress);
//            circle_progress.setProgress(newProgress);
//            System.err.println("进度条" + newProgress);
//            if (circle_progress.getProgress() == 100) {
//
//                circle_progress.setVisibility(view.GONE);
////                circle_progress.spin();//旋转
//                circle_progress.stopSpinning();
//            }
//        }
//    };

//    private void showProgress() {
//
//        circle_progress.setVisibility(View.VISIBLE);
//        circle_progress.spin();//旋转
//    }
//
//    public void hideProgress() {
//
//        circle_progress.setVisibility(View.GONE);
//        circle_progress.stopSpinning();
//
//    }

    private void initData(String path) {
        OkHttpUtils
                .get()
                .url(path)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        hideProgress();
                    }

                    @Override
                    public void onResponse(String string, int id) {
                        try {
//                            String htmlData = subString(string);
//                            hideProgress();
                            mWebView.loadData(string, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
//
//                            //设置图片
//                            Glide.with(FenXiang_Jingjia_Activity.this).load(dailyDetail.getImage()).placeholder(R.mipmap.account_avatar).into(mDetailImage);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                });


    }


    /**
     * 截取字符串
     *
     * @return
     */
    private String subString(String html) {
        int str_start = html.indexOf("zb_in_fontcon") + 15;
        int str_end = html.indexOf("zb_nextprv");
        String myString = null;
        if (str_start != -1 && str_end != -1) {
            String substring = html.substring(str_start, str_end);
            //截取图片地址
            int img_start = substring.indexOf("/Uploa");
            int img_end = substring.indexOf("/U") + 2;
            if (img_start != -1 && img_end != -1) {
                String substring1 = substring.substring(img_start, img_end);
                myString = substring.replaceAll(substring1, "http://abc.webbiao.com/U");
            } else {
                myString = substring;
            }

        }
        return myString;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
