package cn.nju.cs.seg.brasclient.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;

@SuppressLint("UseSparseArrays")
public class OCRUtil {
	
	public static Map<Bitmap, String> trainMap = null;
	
	/**
	 * 获取全部验证码字符
	 * @param String (图片网络链接)
	 * @return
	 * @throws Exception
	 */
	public static String getAllOcr(String picUrl) throws Exception {
		Bitmap img = removeBackgroud(picUrl);
		List<Bitmap> listImg = splitImage(img);
		Map<Bitmap, String> map = loadTrainData();
		String result = "";
		for (Bitmap bi : listImg) {
			result += getSingleCharOcr(bi, map);
		}
		return result;
	}
	
	/**
	 * 获取全部验证码字符
	 * @param InputStram (图片输入流)
	 * @return
	 * @throws Exception
	 */
	public static String getAllOcr(InputStream in) throws Exception {
		Bitmap img = removeBackgroud(in);
		List<Bitmap> listImg = splitImage(img);
		Map<Bitmap, String> map = loadTrainData();
		String result = "";
		for (Bitmap bi : listImg) {
			result += getSingleCharOcr(bi, map);
		}
		return result;
	}
	
	/**
	 * 获取单个验证码字符
	 * @param BufferedImage (图片字节流)
	 * @param Map (基准字符图片集)
	 * @return
	 */
	private static String getSingleCharOcr(Bitmap img, Map<Bitmap, String> map) {
		String result = "#";
		int width = img.getWidth();
		int height = img.getHeight();
		int min = width * height;
		for (Bitmap bi : map.keySet()) {
			int count = 0;
			if (Math.abs(bi.getWidth()-width) > 2)
				continue;
			int widthmin = width < bi.getWidth() ? width : bi.getWidth();
			int heightmin = height < bi.getHeight() ? height : bi.getHeight();
			Label1: for (int x = 0; x < widthmin; ++x) {
				for (int y = 0; y < heightmin; ++y) {
					if (isBlack(img.getPixel(x, y)) != isBlack(bi.getPixel(x, y))) {
						count++;
						if (count >= min)
							break Label1;
					}
				}
			}
			if (count < min) {
				min = count;
				result = map.get(bi);
			}
		}
		return result;
	}
	
	/**
	 * 加载训练图片集
	 * @return
	 * @throws Exception
	 */
	private static Map<Bitmap, String> loadTrainData() throws Exception {
		if (trainMap == null) {
			try {
				AssetManager assets =  MyApplication.getInstance().getAssets();
				String imgs[] = assets.list("train");
				OCRUtil.trainMap = new HashMap<Bitmap, String>();
				InputStream in = null;
				for (int i=0; i<imgs.length; i++)
				{
					in = assets.open("train/"+imgs[i]);
					OCRUtil.trainMap.put(BitmapFactory.decodeStream(in), imgs[i].charAt(1)+"");
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return trainMap;
	}
	
	/**
	 * 下载n张验证码图片到folder目录下
	 * @param httpUrl
	 * @param fileExtension
	 * @param n
	 * @param folder
	 */
	public static String downloadImage(String httpUrl, String fileExtension, int n, String folder) {
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream in = null;
			String fileName = "";
			FileOutputStream fos = null;
			byte[] buffer;
			
			for (int i=0; i<n; i++) {
				conn.connect();
				if (conn.getResponseCode() == 200) {
					in = conn.getInputStream();
					fileName = folder + i + "." + fileExtension;
					File file = new File(fileName);
					fos = new FileOutputStream(file.getAbsoluteFile());
					buffer = new byte[1024];
					int len = 0;
					while((len=in.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
				}
			}
			in.close();
			fos.close();
			
			return "下载完成";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "下载失败";
	}
	
	/**
	 * 构建训练模型
	 * @throws Exception
	 */
	public static void trainData() throws Exception {
		int index = 0;
		
		File dir = new File("temp");
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				Bitmap img = removeBackgroud("temp\\" + file.getName());
				List<Bitmap> listImg = splitImage(img);
				if (listImg.size() == 4) {
					for (int j = 0; j < listImg.size(); ++j) {
						listImg.get(j).compress(CompressFormat.JPEG, 100, new FileOutputStream(new File("train\\"
								+ file.getName().charAt(j) + "-" + (index++)
								+ ".jpg")));
					}
				}
			}
		}
	}
	
	/**
	 * 取出图片的背景
	 * @param picUrl
	 * @return BufferedImage
	 * @throws Exception
	 */
	private static Bitmap removeBackgroud(String picUrl) throws Exception {
		URL url = new URL(picUrl);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoInput(true);
		conn.connect();
		InputStream in = conn.getInputStream();
		Bitmap img = BitmapFactory.decodeStream(in);
		in.close();

		img = Bitmap.createBitmap(img, 1, 1, img.getWidth()-2, img.getHeight()-2);
		int width = img.getWidth();
		int height = img.getHeight();
		double subWidth = width / 5.0;
		for (int i = 0; i < 5; i++) {
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (int x = (int) (1 + i * subWidth); x < (i + 1) * subWidth
					&& x < width - 1; ++x) {
				for (int y = 0; y < height; ++y) {
					if (isWhite(img.getPixel(x, y)) == 1)
						continue;
					if (map.containsKey(img.getPixel(x, y))) {
						map.put(img.getPixel(x, y), map.get(img.getPixel(x, y)) + 1);
					} else {
						map.put(img.getPixel(x, y), 1);
					}
				}
			}
			int max = 0;
			int colorMax = 0;
			for (Integer color : map.keySet()) {
				if (max < map.get(color)) {
					max = map.get(color);
					colorMax = color;
				}
			}
			for (int x = (int) (1 + i * subWidth); x < (i + 1) * subWidth
					&& x < width - 1; ++x) {
				for (int y = 0; y < height; ++y) {
					if (img.getPixel(x, y) != colorMax) {
						img.setPixel(x, y, Color.WHITE);
					} else {
						img.setPixel(x, y, Color.BLACK);
					}
				}
			}
		}
		return img;
	}
	
	/**
	 * 取出图片的背景
	 * @param in
	 * @return BufferedImage
	 * @throws Exception
	 */
	private static Bitmap removeBackgroud(InputStream in) throws Exception {
		Bitmap img = BitmapFactory.decodeStream(in);
		img = Bitmap.createBitmap(img, 1, 1, img.getWidth()-2, img.getHeight()-2);
		int width = img.getWidth();
		int height = img.getHeight();
		double subWidth = width / 5.0;
		for (int i = 0; i < 5; i++) {
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (int x = (int) (1 + i * subWidth); x < (i + 1) * subWidth
					&& x < width - 1; ++x) {
				for (int y = 0; y < height; ++y) {
					if (isWhite(img.getPixel(x, y)) == 1)
						continue;
					if (map.containsKey(img.getPixel(x, y))) {
						map.put(img.getPixel(x, y), map.get(img.getPixel(x, y)) + 1);
					} else {
						map.put(img.getPixel(x, y), 1);
					}
				}
			}
			int max = 0;
			int colorMax = 0;
			for (Integer color : map.keySet()) {
				if (max < map.get(color)) {
					max = map.get(color);
					colorMax = color;
				}
			}
			for (int x = (int) (1 + i * subWidth); x < (i + 1) * subWidth
					&& x < width - 1; ++x) {
				for (int y = 0; y < height; ++y) {
					if (img.getPixel(x, y) != colorMax) {
						img.setPixel(x, y, Color.WHITE);
					} else {
						img.setPixel(x, y, Color.BLACK);
					}
				}
			}
		}
		return img;
	}
	
	/**
	 * 判断像素点是否是白色
	 * @param colorInt
	 * @return
	 */
	private static int isWhite(int colorInt) {
		if (Color.red(colorInt) + Color.green(colorInt) + Color.blue(colorInt) > 600) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 判断像素点是否是黑色
	 * @param colorInt
	 * @return
	 */
	private static int isBlack(int colorInt) {
		if (Color.red(colorInt) + Color.green(colorInt) + Color.blue(colorInt) <= 100) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 切割验证码图片到单个字符
	 * @param img
	 * @return List<BufferedImage>
	 * @throws Exception
	 */
	private static List<Bitmap> splitImage(Bitmap img) throws Exception {
		List<Bitmap> subImgs = new ArrayList<Bitmap>();
		int width = img.getWidth();
		int height = img.getHeight();
		List<Integer> weightlist = new ArrayList<Integer>();
		for (int x = 0; x < width; ++x) {
			int count = 0;
			for (int y = 0; y < height; ++y) {
				if (isBlack(img.getPixel(x, y)) == 1) {
					count++;
				}
			}
			weightlist.add(count);
		}
		for (int i = 0; i < weightlist.size();i++) {
			int length = 0;
			while (i < weightlist.size() && weightlist.get(i) > 0) {
				i++;
				length++;
			}
			if (length > 2) {
				subImgs.add(removeBlank(Bitmap.createBitmap(img, i - length, 0, length, height)));
			}
		}
		return subImgs;
	}
	
	/**
	 * 去除空白
	 * @param img
	 * @return BufferedImage
	 * @throws Exception
	 */
	private static Bitmap removeBlank(Bitmap img) throws Exception {
		int width = img.getWidth();
		int height = img.getHeight();
		int start = 0;
		int end = 0;
		Label1: for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				if (isBlack(img.getPixel(x, y)) == 1) {
					start = y;
					break Label1;
				}
			}
		}
		Label2: for (int y = height - 1; y >= 0; --y) {
			for (int x = 0; x < width; ++x) {
				if (isBlack(img.getPixel(x, y)) == 1) {
					end = y;
					break Label2;
				}
			}
		}
		return Bitmap.createBitmap(img, 0, start, width, end - start + 1);
	}
}
