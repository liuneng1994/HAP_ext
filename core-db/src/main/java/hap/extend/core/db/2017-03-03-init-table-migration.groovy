package hap.extend.core.db


import com.hand.hap.liquibase.MigrationHelper

def mhi = MigrationHelper.getInstance()

databaseChangeLog(logicalFilePath:"hap/extend/core/db/2017-03-03-init-migration.groovy"){


    changeSet(author: "yazheng young", id: "20170303-yyz-1") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_DATA_RULES_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_DATA_RULES") {
            column(name:"RULE_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_DATA_RULES_PK")
            }

            column(name: "RULE_NAME", type: "VARCHAR(100)",remarks: "规则名称") {
                constraints(nullable: "false")
            }
            column(name: "RULE_SQL", type: "CLOB", remarks: "规则SQL") {
                constraints(nullable: "false")
            }
            column(name: "DESCRIPTION", type: "VARCHAR(255)", remarks: "规则说明") {
                constraints(nullable: "true")
            }
            column(name: "INCLUDE_FLAG", type: "VARCHAR(2)",defaultValue:"Y", remarks: "Y=包含或者N=排除") {
                constraints(nullable: "false")
            }
            column(name: "ENABLE_FLAG", type: "VARCHAR(2)",defaultValue:"Y", remarks: "Y=启用或者N=不启用") {
                constraints(nullable: "false")
            }

            column(name:"OBJECT_VERSION_NUMBER",type:"BIGINT",defaultValue: "1")
            column(name: "REQUEST_ID", type: "bigint", defaultValue : "-1")
            column(name: "PROGRAM_ID", type: "bigint", defaultValue : "-1")
            column(name: "CREATED_BY", type: "bigint", defaultValue : "-1")
            column(name: "CREATION_DATE", type: "datetime", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "bigint", defaultValue : "-1")
            column(name: "LAST_UPDATE_DATE", type: "datetime", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATE_LOGIN", type: "bigint", defaultValue : "-1")
            column(name:"ATTRIBUTE_CATEGORY",type:"varchar(30)")
            column(name:"ATTRIBUTE1",type:"varchar(240)")
            column(name:"ATTRIBUTE2",type:"varchar(240)")
            column(name:"ATTRIBUTE3",type:"varchar(240)")
            column(name:"ATTRIBUTE4",type:"varchar(240)")
            column(name:"ATTRIBUTE5",type:"varchar(240)")
            column(name:"ATTRIBUTE6",type:"varchar(240)")
            column(name:"ATTRIBUTE7",type:"varchar(240)")
            column(name:"ATTRIBUTE8",type:"varchar(240)")
            column(name:"ATTRIBUTE9",type:"varchar(240)")
            column(name:"ATTRIBUTE10",type:"varchar(240)")
            column(name:"ATTRIBUTE11",type:"varchar(240)")
            column(name:"ATTRIBUTE12",type:"varchar(240)")
            column(name:"ATTRIBUTE13",type:"varchar(240)")
            column(name:"ATTRIBUTE14",type:"varchar(240)")
            column(name:"ATTRIBUTE15",type:"varchar(240)")
        }

    }

    changeSet(author: "yazheng young", id: "20170303-yyz-2") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_DATA_RULE_ASSIGN_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_DATA_RULE_ASSIGN") {
            column(name:"ASSIGN_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_DATA_RULE_ASSIGN_PK")
            }

            column(name:"RULE_ID",type:"BIGINT",remarks: "规则ID，非空"){
                constraints(nullable: "false")
            }
            column(name: "ASSIGN_TYPE", type: "VARCHAR(30)",remarks: "分配类型，下拉ROLE或者USER=角色或者用户") {
                constraints(nullable: "false")
            }
            column(name: "TYPE_ID", type: "BIGINT", remarks: "ROLE_ID或USER_ID") {
                constraints(nullable: "false")
            }

            column(name:"OBJECT_VERSION_NUMBER",type:"BIGINT",defaultValue: "1")
            column(name: "REQUEST_ID", type: "bigint", defaultValue : "-1")
            column(name: "PROGRAM_ID", type: "bigint", defaultValue : "-1")
            column(name: "CREATED_BY", type: "bigint", defaultValue : "-1")
            column(name: "CREATION_DATE", type: "datetime", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "bigint", defaultValue : "-1")
            column(name: "LAST_UPDATE_DATE", type: "datetime", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATE_LOGIN", type: "bigint", defaultValue : "-1")
            column(name:"ATTRIBUTE_CATEGORY",type:"varchar(30)")
            column(name:"ATTRIBUTE1",type:"varchar(240)")
            column(name:"ATTRIBUTE2",type:"varchar(240)")
            column(name:"ATTRIBUTE3",type:"varchar(240)")
            column(name:"ATTRIBUTE4",type:"varchar(240)")
            column(name:"ATTRIBUTE5",type:"varchar(240)")
            column(name:"ATTRIBUTE6",type:"varchar(240)")
            column(name:"ATTRIBUTE7",type:"varchar(240)")
            column(name:"ATTRIBUTE8",type:"varchar(240)")
            column(name:"ATTRIBUTE9",type:"varchar(240)")
            column(name:"ATTRIBUTE10",type:"varchar(240)")
            column(name:"ATTRIBUTE11",type:"varchar(240)")
            column(name:"ATTRIBUTE12",type:"varchar(240)")
            column(name:"ATTRIBUTE13",type:"varchar(240)")
            column(name:"ATTRIBUTE14",type:"varchar(240)")
            column(name:"ATTRIBUTE15",type:"varchar(240)")
        }

    }

    changeSet(author: "yazheng young", id: "20170303-yyz-3") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_SQL_RULE_HEADER_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_SQL_RULE_HEADER") {
            column(name:"HEADER_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_SQL_RULE_HEADER_PK")
            }

            column(name: "SQLID_CODE", type: "CLOB", remarks: "SQLID编码值,即mapper查询方法的全路径") {
                constraints(nullable: "false")
            }
            column(name: "DESCRIPTION", type: "VARCHAR(255)",remarks: "描述") {
                constraints(nullable: "false")
            }

            column(name:"OBJECT_VERSION_NUMBER",type:"BIGINT",defaultValue: "1")
            column(name: "REQUEST_ID", type: "bigint", defaultValue : "-1")
            column(name: "PROGRAM_ID", type: "bigint", defaultValue : "-1")
            column(name: "CREATED_BY", type: "bigint", defaultValue : "-1")
            column(name: "CREATION_DATE", type: "datetime", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "bigint", defaultValue : "-1")
            column(name: "LAST_UPDATE_DATE", type: "datetime", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATE_LOGIN", type: "bigint", defaultValue : "-1")
            column(name:"ATTRIBUTE_CATEGORY",type:"varchar(30)")
            column(name:"ATTRIBUTE1",type:"varchar(240)")
            column(name:"ATTRIBUTE2",type:"varchar(240)")
            column(name:"ATTRIBUTE3",type:"varchar(240)")
            column(name:"ATTRIBUTE4",type:"varchar(240)")
            column(name:"ATTRIBUTE5",type:"varchar(240)")
            column(name:"ATTRIBUTE6",type:"varchar(240)")
            column(name:"ATTRIBUTE7",type:"varchar(240)")
            column(name:"ATTRIBUTE8",type:"varchar(240)")
            column(name:"ATTRIBUTE9",type:"varchar(240)")
            column(name:"ATTRIBUTE10",type:"varchar(240)")
            column(name:"ATTRIBUTE11",type:"varchar(240)")
            column(name:"ATTRIBUTE12",type:"varchar(240)")
            column(name:"ATTRIBUTE13",type:"varchar(240)")
            column(name:"ATTRIBUTE14",type:"varchar(240)")
            column(name:"ATTRIBUTE15",type:"varchar(240)")
        }

    }

    changeSet(author: "yazheng young", id: "20170303-yyz-4") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_SQL_RULE_LINES_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_SQL_RULE_LINES") {
            column(name:"LINE_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_SQL_RULE_LINES_PK")
            }

            column(name:"HEADER_ID",type:"BIGINT",remarks: "匹配头ID"){
                constraints(nullable: "false")
            }
            column(name:"RULE_ID",type:"BIGINT",remarks: "规则ID，非空"){
                constraints(nullable: "false")
            }
            column(name: "ENABLE_FLAG", type: "VARCHAR(2)",defaultValue:"Y", remarks: "Y=启用或者N=不启用") {
                constraints(nullable: "false")
            }

            column(name:"OBJECT_VERSION_NUMBER",type:"BIGINT",defaultValue: "1")
            column(name: "REQUEST_ID", type: "bigint", defaultValue : "-1")
            column(name: "PROGRAM_ID", type: "bigint", defaultValue : "-1")
            column(name: "CREATED_BY", type: "bigint", defaultValue : "-1")
            column(name: "CREATION_DATE", type: "datetime", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "bigint", defaultValue : "-1")
            column(name: "LAST_UPDATE_DATE", type: "datetime", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATE_LOGIN", type: "bigint", defaultValue : "-1")
            column(name:"ATTRIBUTE_CATEGORY",type:"varchar(30)")
            column(name:"ATTRIBUTE1",type:"varchar(240)")
            column(name:"ATTRIBUTE2",type:"varchar(240)")
            column(name:"ATTRIBUTE3",type:"varchar(240)")
            column(name:"ATTRIBUTE4",type:"varchar(240)")
            column(name:"ATTRIBUTE5",type:"varchar(240)")
            column(name:"ATTRIBUTE6",type:"varchar(240)")
            column(name:"ATTRIBUTE7",type:"varchar(240)")
            column(name:"ATTRIBUTE8",type:"varchar(240)")
            column(name:"ATTRIBUTE9",type:"varchar(240)")
            column(name:"ATTRIBUTE10",type:"varchar(240)")
            column(name:"ATTRIBUTE11",type:"varchar(240)")
            column(name:"ATTRIBUTE12",type:"varchar(240)")
            column(name:"ATTRIBUTE13",type:"varchar(240)")
            column(name:"ATTRIBUTE14",type:"varchar(240)")
            column(name:"ATTRIBUTE15",type:"varchar(240)")
        }

    }
}
