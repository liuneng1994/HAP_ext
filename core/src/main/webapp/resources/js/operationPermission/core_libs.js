/**
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
    gridUtils_gridFromDivId(param_gridDivId).setOptions({param_key:param_value});
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
 * @param param_columnIdsArray array,example:[0,1,2].index starts from 0.
 */
function gridUtils_forbidEditColumns(param_gridDivId, param_columnIdsArray) {
    var _thisGrid = gridUtils_gridFromDivId(param_gridDivId);
    var columns = _thisGrid.columns;
    var totalWidth = _thisGrid.table[0].clientWidth;
    var totalWeight = 0;
    var counter = -1;
    $.each(columns, function (key, ele) {
        counter ++;
        if(param_columnIdsArray.indexOf(counter) > -1){
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

    $.each(param_columnIdsArray, function (key, _index) {
        window.parent.grid.hideColumn(_index);
    });
}
/**
 * flexible controller of nested components applied in grid cell
 * @author young
 * @param param_htmlTagName html tag name,such as:a、input、button
 * @param param_tagAttributeName tag attribute name,such as:name、class;you can define your own attribute as well,for example:OP_PMS_name
 * @param param_tagAttributeValue value of tag attribute
 * @param param_callbackFunction js callback function,for example: function(ele){ele.attr("disabled","disabled");}
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
            ele.click(function (event) {
                event.preventDefault();
            });
        }
    );
}

// =====================GRID OPERATION================================================end