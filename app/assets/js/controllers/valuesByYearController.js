hep.valuesbyyear={};

document.addEventListener("DOMContentLoaded", function (){

    var refreshValues = function(){
     $.get("/valuesbyyear/years", function( data ) {
            var docFragment = document.createDocumentFragment();
            for (var i = 0; i < data.length; i++) {
                var select = document.createElement("option");
                select.innerHTML = data[i];
                docFragment.appendChild(select);
            }
            $("#years").html(docFragment.childNodes);
        });
    };

    var enableControls = function(status){
        $("#years").prop("disabled", !status);
        $("#granularity").prop("disabled", !status);
        $("#go").prop("disabled", !status);
        if(status){
            $("#three .spinner").hide();
        }else{
            $("#three .spinner").show();
        }
    };

    //Add the canvas to the page, or get the context of the already included canvas
    var getCtx = function(){
        var ctx;
        var div = $("#valuesbyyeargraph").get(0);

        if (!$("#valuesbyyeargraph").html()){
            var canvas = document.createElement('canvas');
            canvas.width = div.clientWidth;
            canvas.height = div.clientHeight;
            $("#valuesbyyeargraph").html(canvas);

            ctx = canvas.getContext("2d");
        }else{
            ctx = $("#valuesbyyeargraph canvas")[0].getContext("2d");
        }
        return ctx;
    };

    var changeGraphValues = function(data,lowerDate, upperDate){


        //Add the canvas to the page. If not done dinamically, chrome barfs all over it
        var ctx = getCtx();

        var granularity = $("#granularity").val();

        if (hep.valuesbyyear.graphByYear){
            hep.valuesbyyear.graphByYear.destroy();
        }

        hep.valuesbyyear.graphByYear = Chart.Line(ctx, {
            data: hep.cdf.chartsJsDataCreator.getChartData(data, granularity, lowerDate, upperDate)
        });

    };

    //Its up the this function to display the graph by year and reenable the controls
    var displayGraphByYear  = function (year){

        var lowerDate = new Date(Date.UTC(parseInt(year),0,1));
        var upperDate = new Date(Date.UTC(parseInt(year)+1,0,1));
        if (hep.valuesbyyear.year === year){
            enableControls(true);
            changeGraphValues(hep.valuesbyyear.data, lowerDate, upperDate);
            return;
        }

        $.get("/valuesbyyear/"+year, function(data) {
            hep.valuesbyyear.year = year;
            hep.valuesbyyear.data = data;

            changeGraphValues(data, lowerDate, upperDate);
            enableControls(true);
        });
    };

    //Display the graph of a certain period and reenable the controls
    var displayGraphByPeriod  = function(lower, upper){
        $.post("/valuesbyperiod",{ lowerbound: lower, upperbound: upper })
        .done(function(data) {
            hep.valuesbyyear.data = data;

            changeGraphValues(data, new Date(lower), new Date(upper));
            enableControls(true);
        });
    };

    var displayGraph = function(){

        enableControls(false);

        if($('#yearradio').is(':checked')){
            var year = $("#years").val();
            displayGraphByYear(year);
        }else{
            var lower = $("#lowerbound").val();
            var upper = $("#upperbound").val();
            displayGraphByPeriod(lower, upper);
        }
    };

    var loadIfEmpty = function(){
        var nbItems = $('#years')[0].options.length;
        if (nbItems === 0){
            refreshValues();
        }
    };

    var disablePeriod = function(disable){
       return function(){
           $("#lowerbound").prop( "disabled", disable );
           $("#upperbound").prop( "disabled", disable );
           $("#years").prop( "disabled", !disable );
       };
    };

    $("#years").click(loadIfEmpty);
    $("#RefreshYearsValues").click(refreshValues);
    $("#go").click(displayGraph);
    $("#yearradio").click(disablePeriod(true));
    $("#periodradio").click(disablePeriod(false));
    //Call once at the page loading
    refreshValues();

});
