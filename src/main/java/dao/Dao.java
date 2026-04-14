package dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Dao {
	// フィールド（データ、変数）
	// static
	// DB接続は、各テーブルで共通だから
	static DataSource ds;

	// コンストラクタ（初期化メソッド）：クラス名と同じ

	// メソッド（処理）
	public Connection getConnection() throws Exception {
		if (ds==null) {
			InitialContext ic = new InitialContext();
			ds = (DataSource)ic.lookup("java:comp/env/jdbc/scorebook");
		}
		return ds.getConnection();
	}
}