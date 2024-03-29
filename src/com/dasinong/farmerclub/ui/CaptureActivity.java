package com.dasinong.farmerclub.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.CurrentCouponInfoEntity;
import com.dasinong.farmerclub.entity.LoginRegEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.umeng.analytics.MobclickAgent;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends BaseActivity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private TopbarView topbar;
    private boolean isFarmer;

    // private Button cancelScanButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scan_qrcoder);

        String type = SharedPreferencesHelper.getString(this, Field.USER_TYPE, SelectUserTypeActivity.FARMER);
        if (SelectUserTypeActivity.RETAILER.equals(type)) {
            isFarmer = false;
        } else {
            isFarmer = true;
        }

        // ViewUtil.addTopView(getApplicationContext(), this,
        // R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        // cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
        topbar = (TopbarView) findViewById(R.id.topbar);
        initTopar();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

        // quit the scan view
        // cancelScanButton.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // CaptureActivity.this.finish();
        // }
        // });
    }

    private void initTopar() {
        topbar.setCenterText("扫描二维码");
        topbar.setLeftView(true, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(CaptureActivity.this, "未能识别的二维码", Toast.LENGTH_SHORT).show();
        } else {

            // 友盟统计自定义统计事件
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("url", resultString);
            MobclickAgent.onEvent(this, "ScanQRcodeSuccess", map);

            dispatchRequest(resultString);
        }
        CaptureActivity.this.finish();
    }

    private void dispatchRequest(String resultString) {
        resultString = resultString.trim();
        if (resultString.startsWith("http:/")) {
            if((resultString.startsWith("http://winsafe.cn/?b=") || resultString.startsWith("http:/awinsafe.cn/?b=")) && !isFarmer){
                int start = resultString.indexOf("b=") + 2;
                resultString = resultString.substring(start ,start + 25);
                System.out.println(resultString  + " resultString +++++++++++++++++++++  ");
                Intent intent = new Intent(this, ScanProductResultActivity.class);
                intent.putExtra("boxcode", resultString);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", resultString);
                startActivity(intent);
            }
        } else {
            if (resultString.contains("&")) {
                String[] split = resultString.split("&");
                if ("refcode".equals(split[0].split("=")[1])) {
                    String refCode = split[1].split("=")[1];
                    Intent intent = new Intent();
                    intent.putExtra("refcode", refCode);
                    setResult(RESULT_OK, intent);
//					sendRefQuery(refCode);
                } else if ("coupon".equals(split[0].split("=")[1])) {
                    String userId = split[1].split("=")[1];
                    String couponId = split[2].split("=")[1];
                    sendCouponQuery(userId, couponId);
                }
            } else if (resultString.length() == 25 && !isFarmer) {
//				String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));
                Intent intent = new Intent(this, ScanProductResultActivity.class);
                intent.putExtra("boxcode", resultString);
                startActivity(intent);
            } else {
                showToast("请扫描有效二维码");
            }
        }
    }

    private void sendCouponQuery(String userId, String couponId) {

        startLoadingDialog();
        RequestService.getInstance().redeemCoupon(this, couponId, userId, CurrentCouponInfoEntity.class, new RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {
                if (resultData.isOk()) {
//                    showToast("使用成功");
                    Toast.makeText(CaptureActivity.this,resultData.getMessage(),Toast.LENGTH_LONG).show();
                    CurrentCouponInfoEntity entity = (CurrentCouponInfoEntity) resultData;
                    if (entity.data != null && entity.data.coupon != null) {
                        if(entity.data.coupon.campaignId != 15){
                            Intent intent = new Intent(CaptureActivity.this, RedeemRecordsActivity.class);
                            intent.putExtra("campaignId", entity.data.coupon.campaignId);
                            startActivity(intent);
                        }
                    }
                } else if ("2101".equals(resultData.getRespCode())) {
//                    showToast("已使用的优惠券");
                    showToast(resultData.getMessage());
                } else if ("2102".equals(resultData.getRespCode())) {
//                    showToast("请在有效时间内使用");
                    showToast(resultData.getMessage());
                } else if ("2103".equals(resultData.getRespCode())) {
//                    showToast("未授权扫描该优惠券");
                    showToast(resultData.getMessage());
                } else if ("2014".equals(resultData.getRespCode())) {
//                    showToast("不能使用他人的优惠券");
                    showToast(resultData.getMessage());
                } else if ("2107".equals(resultData.getRespCode())) {
//                    showToast("店铺授权已用完，请入库更多产品");
                    showToast(resultData.getMessage());
                } else {
                    showToast(R.string.please_check_netword);
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {
                dismissLoadingDialog();
            }
        });

    }

    private void sendRefQuery(String refCode) {
        startLoadingDialog();
        RequestService.getInstance().setRef(this, refCode, LoginRegEntity.class, new RequestListener() {
            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {
                if (resultData.isOk()) {
                    LoginRegEntity entity = (LoginRegEntity) resultData;
                    showToast("验证成功");

                    SharedPreferencesHelper.setInt(CaptureActivity.this, Field.REFUID, entity.getData().getRefuid());
                    SharedPreferencesHelper.setInt(CaptureActivity.this, Field.INSTITUTIONID, entity.getData().getInstitutionId());

                } else {
                    showToast(resultData.getMessage());
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {
                dismissLoadingDialog();
            }
        });
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
}
