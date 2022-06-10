function checkNodeExist(arrNodes) {
    let chkFlag = false;
    const arNodeTypeExist = arrNodes.find(x => x.nodeType !== '');
    const arNodeNamesExist = arrNodes.find(x => x.nodeNames !== '');
    const arMarketExist = arrNodes.find(x => x.market !== '');
    if (arNodeTypeExist || arNodeNamesExist || arMarketExist) {
        chkFlag = true;
    }
    return chkFlag;
}
function getNoDataRow() {
    return '<tr><td colspan="11" style="text-align:center">No data found.</td></tr>';
}

