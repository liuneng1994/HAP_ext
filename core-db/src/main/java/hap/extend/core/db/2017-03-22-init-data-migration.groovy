package hap.extend.core.db


import com.hand.hap.liquibase.MigrationHelper
import com.hand.hap.db.excel.ExcelDataLoader

def mhi = MigrationHelper.getInstance()
dbType = MigrationHelper.getInstance().dbType()

databaseChangeLog(logicalFilePath:"hap/extend/core/db/2017-03-22-init-data.groovy"){


//    changeSet(author: "yazheng young", id: "20170303-yyz-0") {
//            //sqlFile(path: MigrationHelper.getInstance().dataPath("com/hand/hap/db/data/"+dbType+"/demo.sql"), encoding: "UTF-8")
//    }

    changeSet(author: "yazheng young", id: "20170322-init-data-xlsx", runAlways:"true"){
        customChange(class:ExcelDataLoader.class.name){
            param(name:"filePath",value:MigrationHelper.getInstance().dataPath("hap/extend/core/db/data/2017-03-22-init-data.xlsx"))
        }
    }
}
