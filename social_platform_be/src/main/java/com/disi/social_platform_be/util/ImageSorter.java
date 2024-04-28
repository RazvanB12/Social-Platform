package com.disi.social_platform_be.util;

import com.disi.social_platform_be.model.Image;

import java.util.List;

public class ImageSorter {

    public static void sortImagesByTimestampDescending(List<Image> imageList) {
        imageList.sort((image1, image2) -> Long.compare(image2.getUploadDate(), image1.getUploadDate()));
    }
}
