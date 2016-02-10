hep.sumvalues={};

document.addEventListener("DOMContentLoaded", function (){

    var refreshValues = function(){
     $.get("/sums/getSumTemplate", function( data ) {
            $("#sums").html(data);
     });
    };

    $("#RefreshValues").click(refreshValues);

});
