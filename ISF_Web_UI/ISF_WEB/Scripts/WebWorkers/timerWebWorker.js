self.onmessage = function (e) {
    startDateAdhoc = e.data.startDateAdhoc;
    startTimerWorker(e.data.elapsedTime);
}
let startDateAdhoc = "";

//Starts the timer
function startTimerWorker(elapsedTime) {
    let currentDateAdhoc = new Date();
    let diff = parseInt((currentDateAdhoc - startDateAdhoc) / 1000);
    var totalSeconds = elapsedTime + diff;
    var hours = Math.floor(totalSeconds / 3600);
    var minutes = Math.floor((totalSeconds - (hours * 3600)) / 60);
    var seconds = totalSeconds - (hours * 3600) - (minutes * 60);
    min = checkTime(minutes);
    sec = checkTime(seconds);
    hr = checkTime(hours);
    var finalTime = hr + " : " + min + " : " + sec;

    self.postMessage(finalTime);

    var timer = setTimeout(function () { startTimerWorker(elapsedTime) }, 1000); 

}

//Validates values in time which are less than 10
function checkTime(i) {
    if (i < 10)
        i = "0" + i;
    return i;
}