# 智慧展业



### 1.腾讯云SDK初始化

> 懒加载，在第一次使用sdk的api时进行sdk的初始化

- android 

  ```
  /**
   *
   * Application 
   * environment: TXSdk.Environment 当前环境 (DEV开发版本  TEST 体验版本 RELEASE 正式版本)
   * wechat: TxConfig 微信配置{txConfig.wxKey = "wxkey"（微信分享申请的appid）
                          txConfig.miniprogramType = TXSdk.Environment.TEST（切换对应的小程序版本，方便调试） //DEV小程序开发版本  TEST 体验版本 RELEASE 正式版本
                          txConfig.userName = "tx"（小程序的原始id） //小程序跳转参数}
  */
  TXSdk.getInstance().init(Application application, TXSdk.Environment environment, TxConfig wechat)
  ```

- ios

  ```
  /**
   *
   * environment: string 当前环境（"0": 开发, "1":测试, "2":生产）
   * wechat: json 微信配置{appid 微信开放id, universalLink 微信社会化分享回调链接,userName 小程序userName}
  */
  [TXTManage.shareInstance setEnvironment:(NSString *)environment wechat:(NSDictionary *)wechat]
  ```



### 2. 腾讯云SDK 唤起视频

> 云助理app调用接口唤起视频白板界面

- android

   ```
  /**
  *
  * account 账号 测试用testdev
  * org 机构 测试用test_org
  * sign aes-128-cbc加密org+时间戳(秒) 加密测试 key: net02d2geftdt4tj，测试IV:4kz8rn8a7yxdy9u8 
  */
  TXSdk.getInstance().startVideo(activity, String account, String org, String sign, StartVideoResultOnListener listener);
  ```

- ios

  ```
  /**
   * 鉴权验证
   * agentName string 用户名
   * org string  机构名
   * sign string 加密后的机构名
  */
  [TXTManage.sharedInstance startVideo:agentName OrgName:org SignOrgName:sign CallBack:^(int code, NSString * _Nonnull desc) {
  	
  		if (code != 0) {
  		
  				// Callback 错误
  				return;
  		}
  		
      NSString *path = [NSBundle.mainBundle pathForResource:@"txtBundle" ofType:@"bundle"];
      NSBundle *bundle = [NSBundle bundleWithPath:path];
      ClassroomViewController *ctrl = [[ClassroomViewController alloc] initWithNibName:@"ClassroomViewController" bundle:bundle];
      
      // 显示视频界面
      [nav pushViewController:ctrl animated:YES];
      // Callback 成功
  }]
  
  说明：
  		显示前先鉴权，验证成功（code ==0）时，显示视频界面，否则返回错误给上层调用方
  ```

### 3.云助理提供唤起视频

```
yzl.txtFaceTime({
		success: function(result) {},
		fail: function(error) {}
});

说明：
	 云助理通过JS-SDK向网页提供视频的接口
	 云助理初始化腾讯云sdk后调用腾讯云SDK提供的唤起视频功能，并透传参数
	 腾讯云SDK startVideo 返回的code 不等于0时，云助理回调js的fail方法，error = {code:code, msg:desc}
	 腾讯云SDK startVideo 返回的code 等于0时，云助理回调js的succes方法，result = {code:code, msg:desc}
```