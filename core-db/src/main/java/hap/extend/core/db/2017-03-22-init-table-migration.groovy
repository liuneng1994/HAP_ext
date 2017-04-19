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
            column(name: "ENABLE_JS", type: "VARCHAR(2)",defaultValue:"N", remarks: "Y=启用或者N=不启用N,是否启用个性化js控制") {
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


    changeSet(author: "yazheng young", id: "20170414-yyz-1") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_RES_COMPONENT_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_RES_COMPONENT") {
            column(name:"COMPONENT_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_RES_COMPONENT_PK")
            }

            column(name: "RESOURCE_ID", type: "BIGINT",remarks: "页面资源id") {
                constraints(nullable: "false")
            }
            column(name: "COMPONENT_NAME", type: "VARCHAR(255)",remarks: "组件名称") {
                constraints(nullable: "false")
            }
            column(name: "CPN_LEVEL", type: "VARCHAR(30)",remarks: "组件大的类别，分为FORM和GRID") {
                constraints(nullable: "false")
            }
            column(name: "COMPONENT_TYPE", type: "VARCHAR(50)",remarks: "组件类型") {
                constraints(nullable: "false")
            }
            column(name: "HTML_TAG_ATTR", type: "VARCHAR(30)",remarks: "HTML标签的属性") {
                constraints(nullable: "false")
            }
            column(name: "HTML_TAG_ATTR_VAL", type: "VARCHAR(128)",remarks: "HTML标签的属性值") {
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
    changeSet(author: "yazheng young", id: "20170414-yyz-2") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_RES_COMPONENT_ASSIGN_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_RES_COMPONENT_ASSIGN") {
            column(name:"COMPONENT_ASSIGN_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_RES_COMPONENT_ASSIGN_PK")
            }

            column(name: "ASSIGN_ID", type: "BIGINT",remarks: "页面权限分配ID") {
                constraints(nullable: "false")
            }
            column(name: "COMPONENT_ID", type: "BIGINT",remarks: "资源组件ID") {
                constraints(nullable: "false")
            }

            column(name: "DISPLAY", type: "VARCHAR(1)",remarks: "可见") {
                constraints(nullable: "false")
            }
            column(name: "REQUIRED", type: "VARCHAR(1)",remarks: "必输") {
                constraints(nullable: "false")
            }
            column(name: "READ_ONLY", type: "VARCHAR(1)",remarks: "只读") {
                constraints(nullable: "false")
            }
            column(name: "DISABLE", type: "VARCHAR(1)",remarks: "失效") {
                constraints(nullable: "false")
            }
            column(name: "DESCRIPTION", type: "VARCHAR(255)",remarks: "描述") {
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
    changeSet(author: "yazheng young", id: "20170414-yyz-3") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_RES_GRID_COLUMN_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_RES_GRID_COLUMN") {
            column(name:"CPN_COLUMN_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_RES_GRID_COLUMN_PK")
            }

            column(name: "COMPONENT_ID", type: "BIGINT",remarks: "组件id") {
                constraints(nullable: "false")
            }
            column(name: "COLUMN_NAME", type: "VARCHAR(255)",remarks: "列名称") {
                constraints(nullable: "false")
            }
            column(name: "COLUMN_INDEX", type: "BIGINT",remarks: "列下标，从0开始") {
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
    changeSet(author: "yazheng young", id: "20170414-yyz-4") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HCOM_RES_COLUMN_ASSIGN_S', startValue:"10001")
        }
        createTable(tableName: "HCOM_RES_COLUMN_ASSIGN") {
            column(name:"COLUMN_ASSIGN_ID",type:"BIGINT",autoIncrement: "true", startWith:"10001",remarks: "表ID，主键"){
                constraints(nullable: "false", primaryKey: "true",primaryKeyName: "HCOM_RES_COLUMN_ASSIGN_PK")
            }

            column(name: "COMPONENT_ASSIGN_ID", type: "BIGINT",remarks: "权限类别中当前列所在grid组件的分配id") {
                constraints(nullable: "false")
            }
            column(name: "CPN_COLUMN_ID", type: "BIGINT",remarks: "列id") {
                constraints(nullable: "false")
            }

            column(name: "DISPLAY", type: "VARCHAR(1)",remarks: "可见") {
                constraints(nullable: "false")
            }
            column(name: "REQUIRED", type: "VARCHAR(1)",remarks: "必输，目前只作为冗余字段没有实际意义") {
                constraints(nullable: "false")
            }
            column(name: "READ_ONLY", type: "VARCHAR(1)",remarks: "只读") {
                constraints(nullable: "false")
            }
            column(name: "DISABLE", type: "VARCHAR(1)",remarks: "失效，目前只作为冗余字段没有实际意义") {
                constraints(nullable: "false")
            }
            column(name: "DESCRIPTION", type: "VARCHAR(255)",remarks: "描述") {
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
}
