hep.import={};

document.addEventListener("DOMContentLoaded", function (){
    var  importStartPoint = 0;

    var startImport = function(e){
      $.get( "import/"+importStartPoint, function( data ) {
        console.log("Import finished with "+data+" lines imported.");
      });
    };

    var pauseImport = function(e){
      $.get( "import/pause", function( data ) {
        console.log("Import paused at line "+data);
      });
    };

    hep.ws.onmessage = function(mesg){
      $("#ImportProgress")[0].value = mesg.data;
    };

    $.get("/import/nbLines", function( data ) {
        $("#ImportProgress")[0].max = data;
    });

    $("#StartImport").click(startImport);
    $("#PauseImport").click(pauseImport);
});