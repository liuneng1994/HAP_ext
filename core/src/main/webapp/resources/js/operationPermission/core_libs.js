/**
 * Forbidden modifying,all right reserved.
 * Created on 2017/3/10.
 * @author young
 */

// =====================GRID OPERATION===============================================start
/**
 * extract grid Object from id
 * @param _divId
 * @author young
 * @returns {*|jQuery}
 */
function gridUtils_gridFromDivId(param_divId) {
    return $("#"+param_divId).data("kendoGrid");
}

function gridUtils_setTopLevelOptions(param_gridDivId, param_key, param_value) {
    var ops = {};
    ops[""+param_key] = param_value;
    // gridUtils_gridFromDivId(param_gridDivId).setOptions({param_key:param_value}); already fixed
    gridUtils_gridFromDivId(param_gridDivId).setOptions(ops);
}
/**
 * allow user to edit the whole grid
 * @author young
 * @param param_gridDivId
 */
function gridUtils_allowEditWholeGrid(param_gridDivId) {
    gridUtils_setTopLevelOptions(param_gridDivId,"editable",true);
}

/**
 * forbidden editing the whole grid
 * @author young
 * @param param_gridDivId
 */
function gridUtils_forbidEditWholeGrid(param_gridDivId) {
    gridUtils_setTopLevelOptions(param_gridDivId,"editable",false);
}

/**
 * switch off editing mode of columns
 * @author young
 * @param param_gridDivId
 * @param param_columnIndexArray array,example:[0,1,2].index starts from 0.
 */
function gridUtils_forbidEditColumns(param_gridDivId, param_columnIndexArray) {
    var _thisGrid = gridUtils_gridFromDivId(param_gridDivId);
    _thisGrid.OP_PMS_forbidEditColumns=[];
    $.each(param_columnIndexArray, function (key, ele) {
        _thisGrid.OP_PMS_forbidEditColumns.push(ele);
    });
    gridUtils_setTopLevelOptions(param_gridDivId,"edit",function (e) {
        // make compatible for old edit function if exists
        var localGrid = gridUtils_gridFromDivId(param_gridDivId);
        if(!localGrid || !localGrid.OP_PMS_forbidEditColumns){
            return;
        }else if(localGrid.OP_PMS_forbidEditColumns===[]){
            return;
        }else {
            var nowIndex = e.container[0].cellIndex;
            if(localGrid.OP_PMS_forbidEditColumns.indexOf(nowIndex) > -1){
                e.container.children().attr("readonly","readonly");
                // e.container.children().attr("disabled","disabled");//need not consider for nested or customized editor
                e.container.children().click(function (event) {
                    event.preventDefault();
                });
            }
        }
    });
}

/**
 * hide columns of given indexes.
 * @param param_gridDivId
 * @param param_columnIndexArray array of column index,the index start from 0.
 */
function gridUtils_hideColumns(param_gridDivId, param_columnIndexArray) {
    console.log(param_columnIndexArray);
    var _thisGrid = gridUtils_gridFromDivId(param_gridDivId);
    var columns = _thisGrid.columns;
    var totalWidth = _thisGrid.table[0].clientWidth;
    var totalWeight = 0;
    var counter = -1;
    $.each(columns, function (key, ele) {
        counter ++;
        if(param_columnIndexArray.indexOf(counter) > -1){
            ele["hap_ext_op_permission"]="hide";
        }else if(!ele["hap_ext_op_permission"]){
            totalWeight += ele["width"];
        }
    });

    $.each(columns, function (key, ele) {
        if(!ele["hap_ext_op_permission"]){
            ele["width"] = (totalWidth/totalWeight)*ele["width"];
        }
    });

    $.each(param_columnIndexArray, function (key, _index) {
        _thisGrid.hideColumn(_index);
    });
}

/**
 * flexible controller of nested components applied in grid cell
 * @author young
 * @param param_htmlTagName html tag name,such as:a、input、button
 * @param param_tagAttributeName tag attribute name,such as:name、class;you can define your own attribute as well,for example:OP_PMS_name
 * @param param_tagAttributeValue value of tag attribute
 * @param param_callbackFunction js callback function,for example: function(ele){ele.attr("disabled","disabled");}，ele is a js object which will be injected automatically represented for this nested obj
 */
function gridUtils_controlNestedComponents(param_htmlTagName, param_tagAttributeName, param_tagAttributeValue, param_callbackFunction) {
    var temp = param_htmlTagName+"["+param_tagAttributeName+"='"+param_tagAttributeValue+"']";
    $.each($(temp), function (key, value) {
        param_callbackFunction($(value));
    });
}

/**
 * disable buttons in single column
 * @author young
 * @param param_tagAttributeValue value of "OP_PMS_name" attribute
 */
function gridUtils_disableButtonInColumn(param_tagAttributeValue) {
    gridUtils_controlNestedComponents("a","OP_PMS_name",param_tagAttributeValue,
        function (ele) {
            ele.attr("disabled","disabled");
            ele.unbind();
            ele.prop("onclick",null);
            ele.click(function (event) {
                event.preventDefault();
            });
        }
    );
}

// =====================GRID OPERATION================================================end