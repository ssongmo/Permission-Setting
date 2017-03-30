## Permission Setting
기본적인 권한을 Setting하기 위한 Class.

### 사용 방법

1. Menifest에서 권한 설정.

2. Activity 에서 사용하기
```
  public class Activity implements PermissionControl.Callback{
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          // 권한체크 호출
          PermissionControl.checkPermission(this);
      }

      @Override
      public Activity getActivity(){
          return this;
      }

      @Override
      public void init(){
          // 초기화 로직
      }

      @Override
      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          super.onRequestPermissionsResult(requestCode, permissions, grantResults);
          // 결과처리
          PermissionControl.onCheckResult(requestCode,grantResults);
      }
  }
 ```


3. PermissionControl.java 페이지에서 필요한 권한을 추가. (Menifest에 설정한 것과 동일.)
