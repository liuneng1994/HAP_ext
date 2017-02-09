//package HapSwitchUser.core.db

import com.hand.hap.liquibase.MigrationHelper

//表结构
databaseChangeLog(logicalFilePath: "HapSwitchUser/core/db/liquibase.groovy") {
    def migrationHelper = MigrationHelper.getInstance()
    migrationHelper.dbmigrate.delegate = delegate
    //上面的内容不需要改动,三个参数的含义分别是
    //"mysql"表示数据库类型
    //["com/hand/hap"] 表示本次要执行脚本的扫描路径,可以添加多个
    //["table", "data"] 表示在建表的同时初始化数据
    def dbType = "mysql";
    def dburl = System.properties.getProperty("db.url");
    dburl = dburl == null ? "" : dburl;
    if (dburl.startsWith("jdbc:oracle")) {
        dbType = "oracle";
    } else if (dburl.startsWith("jdbc:sqlserver")) {
        dbType = "sqlserver";
    }
    // 现在支持自动根据 db.url 参数检测数据库类型
    migrationHelper.dbmigrate(dbType,["com/hand/hap","HapSwitchUser/core"] ,["table", "data", "patch"])

}