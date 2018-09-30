package com.example.joancolmenero.doubleclickbackrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    private static final long EXIT_TIMEOUT = 2000;
    private PublishSubject<Boolean> backButtonClickSource = PublishSubject.create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        observeBackButton();
    }

    @NonNull
    private Disposable observeBackButton() {
        return backButtonClickSource
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(__ -> Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show())
                .timeInterval(TimeUnit.MILLISECONDS)
                .skip(1)
                .filter(interval -> interval.time() < EXIT_TIMEOUT)
                .subscribe(__ -> MainActivity.this.finish());
    }

    @Override
    public void onBackPressed() {
        backButtonClickSource.onNext(true);
    }
}
