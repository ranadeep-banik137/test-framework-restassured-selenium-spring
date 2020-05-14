package com.app.testautomation.utilities;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.springframework.stereotype.Component;
import org.testng.log4testng.Logger;

@Component(value = "converter")
public class ImageConverterUtility {

	private static final Logger LOGGER = Logger.getLogger(ImageConverterUtility.class);
	
	private int widthResolution;
	private int heightResolution;
	private String imagesLocation;
	private String videoLocation;
	private String videoFormat;
	private String videoFileName;
	private String defaultVideoFormat = "mp4";
	
	
	public int getWidthResolution() {
		return widthResolution;
	}
	public void setWidthResolution(int widthResolution) {
		this.widthResolution = widthResolution;
	}
	public int getHeightResolution() {
		return heightResolution;
	}
	public void setHeightResolution(int heightResolution) {
		this.heightResolution = heightResolution;
	}
	public String getImagesLocation() {
		return imagesLocation;
	}
	public void setImagesLocation(String imagesLocation) {
		this.imagesLocation = imagesLocation;
	}
	public String getVideoLocation() {
		return videoLocation;
	}
	public void setVideoLocation(String videoLocation) {
		FileUtils.createDirectory(videoLocation);
		this.videoLocation = videoLocation;
	}
	public String getVideoFormat() {
		return videoFormat;
	}
	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}
	
	public void setVideoResolution(int videoWidthResolution, int videoHeightResolution) {
		setWidthResolution(videoWidthResolution);
		setHeightResolution(videoHeightResolution);
	}
	
	public void convertJPGtoMovie(Set<String> images) {
		OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(getVideoLocation() + "/" + getVideoFileName(), getWidthResolution(), getHeightResolution());
		try {
			recorder.setFrameRate(Double.valueOf(2));
			recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
			recorder.setVideoBitrate(9000);
			recorder.setFormat(getVideoFormat() == null ? defaultVideoFormat : getVideoFormat());
			recorder.setVideoQuality(0);
			recorder.start();
			Iterator<String> imageIterator = images.iterator();

			while (imageIterator.hasNext()) {
				recorder.record(grabberConverter.convert(cvLoadImage(imageIterator.next())));
			}
			recorder.stop();
		} catch (org.bytedeco.javacv.FrameRecorder.Exception exception) {
			LOGGER.info(exception.getMessage());
		}
	}
	
	public Set<String> getImages() {
		File imagePath = new File(getImagesLocation());
		File[] imagesArray = imagePath.listFiles();
		Arrays.sort(imagesArray, (f1, f2) -> f1.compareTo(f2));
		Set<String> images = new TreeSet<>();
		for (File eachFile : imagesArray) {
			images.add(eachFile.getAbsolutePath());
		}
		return images;
	}
	
	public void convert() {
		convertJPGtoMovie(getImages());
		LOGGER.info("Video created on location : " + getVideoLocation() + "/" + getVideoFileName());
	}
	
	public String getVideoFileName() {
		return videoFileName;
	}
	
	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName.split("[.]").length > 1 ? videoFileName : videoFileName.concat("." + defaultVideoFormat);
	}
	
	public ImageConverterUtility appendVideoFileName(String name) {
		String format = getVideoFileName().split("[.]").length > 1 ? getVideoFileName().split("[.]")[1] : null;
		String videoName = getVideoFileName().split("[.]")[0].concat(name).concat(".").concat(format == null ? getDefaultVideoFormat() : format);
		setVideoFileName(videoName);
		return this;
	}
	
	public String getDefaultVideoFormat() {
		return defaultVideoFormat;
	}
}
