//Bind console log here
const DEBUG = conf.DEBUG;
let cl = function () {  // Access console.log
    if (DEBUG) {
        console.log.apply(console, arguments);
    }   
};

let clw = function () { // Access console.warning
    console.warn.apply(console, arguments);    
};

let clToText = function () {// Download console data onto a text file
    if (DEBUG) {
        console.log.apply(console, arguments);
        downloadConsoleInToTextFile('console.text', JSON.stringify(arguments));
    }
};


function downloadConsoleInToTextFile(filename, text) {
    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    element.setAttribute('download', filename);

    element.style.display = 'none';
    document.body.appendChild(element);

    element.click();
    document.body.removeChild(element);
}