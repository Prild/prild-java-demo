package com.thinkgem.jeesite.modules.inxedu.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

public class VideoPlayTokenUtils {

	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(VideoPlayTokenUtils.class);

	/**
	 * HmacSHA1消息摘要
	 * 
	 * @param encryptText
	 *            待做摘要处理的数据
	 * @param encryptKey
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
		// 根据给定的字节数组构造一个用HmacSHA1算法加密的密钥。
		SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(), MAC_NAME);
		// 实例化
		Mac mac = Mac.getInstance(MAC_NAME);
		// 初始化Mac
		mac.init(secretKey);
		// 执行消息摘要处理
		return mac.doFinal(encryptText.getBytes());
	}

	public static String createPlayToken(String fid, String SecretKey, String AccessKey) {
		if (fid == null || fid.length() == 0) {
			return "";
		}
		long deadline = System.currentTimeMillis() / 1000 + 3600;
		String token = "id=" + fid + "&deadline=" + deadline + "&enablehtml5=1";
		String encoded = new BASE64Encoder().encode(token.getBytes());
		encoded = encoded.replaceAll("\r|\n", "");
		String encodeSign = "";
		byte[] sign = null;
		try {
			sign = HmacSHA1Encrypt(encoded, SecretKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		encodeSign = new BASE64Encoder().encode(sign);
		token = AccessKey + ":" + encodeSign + ":" + encoded;
		try {
			token = URLEncoder.encode(token, ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.info("UTF-8 not supported. " + e);
			e.printStackTrace();
		}
		return token;
	}
}
