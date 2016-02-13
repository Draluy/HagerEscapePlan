hep.sumvalues={};

document.addEventListener("DOMContentLoaded", function (){

    var refreshValues = function(){
        $("#RefreshValues").prop( "disabled", true);
        $.get("/sums/getSumTemplate", function( data ) {
           $("#sums").html(data);
           $("#RefreshValues").prop( "disabled", false);
        });
    };

    $("#RefreshValues").click(refreshValues);

    //Call once at the page loading
    refreshValues();
});
