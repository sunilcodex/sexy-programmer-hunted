package com.hunted;

import android.app.Activity;
import android.content.Intent;
import net.emome.hamiapps.sdk.UserService;
import net.emome.hamiapps.sdk.exception.AMNeedUpdateException;
import net.emome.hamiapps.sdk.exception.AMNotFoundException;
public class MyActivity extends Activity
{ private static int LOGIN_REQUEST = 1; private void startRemoteLicenseCheckActivity() throws AMNotFoundException, AMNeedUpdateException
{
Intent intent = UserService.getLoginIntent(this);
startActivityForResult(intent, LOGIN_REQUEST);
} @Override
public void onActivityResult(int reqCode, int resultCode, Intent data)
{
if(reqCode == LOGIN_REQUEST)
{
if(resultCode == RESULT_OK)
{
// 用戶完成登入程序；
}
else
{
// 用戶未完成登入程序；
}
}
}
}