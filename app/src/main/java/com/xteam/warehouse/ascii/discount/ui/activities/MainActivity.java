package com.xteam.warehouse.ascii.discount.ui.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xteam.warehouse.ascii.discount.R;
import com.xteam.warehouse.ascii.discount.business.datahandling.DataFetchListener;
import com.xteam.warehouse.ascii.discount.business.datahandling.DataManager;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.BaseResponse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DataFetchListener {

    private Button mSearchAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSearchAllButton = (Button) findViewById(R.id.searchAllButton);
        mSearchAllButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchAllButton:
                DataManager.getInstance().fetchData(null, this);
        }
    }


    @Override
    public void onSuccess(@NonNull BaseResponse response) {
        if (response.getResponseType() == BaseResponse.NDJSON_RESPONSE) {
            Toast.makeText(MainActivity.this, "Success " + response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(@Nullable Throwable throwable) {
        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
    }
}
