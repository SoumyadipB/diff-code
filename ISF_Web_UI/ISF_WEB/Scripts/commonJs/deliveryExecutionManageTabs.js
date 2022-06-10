// Not use  any one can delete this one

//$(document).ready(function () {
//    clearInterval(idleInterval);
//    idleInterval = setInterval(DeliveryTimerIncrement, 8000); // 1 second  
//    //  extendSession();
//    //getAllSessionValues();
//});

//function formatDate(date) {
//    var hours = date.getHours();
//    var minutes = date.getMinutes();
//    var seconds = date.getSeconds();
//    var ampm = hours >= 12 ? 'pm' : 'am';
//    hours = hours % 12;
//    hours = hours ? hours : 12; // the hour '0' should be '12'
//    minutes = minutes < 10 ? '0' + minutes : minutes;
//    var strTime = hours + ':' + minutes + ':' + seconds + ' ' + ampm;
//    return (date.getMonth() + 1) + "/" + date.getDate() + "/" + date.getFullYear() + "  " + strTime;
//}


//var idleInterval = "";
//var workOrderIdParam = "";

////function DeliveryTimerIncrement() {
////    debugger;
////    var paramWoid = localStorage.getItem("clickedWOID");
////    var arrayLocalStorage = JSON.parse(localStorage.getItem("CreateEntry")) || [];

////    if (window.location.href.includes("DeliveryExecution")) {
////        if (paramWoid) {
////            var openWoTabsArray = arrayLocalStorage.filter(item => item.Woid === paramWoid);

////            var maxdateTime = new Date(Math.max.apply(null, openWoTabsArray.map(function (e) {
////                return new Date(e.CreatedTime);
////            })));
////            maxdateTime = formatDate(maxdateTime);
////            var maxConvertDateTime = new Date(maxdateTime);

////            var i;
////            for (i = 0; i < openWoTabsArray.length; i++) {
////                var converStoreDateTime = new Date(openWoTabsArray[i].CreatedTime);
////                if (converStoreDateTime < maxConvertDateTime) {
////                    if (openWoTabsArray.length > 1) {
////                        debugger;
////                        console.log(openWoTabsArray[i]);
////                        if (checkifshouldclose(document.location.href, openWoTabsArray[i].url)) {

////                            arrayLocalStorage.splice(arrayLocalStorage.findIndex(item => item.Key === openWoTabsArray[i].Key), 1);
////                            localStorage.setItem("CreateEntry", JSON.stringify(arrayLocalStorage));
////                            window.close();
////                        }

////                    }
////                }
////            }
////        }
////    }
////}

////function checkifshouldclose(url, storeUrl) {
////    if (url != storeUrl) {
////        return true;
////    } else {
////        return false;
////    }
////}


////function DeliveryTimerIncrement() {
////    debugger;
////    var paramWoid = localStorage.getItem("paramWoid");
////    var arrayLocalStorage = JSON.parse(localStorage.getItem("CreateEntry")) || [];

////    if (window.location.href.includes("DeliveryExecution")) {
////        if (paramWoid) {
////            var openWoTabsArray = arrayLocalStorage.filter(item => item.Woid === paramWoid);

////            var maxdateTime = new Date(Math.max.apply(null, openWoTabsArray.map(function (e) {
////                return new Date(e.CreatedTime);
////            })));
////            maxdateTime = formatDate(maxdateTime);
////            var i;
////            for (i = 0; i < openWoTabsArray.length; i++) {
////                if (openWoTabsArray[i].CreatedTime < maxdateTime) {
////                    if (openWoTabsArray.length > 1) {
////                        console.log(openWoTabsArray[i]);

////                        //arrayLocalStorage = arrayLocalStorage.splice(arrayLocalStorage.findIndex(item => item.Key === openWoTabsArray[i].Key), 1);
////                        //localStorage.setItem("CreateEntry", JSON.stringify(arrayLocalStorage));
////                        window.close();

////                    }
////                }
////            }
////        }
////    }
////}