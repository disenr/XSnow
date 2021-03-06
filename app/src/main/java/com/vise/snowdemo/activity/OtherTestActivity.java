package com.vise.snowdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vise.log.ViseLog;
import com.vise.snowdemo.R;
import com.vise.snowdemo.db.DbHelper;
import com.vise.snowdemo.mode.GithubEvent;
import com.vise.snowdemo.mode.GithubModel;
import com.vise.xsnow.cache.MemoryCache;
import com.vise.xsnow.cache.SpCache;
import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.event.EventSubscribe;
import com.vise.xsnow.event.IEvent;
import com.vise.xsnow.ui.BaseActivity;

/**
 * @Description: 数据库、缓存及事件发送展示，具体展示结果参考日志
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 17/1/18 23:10.
 */
public class OtherTestActivity extends BaseActivity {

    private Button mDb_insert;
    private Button mDb_delete;
    private Button mDb_update;
    private Button mDb_query;
    private Button mMemory_cache_put;
    private Button mMemory_cache_get;
    private Button mSp_cache_put;
    private Button mSp_cache_get;
    private Button mSend_event;
    private SpCache spCache;
    private GithubModel mGithubModel;
    private boolean mIsInsert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRegisterEvent(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_test);
    }

    @Override
    protected void initView() {
        mDb_insert = F(R.id.db_insert);
        mDb_delete = F(R.id.db_delete);
        mDb_update = F(R.id.db_update);
        mDb_query = F(R.id.db_query);
        mMemory_cache_put = F(R.id.memory_cache_put);
        mMemory_cache_get = F(R.id.memory_cache_get);
        mSp_cache_put = F(R.id.sp_cache_put);
        mSp_cache_get = F(R.id.sp_cache_get);
        mSend_event = F(R.id.send_event);
    }

    @Override
    protected void bindEvent() {
        C(mDb_insert);
        C(mDb_delete);
        C(mDb_update);
        C(mDb_query);
        C(mMemory_cache_put);
        C(mMemory_cache_get);
        C(mSp_cache_put);
        C(mSp_cache_get);
        C(mSend_event);
    }

    @Override
    protected void initData() {
        spCache = new SpCache(mContext);
        mGithubModel = new GithubModel();
        mGithubModel.setUser_url("https://api.github.com/users/{user}");
        mGithubModel.setEmails_url("https://api.github.com/user/emails");
        mGithubModel.setEmojis_url("https://api.github.com/emojis");
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()) {
            case R.id.db_insert:
                if (!mIsInsert) {
                    mIsInsert = true;
                    DbHelper.getInstance().gitHub().insert(mGithubModel);
                }
                break;
            case R.id.db_delete:
                if (mIsInsert) {
                    mIsInsert = false;
                    DbHelper.getInstance().gitHub().delete(mGithubModel);
                }
                break;
            case R.id.db_update:
                if (mIsInsert) {
                    mGithubModel.setUser_url("https://api.github.com/users/xiaoyaoyou1212");
                    DbHelper.getInstance().gitHub().update(mGithubModel);
                }
                break;
            case R.id.db_query:
                ViseLog.i(DbHelper.getInstance().gitHub().loadAll());
                break;
            case R.id.memory_cache_put:
                MemoryCache.getInstance().put("xyy", mGithubModel);
                break;
            case R.id.memory_cache_get:
                ViseLog.i(MemoryCache.getInstance().get("xyy"));
                break;
            case R.id.sp_cache_put:
                spCache.put("xyy", mGithubModel);
                break;
            case R.id.sp_cache_get:
                ViseLog.i(spCache.get("xyy"));
                break;
            case R.id.send_event:
                BusFactory.getBus().post(new GithubEvent().setGithubModel(mGithubModel));
                break;
        }
    }

    @EventSubscribe
    public void showGithub(IEvent event) {
        if (event != null && event instanceof GithubEvent) {
            ViseLog.i("Receive Event Message:" + ((GithubEvent) event).getGithubModel());
        }
    }
}
