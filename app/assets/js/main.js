hep={};



(function (){
    //Take care of the websocket
    var ws = new WebSocket("ws://localhost:9000/ws");

    ws.onclose = function(e){
        console.log("onclose "+e);
        ws = new WebSocket("ws://localhost:9000/ws");
        ws.onerror = hep.ws.onerror;
        ws.onopen = hep.ws.onopen;
        ws.onmessage = hep.ws.onmessage;
     };
    ws.onerror = function(e){console.log("onerror "+e);};
    ws.onopen = function(e){console.log("onopen "+e);};

    //keep a reference for other modules
    hep.ws = ws;


    //call the polyfill for html5 forms
    webshim.polyfill('forms forms-ext');
})();