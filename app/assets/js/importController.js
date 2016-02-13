hep.import={};

document.addEventListener("DOMContentLoaded", function (){
    var startImport = function(e){
      $("#StartImport").hide();
      $("#PauseImport").show();
          $.get( "import/start", function( data ) {
        $("#ImportProgress")[0].value = data;
        console.log("Import finished with "+data+" lines imported.");
      });
    };

    var pauseImport = function(e){
      $("#PauseImport").hide();
      $("#StartImport").show();
      $.get( "import/pause", function( data ) {
        console.log("Import paused at line "+data);
      });
    };

    var resetImport = function(e){
      $("#ResetImport").prop( "disabled", true );
      $.get( "import/reset", function( data ) {
        $("#ImportProgress")[0].value = data;
        $("#ResetImport").prop( "disabled", false );
        console.log("Import resetted");
      });
    };

    hep.ws.onmessage = function(mesg){
      $("#ImportProgress")[0].value = mesg.data;
    };

    $("#StartImport").prop( "disabled", true);
    $("#ResetImport").prop( "disabled", true);

    $.get("/import/nbLines", function( data ) {
        $("#ImportProgress")[0].max = data;
    });

    //Iniitialize the current import state
//    $.get("/import/nbCurrentLines", function( data ) {
        $("#StartImport").prop( "disabled", false);
        $("#ResetImport").prop( "disabled", false);
//      $("#ImportProgress")[0].value = data;
//    });

    $("#StartImport").click(startImport);
    $("#PauseImport").click(pauseImport);
    $("#ResetImport").click(resetImport);
});