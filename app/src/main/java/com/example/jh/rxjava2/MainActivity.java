package com.example.jh.rxjava2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * RxJava2的学习demo
 * 参考学习链接：http://blog.csdn.net/maplejaw_/article/details/52442065
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *  第一步：初始化一个Observable
         * ObservableEmitter ：发射器，用于发射数据（onNext）和通知（onError/onComplete）
         */


        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onComplete();
            }
        });

        /**
         *
         * 第二步：初始化一个Observer
         * 创建的Observer中多了一个回调方法onSubscribe，传递参数为Disposable ，Disposable 相当于RxJava1.x中的Subscription,用于解除订阅。
         *
         */

        Observer<Integer> observer= new Observer<Integer>() {

            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Integer value) {
                Log.d("JG", value.toString());
                if (value > 3) {   // >3 时为异常数据，解除订阅
                    disposable.dispose();
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        };


        // 第三部：建立订阅关系
        observable.subscribe(observer); //建立订阅关系


        /**
         * 此外，RxJava2.x中仍然保留了其他简化订阅方法，我们可以根据需求，
         * 选择相应的简化订阅。只不过传入的对象改为了Consumer。
         */
        Disposable disposable = observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                //这里接收数据项
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //这里接收onError
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //这里接收onComplete。
            }
        });


    }
}
