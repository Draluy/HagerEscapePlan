hep={};

(function (){
    var ws = new WebSocket("ws://localhost:9000/ws");
    hep.ws = ws;
})();