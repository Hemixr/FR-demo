package com.example.service;

import java.util.Map;

public interface CameraService {

    boolean initCamera(int cameraId);

    String captureFrame();

    void startVideoStream();

    void stopVideoStream();

    void releaseCamera();

    boolean cameraRunning();

    String getCameraInfo();

    Map<String, Object> faceCompare(String knowImageBase64);

    Map<String, Object> continuousFaceCompare();
}
