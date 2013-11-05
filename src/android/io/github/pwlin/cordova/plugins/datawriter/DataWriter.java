package io.github.pwlin.cordova.plugins.datawriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.util.Log;
import android.os.Environment;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

import io.github.pwlin.cordova.plugins.base64.Base64Plugin;

public class DataWriter extends CordovaPlugin {

	

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {


		
		if (!action.equals("writeToFile")) 
			return new PluginResult(PluginResult.Status.INVALID_ACTION);
		
		try {
			
			String fileContent = args.getString(0);
			JSONObject params = args.getJSONObject(1);
			
			String fileName = params.has("fileName") ? params.getString("fileName"): "unknown_file";
			
			String dirName = params.has("dirName") ? params.getString("dirName"): Environment.getExternalStorageDirectory().getPath() + "/Download";
					
			Boolean overwrite = params.has("overwrite") ? params.getBoolean("overwrite") : true;
			
			Boolean dataIsBase64Encoded = params.has("dataIsBase64Encoded") ? params.getBoolean("dataIsBase64Encoded") : true;
			
			return this._writeToFile(fileContent, dirName, fileName, overwrite, dataIsBase64Encoded, callbackId);
			
		} catch (JSONException e) {
			e.printStackTrace();
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION, e.getMessage());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			return new PluginResult(PluginResult.Status.ERROR, e.getMessage());
		}
	
	}

	private PluginResult _writeToFile(String fileContent, String dirName, String fileName, Boolean overwrite, Boolean dataIsBase64Encoded, String callbackId) throws InterruptedException, JSONException {
		
		try {
			
			File dir = new File(dirName);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File fileTmp = new File(dirName, fileName);
			
			if (!overwrite && fileTmp.exists()) {
				fileName = (new Date().getTime()) + "-" + fileName;
			}
			
			fileTmp = null;
			
			File file = new File(dirName, fileName);
				
			FileOutputStream fos = new FileOutputStream(file);
			
			byte[] res2;
			if (dataIsBase64Encoded == true) {
				res2 = Base64Plugin.decode(fileContent);
			} else {
				res2 = fileContent.getBytes();
			}

		    fos.write(res2);
			fos.close();

			JSONObject obj = new JSONObject();
			obj.put("status", 1);
			obj.put("fileName", fileName);
			obj.put("dirName", dirName);
			
			return new PluginResult(PluginResult.Status.OK, obj);
			
		}
		catch (FileNotFoundException e) {
			return new PluginResult(PluginResult.Status.ERROR, 404);
		}
		catch (IOException e) {
			return new PluginResult(PluginResult.Status.ERROR, e.getMessage());
		}

	}

}