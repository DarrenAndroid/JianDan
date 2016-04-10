package com.socks;

/**
 * Created by zhaokaiqiang on 15/5/11.
 */

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * 用来为GreenDao框架生成Dao文件
 */
public class MyDaoGenerator {

	public static final String DAO_PATH = "../app/src/main/java-gen";
	public static final String PACKAGE_NAME = "com.socks.greendao";
	public static final int DATA_VERSION_CODE = 1;

	public static void main(String[] args) throws Exception {

		Schema schema = new Schema(DATA_VERSION_CODE, PACKAGE_NAME);
		addCache(schema, "JokeCache");
		addCache(schema, "FreshNewsCache");
		addCache(schema, "PictureCache");
		addCache(schema, "SisterCache");
		addCache(schema, "VideoCache");
		new DaoGenerator().generateAll(schema, DAO_PATH);

	}

	/**
	 * 添加不同的缓存表
	 * @param schema
	 * @param tableName
	 */
	private static void addCache(Schema schema, String tableName) {

		Entity joke = schema.addEntity(tableName);

		joke.addIdProperty().primaryKey().autoincrement();
		joke.addStringProperty("result");
		joke.addIntProperty("page");
		joke.addLongProperty("time");

	}

}