/**
 * Created by yyz on 17/3/23.
 */

$(document).ready(function(){
    var HAP_EXTEND_OPERATION_APPLY_FUNCTION_POINTER = 0;
    var HAP_EXTEND_OPERATION_APPLY_FUNCTION_REFER;

    HAP_EXTEND_OPERATION_APPLY_FUNCTION_REFER = setInterval(function () {
        try{
            eval(op_permission_code);
        }catch (error){
            HAP_EXTEND_OPERATION_APPLY_FUNCTION_POINTER=250;
            console.error(error);
        }
        HAP_EXTEND_OPERATION_APPLY_FUNCTION_POINTER ++;
        if(HAP_EXTEND_OPERATION_APPLY_FUNCTION_POINTER > 250){
            clearInterval(HAP_EXTEND_OPERATION_APPLY_FUNCTION_REFER);
        }
    },200);
});