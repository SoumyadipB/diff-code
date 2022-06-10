
var window = self;
let isDataReceived = false;
importScripts('paho.js');

//stores all the open clients
let browserInstances = [];

let counter = 0;
let signumGlobal, topicName, apiResponse, UiRootDir, environment, neTopicName;
var MQTT_KEY = "n4HTbJEmsR";

function initiateMqttOverWs() {

    //clientID in the format signum_web
    let clientID = signumGlobal + '_' + environment + '_' + getBrowserType();
    console.log(clientID);

    let client = new Paho.MQTT.Client(decrypt(apiResponse.hostNameMQTT), 443, "/ws/mqtt", clientID);

    client.onConnectionLost = onConnectionLost;
    client.onMessageArrived = onMessageArrived;

    var options = {
        useSSL: true,
        userName: decrypt(apiResponse.userName),
        password: decrypt(apiResponse.password),
        onSuccess: onConnect,
        onFailure: doFail,
        //willMessage: lastWill,
        keepAliveInterval: 30,
        cleanSession: false,
        reconnect: true
    }

    // connect the client
    client.connect(options);

    // when the client connects
    function onConnect() {

        // Once a connection has been made, make a subscription and send a message.
        console.log("onConnect");
        let subscribeOptions = {
            qos: 1
        };

        console.log(topicName);
        console.log(neTopicName);

        //subscribe to the topic after connection 
        if (!topicName) {
            console.log("Invalid topic, cannot subscribe. ");
        }
        else {
            client.subscribe(topicName, subscribeOptions);
            client.subscribe(neTopicName, subscribeOptions);

        }

    }

    // when connection fails
    function doFail(e) {
        console.log(e);
    }

    // called when the client loses its connection
    function onConnectionLost(responseObject) {
        console.log(new Date());
        if (responseObject.errorCode !== 0) {
            console.log(responseObject.errorMessage);
        }
    }

    //method called everytime the message arrives
    function onMessageArrived(message) {

        console.log("onMessageArrived:" + message.payloadString);
        browserInstances.map(instance => {
            instance.postMessage(message.payloadString);
        });
    }
}

//method called everytime for new connection
onconnect = function (e) {

    //the current port address
    const port = e.ports[0];

    //all the open port addresses
    browserInstances.push(port);
   
    port.onmessage = function (event) {
        if (!isDataReceived) {
            topicName = event.data[0];
            UiRootDir = event.data[1];
            environment = event.data[2];
            neTopicName = event.data[3];
            getMQTTConnectionDetails();
            initiateMqttOverWs();
            isDataReceived = true;
        }
    }
}

// get browser type
function getBrowserType() {
    const { userAgent } = navigator;

    if (userAgent.includes('Firefox/')) {
        return 'Firefox';
    }
    else if (userAgent.includes('Edg/')) {
        return 'Edge';
    }
    else if (userAgent.includes('Chrome/')) {
        return 'Chrome';
    }
    else if (userAgent.includes('Safari/')) {
        return 'Safari';
    }
}

// .net API to get mqtt connection details
function getMQTTConnectionDetails() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var serverResponse = this.responseText;
            apiResponse = JSON.parse(serverResponse);
            signumGlobal = apiResponse.signum;
        }
    };
    xhr.open('POST', UiRootDir + '/Data/getMqttDataDetails', false);
    xhr.setRequestHeader('content-type', 'application/json; charset=utf-8');
    xhr.setRequestHeader('mqttToken', MQTT_KEY);
    xhr.send();
}


function decrypt(value) {
    let newValue = value.substring(4, value.length - 1);
    let index = newValue.indexOf("*");
    let final = newValue.substring(0, index)
    final = final.split("").reverse().join("");
    return final;
}