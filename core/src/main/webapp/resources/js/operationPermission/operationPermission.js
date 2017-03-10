/**
 * Created by yyz on 2017/3/8.
 */
$(document).ready(function(){
    // console.log(_basePath);
    // console.log(window.location.pathname);
    var _opt_pms_filePath = window.location.pathname.substring(_basePath.length);
    // console.log(_opt_pms_filePath);
    $.ajax({
        type: 'POST',
        url: _basePath+"/hap_extend/operation_permission/query" ,
        data:{filePath:_opt_pms_filePath},
        success: function (data) {
            console.log(data);
            if(!!data && !!data.rows && data.rows.length>0){
                eval(data.rows[0]["attribute1"]);
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
