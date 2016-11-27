package com.nata.xdroid.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nata.xdroid.R;
import com.nata.xdroid.db.CrashInfo;
import com.nata.xdroid.db.RecordDao;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private RecordAdapter mRecordAdapter;
    private RecordDao recordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        recordDao = new RecordDao(this);
        listView = (ListView) findViewById(R.id.listView);

        mRecordAdapter = new RecordAdapter();
        listView.setAdapter(mRecordAdapter);
        listView.setOnItemClickListener(this);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    recordDao.delete((int) mRecordAdapter.getItemId(position));
                                    mRecordAdapter.remove(position);
                                }
                                mRecordAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CrashInfo crashInfo = mRecordAdapter.getItem(position);
        CrashDialog.show(this, crashInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<CrashInfo> crashInfos = recordDao.getAll();
        mRecordAdapter.update(crashInfos);
    }
}
