/**
 * Created by yyz on 17/3/23.
 */

$(document).ready(function(){
    var HAP_EXTEND_OPERATION_APPLY_FUNCTION_REFER;

    HAP_EXTEND_OPERATION_APPLY_FUNCTION_REFER = setInterval(function () {
        try{
            eval(op_permission_code);
        }catch (err){
            clearInterval(HAP_EXTEND_OPERATION_APPLY_FUNCTION_REFER);
            // console.error(err);
            throw err;
        }
    },200);
});