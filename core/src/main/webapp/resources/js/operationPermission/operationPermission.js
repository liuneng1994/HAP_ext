/**
 * Created by yyz on 2017/3/8.
 */
$(document).ready(function(){
    var _opt_pms_filePath = window.location.pathname.substring(_basePath.length+1);
    console.log(_opt_pms_filePath);
    $.ajax({
        type: 'POST',
        url: _basePath+"/hap_extend/operation_permission/query" ,
        data:{filePath:_opt_pms_filePath},
        success: function (data) {
            console.log(data);
            if(!!data && !!data.rows && data.rows.length>0){
                console.log(data.rows[0]["attribute1"]);
                eval(data.rows[0]["attribute1"]);
                // eval("hideColumn_1();");
                // console.log("执行完毕");
            }
        },
        async:false,
        dataType: "json"
    });
});
//
// function disableCpn(cpn_id) {
//     $("#"+cpn_id).attr("disabled",true);
// }
