package com.game.onecricket.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.onecricket.R;

public class WebviewAcitivity extends AppCompatActivity {

    private WebView wv1;
    WebviewAcitivity activity;
    Context context;
    ImageView im_back;
    TextView tv_HeaderName;
    SwipeRefreshLayout swipeRefreshLayout;
    String IntentHeading,IntentURL;
    String doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_acitivity);

        context = activity = this;
        IntentHeading = getIntent().getStringExtra("Heading");
        IntentURL = getIntent().getStringExtra("URL");
        // doc="http://docs.google.com/gview?embedded=true&url="+IntentURL;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24);
        toolbar.setTitle(IntentHeading);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));// your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        initViews();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        LoadPage(IntentURL);
                                    }
                                }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage(doc);

            }
        });

    }

    public void initViews(){

       // im_back = findViewById(R.id.im_back);
        //tv_HeaderName = findViewById(R.id.tv_HeaderName);
        wv1= findViewById(R.id.webView1);

       // tv_HeaderName.setText("About US");
        //im_back.setOnClickListener(new View.OnClickListener() {
        //    @Override
          //  public void onClick(View view) {
            //    onBackPressed();
            //}
        //});

    }

    public void LoadPage(String Url){
        wv1.setWebViewClient(new MyBrowser());
        wv1.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.getSettings().setAllowFileAccess(true);
        wv1.loadUrl(Url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



    @Override protected void onResume() {
        super.onResume();
        wv1.onResume();
        wv1.resumeTimers(); }


    @Override
    public void onBackPressed() {
        if(wv1.canGoBack()){
            wv1.goBack();
        }else{
            super.onBackPressed();
        }
    }


}
