package com.meallogger.cordova.pickphotos;


import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import me.crosswall.photo.pick.PickConfig;
import me.crosswall.photo.pick.PickPhotosActiviy;

public class PickPhotos extends CordovaPlugin {
  private CallbackContext callbackContext;
  private JSONArray args;

  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.args = args;
    this.callbackContext = callbackContext;

    if (action.equals("pickPhotos")) {
      if(cordova.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
        pickPhoto();
      }
      else {
        cordova.requestPermission(this, PickConfig.PICK_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
      }
      return true;
    }

    return false;
  }

  public void pickPhoto() throws JSONException {
    Intent intent = new Intent(this.cordova.getActivity(), PickPhotosActiviy.class);
    intent.putExtra(PickConfig.EXTRA_PICK_BUNDLE, new Bundle());
    this.cordova.startActivityForResult(this, intent, PickConfig.PICK_REQUEST_CODE);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if(requestCode == PickConfig.PICK_REQUEST_CODE) {
      ArrayList<String> pick = intent.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);

      callbackContext.success(pick.size() > 0 ? "file://" + pick.get(0) : null);
    }
  }

  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
    for(int result : grantResults) {
      if(result == PackageManager.PERMISSION_DENIED) {
        callbackContext.error("Permission to read storage was denied.");
        return;
      }
    }

    if(requestCode == PickConfig.PICK_REQUEST_CODE) {
      pickPhoto();
    }
  }
}
