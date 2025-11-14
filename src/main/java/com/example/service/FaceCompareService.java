package com.example.service;

import java.util.Map;

public interface FaceCompareService {

    Map<String,Object> compareImages(String imageBase641, String imageBase642);

    Map<String,Object> detectFace(String imageBase64);
}
