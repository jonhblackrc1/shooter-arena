package com.blackstudio.shooterarena;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.Uri;
import java.io.InputStream;

public class MainActivity extends Activity {
    // The game files ship inside the APK (assets/www) and are served on a fixed
    // https origin so localStorage (progress, coins, scores) persists between launches.
    private static final String HOST = "appassets.shooterarena";
    private WebView web;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        web = new WebView(this);
        web.setBackgroundColor(0xFF07080F);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(false);
        web.setWebViewClient(new AssetClient());
        setContentView(web);
        hideBars();
        if (b != null) web.restoreState(b);
        else web.loadUrl("https://" + HOST + "/index.html");
    }

    private static String mime(String p) {
        if (p.endsWith(".html")) return "text/html";
        if (p.endsWith(".js")) return "application/javascript";
        if (p.endsWith(".json")) return "application/json";
        if (p.endsWith(".png")) return "image/png";
        if (p.endsWith(".css")) return "text/css";
        return "application/octet-stream";
    }

    @SuppressWarnings("deprecation")
    private void hideBars() {
        web.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override public void onWindowFocusChanged(boolean f) { super.onWindowFocusChanged(f); if (f) hideBars(); }
    @Override protected void onSaveInstanceState(Bundle o) { super.onSaveInstanceState(o); web.saveState(o); }
    @Override protected void onPause() { super.onPause(); web.onPause(); }
    @Override protected void onResume() { super.onResume(); web.onResume(); hideBars(); }
    @SuppressWarnings("deprecation")
    @Override public void onBackPressed() {
        // Back opens the in-game pause menu instead of closing the app mid-run
        web.evaluateJavascript("(function(){try{if(typeof state!=='undefined'&&state==='play'){openPause();return 'p';}}catch(e){}return 'x';})()",
            new BackCallback());
    }

    private void superBack() { super.onBackPressed(); }

    final class BackCallback implements android.webkit.ValueCallback<String> {
        @Override public void onReceiveValue(String r) { if (r == null || !r.contains("p")) superBack(); }
    }

    final class AssetClient extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView v, WebResourceRequest req) {
            Uri u = req.getUrl();
            if (!HOST.equals(u.getHost())) return null;
            String path = u.getPath();
            if (path == null || path.equals("/")) path = "/index.html";
            try {
                InputStream in = getAssets().open("www" + path);
                return new WebResourceResponse(mime(path), "utf-8", in);
            } catch (Exception e) {
                return new WebResourceResponse("text/plain", "utf-8", 404, "Not Found", null, null);
            }
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest req) {
            return !HOST.equals(req.getUrl().getHost());
        }
    }
}
