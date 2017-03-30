package com.example.songmoo.permissionsetting;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

/**
 * 1. 권한처리를 담당하는 클래스
 * 권한변경시 PERMISSION_ARRAY의 값만 변경해주면 된다
 *
 * 2. Activity 에서 사용하기
 * public class Activity implements PermissionControl.Callback{
 *     @Override
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.activity_main);
 *         // 권한체크 호출
 *         PermissionControl.checkPermission(this);
 *     }
 *
 *     @Override
 *     public Activity getActivity(){
 *         return this;
 *     }
 *     @Override
 *     public void init(){
 *         // 초기화 로직
 *     }
 *
 *     @Override
 *     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 *         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 *         // 결과처리
 *         PermissionControl.onCheckResult(requestCode,grantResults);
 *     }
 * }
 */

public class PermissionControl {
    // 권한요청코드
    private static final int REQ_PERMISSION = 9760345;
    private static Callback callback = null;

    // 요청할 권한 목록
    public static final String PERMISSION_ARRAY[] = {
            Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.INTERNET
    };

    // 권한체크 함수
    @TargetApi(Build.VERSION_CODES.M)
    public static void checkPermission(Callback object){
        callback = object;
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            Activity activity = callback.getActivity();
            // 1.1 런타임 권한체크
            // 위에 설정한 권한을 반복문을 돌면서 처리한다...
            boolean permCheck = true;
            for (String perm : PERMISSION_ARRAY) {
                if (activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                    permCheck = false;
                    break;
                }
            }
            // 1.2 퍼미션이 모두 true 이면 그냥 프로그램 실행
            if (permCheck) {
                callback.init();
            } else {
                activity.requestPermissions(PERMISSION_ARRAY, REQ_PERMISSION);
            }
        }else {
            callback.init();
        }
    }

    // 권한체크 후 콜백처리
    public static void onCheckResult(int requestCode, int[] grantResults) {
        boolean checkResult = true;
        if(requestCode == REQ_PERMISSION) {
            // 권한처리 결과값을 반복문을 돌면서 확인한 후 하나라도 승인되지 않았다면 false를 리턴해준다
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false;
                    break;
                }
            }
            if(callback != null) {
                if (checkResult) {
                    callback.init();
                } else {
                    Toast.makeText(callback.getActivity(), "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다.", Toast.LENGTH_LONG).show();
                }
                callback = null;
            }
        }
    }

    interface Callback {
        public Activity getActivity();
        public void init();
    }
}
