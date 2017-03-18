package hap.extend.core.db


import com.hand.hap.liquibase.MigrationHelper

def mhi = MigrationHelper.getInstance()

databaseChangeLog(logicalFilePath:"hap/extend/core/db/2017-03-22-init-migration.groovy"){


    changeSet(author: "yazheng young", id: "20170322-yyz-1") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_RES_JS_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_RES_JS") {
            column(name:"JS_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_RES_JS_PK")
            }

            column(name: "JS_NAME", type: "VARCHAR(255)",remarks: "JS名称") {
                constraints(nullable: "false")
            }
            column(name: "DESCRIPTION", type: "VARCHAR(255)", remarks: "js作用描述") {
                constraints(nullable: "true")
            }
            column(name: "RESOURCE_ID", type: "BIGINT",remarks: "页面资源ID") {
                constraints(nullable: "false")
            }
            column(name: "JS_SCRIPT", type: "CLOB", remarks: "js脚本代码") {
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

    changeSet(author: "yazheng young", id: "20170322-yyz-2") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_RES_PRIV_ASSIGN_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_RES_PRIV_ASSIGN") {
            column(name:"ASSIGN_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键，权限类别ID"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_RES_PRIV_ASSIGN_PK")
            }

            column(name: "RESOURCE_ID", type: "BIGINT",remarks: "页面资源ID") {
                constraints(nullable: "false")
            }
            column(name: "ASSIGN_TYPE", type: "VARCHAR(30)",remarks: "分配类型，下拉ROLE或者USER=角色或者用户；此外GLOBAL=全局") {
                constraints(nullable: "false")
            }
            column(name: "TYPE_ID", type: "BIGINT", remarks: "ROLE_ID或USER_ID或全局ID10001(无特殊含义)") {
                constraints(nullable: "false")
            }
            column(name: "DESCRIPTION", type: "VARCHAR(255)", remarks: "js作用描述") {
                constraints(nullable: "true")
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

    changeSet(author: "yazheng young", id: "20170322-yyz-3") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_RES_JS_ASSIGN_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_RES_JS_ASSIGN") {
            column(name:"JS_ASSIGN_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_RES_JS_ASSIGN_PK")
            }

            column(name: "ASSIGN_ID", type: "BIGINT",remarks: "权限类别id") {
                constraints(nullable: "false")
            }
            column(name: "JS_ID", type: "BIGINT",remarks: "页面js代码id") {
                constraints(nullable: "false")
            }
            column(name: "DESCRIPTION", type: "VARCHAR(255)",remarks: "描述") {
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
