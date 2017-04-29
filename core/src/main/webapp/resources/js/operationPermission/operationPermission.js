/**
 * Created by yyz on 2017/3/8.
 */

var op_permission_code="";
var _opt_pms_filePath = window.location.pathname.substring(_basePath.length+1);
$.ajax({
    type: 'POST',
    url: _basePath+"/hap_extend/operation_permission/query" ,
    data:{filePath:_opt_pms_filePath},
    success: function (data) {
        if(!!data && !!data.rows && data.rows.length>0){
            op_permission_code = data.rows[0]["name"];
        }else {
            throw Error("error when fetch op permission code");
        }
    },
    async:false,
    dataType: "json"
});

