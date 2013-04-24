package com.jmcloud.compute.sys;

import java.io.File;
import java.util.Properties;

import com.jmcloud.compute.util.SysUtils;

public class SystemEnviroment {

	private static final Properties sysEnvProperty = SysUtils
			.getProperties("sys/sys-env.properties");

	private static final String installDir = new File(
			SysUtils.getResourceURI("./")).getAbsolutePath()
			+ "/";

	private static String getString(String key) {
		return sysEnvProperty.getProperty(key);
	}
	
	private static String getInstallDirPlusString(String key){
		return installDir + getString(key);
	}

	public static String getImagesDir() {
		return getString("IMAGES_DIR");
	}

	public static String getFlagImagesDir() {
		return getString("FLAG_IMAGES_DIR");
	}

	public static String getSpringConfPath() {
		return getString("SPRING_CONF_PATH");
	}

	public static String getRegionCityMappingPath() {
		return getString("REGION_CITY_MAPPING_PATH");
	}

	public static String getRegionContryMappingPath() {
		return getString("REGION_CONTRY_MAPPING_PATH");
	}

	public static String getDefaultEC2EnvPath() {
		return getString("DEFAULT_EC2_ENV_PATH");
	}

	public static String getUserEC2EnvPath() {
		return getInstallDirPlusString("USER_EC2_ENV_PATH");
	}

	public static String getKeypairDir() {
		return getInstallDirPlusString("KEY_PAIR_DIR");
	}

	public static String getDataSaveDir() {
		return getInstallDirPlusString("DATA_SAVE_DIR");
	}

	public static String getUserDir() {
		return installDir;
	}
}
