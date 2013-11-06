package io.github.pwlin.cordova.plugins.datawriter;

import io.github.pwlin.cordova.plugins.base64.Base64Plugin;
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
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

public class DataWriter extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		if (action.equals("writeToFile")) {

			try {

				String fileContent = args.getString(0);
				JSONObject params = args.getJSONObject(1);
				String fileName = params.has("fileName") ? params.getString("fileName") : "unknown_file";
				String dirName = params.has("dirName") ? params.getString("dirName") : Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DOWNLOADS;
				Boolean overwrite = params.has("overwrite") ? params.getBoolean("overwrite") : true;
				Boolean dataIsBase64Encoded = params.has("dataIsBase64Encoded") ? params.getBoolean("dataIsBase64Encoded") : true;
				return this._writeToFile(fileContent, dirName, fileName, overwrite, dataIsBase64Encoded, callbackContext);

			} catch (JSONException e) {

				// e.printStackTrace();
				JSONObject errorObj = new JSONObject();
				errorObj.put("status", PluginResult.Status.JSON_EXCEPTION.ordinal());
				errorObj.put("message", e.getMessage());
				callbackContext.error(errorObj);
				return false;

			} catch (InterruptedException e) {

				// e.printStackTrace();
				JSONObject errorObj = new JSONObject();
				errorObj.put("status", PluginResult.Status.ERROR.ordinal());
				errorObj.put("message", "Interrupted exception: " + e.getMessage());
				callbackContext.error(errorObj);
				return false;

			}

		} else {

			JSONObject errorObj = new JSONObject();
			errorObj.put("status", PluginResult.Status.INVALID_ACTION.ordinal());
			errorObj.put("message", "Invalid action");
			callbackContext.error(errorObj);
			return false;

		}

	}

	private Boolean _writeToFile(String fileContent, String dirName, String fileName, Boolean overwrite, Boolean dataIsBase64Encoded, CallbackContext callbackContext) throws InterruptedException, JSONException {

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
			obj.put("status", PluginResult.Status.OK.ordinal());
			obj.put("fileName", fileName);
			obj.put("dirName", dirName);
			obj.put("fileFullPath", dirName + "/" + fileName);

			callbackContext.success(obj);
			return true;

		} catch (FileNotFoundException e) {

			JSONObject errorObj = new JSONObject();
			errorObj.put("status", PluginResult.Status.ERROR.ordinal());
			errorObj.put("message", "File not found: " + e.getMessage());
			callbackContext.error(errorObj);
			return false;

		} catch (IOException e) {

			JSONObject errorObj = new JSONObject();
			errorObj.put("status", PluginResult.Status.ERROR.ordinal());
			errorObj.put("message", "IO exception: " + e.getMessage());
			callbackContext.error(errorObj);
			return false;

		}

	}

}