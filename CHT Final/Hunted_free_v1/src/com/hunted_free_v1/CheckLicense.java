package com.hunted_free_v1;

import net.emome.hamiapps.sdk.ForwardActivity;

public class CheckLicense extends ForwardActivity {

	@Override
	public Class getTargetActivity() {
		// TODO Auto-generated method stub
		return MainActivity.class;
	}
	
	@Override
	public boolean passOnUnavailableDataNetwork ()
	{ return true; }
	

}
