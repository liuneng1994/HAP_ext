/**
 * Created by yyz on 2017/3/8.
 */
$(document).ready(function(){
    var _opt_pms_filePath = window.location.pathname.substring(_basePath.length+1);
    $.ajax({
        type: 'POST',
        url: _basePath+"/hap_extend/operation_permission/query" ,
        data:{filePath:_opt_pms_filePath},
        success: function (data) {
            if(!!data && !!data.rows && data.rows.length>0){
                eval(data.rows[0]["name"]);
            }
        },
        async:false,
        dataType: "json"
    });
});
