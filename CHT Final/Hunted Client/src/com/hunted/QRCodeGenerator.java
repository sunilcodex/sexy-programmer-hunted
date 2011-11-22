package com.hunted;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.ByteMatrix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class QRCodeGenerator
{
	public static Bitmap encodeString(String input) 
	{
		URL aURL;

		try
		{
			aURL = new URL("http://chart.apis.google.com/chart?chs=300x300&cht=qr&choe=UTF-8&chl="+ URLEncoder.encode(input, "UTF-8"));
			URLConnection conn = aURL.openConnection();
			conn.connect();
			
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			
			return bm;
		} 
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;

	}
	
	public static Bitmap encodeString2(String input) 
	{
		try
		{
			BitMatrix result = new MultiFormatWriter().encode(input, BarcodeFormat.QR_CODE, 300, 300);
			int width = result.getWidth();
			int height = result.getHeight();
			int[] pixels = new int[width * height];
			// All are 0, or black, by default
			for (int y = 0; y < height; y++) 
			{
				int offset = y * width;
				for (int x = 0; x < width; x++) 
				{
					pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
				}
			}
			
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	  }
}
