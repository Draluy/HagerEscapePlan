hep.valuesbyyear={};

document.addEventListener("DOMContentLoaded", function (){

    var numberOfDays = function (year, month) {
        var d = new Date(year, month, 0);
        return d.getDate();
    };

    var isLeapYear = function(year){
        return new Date(year, 1, 29).getMonth() == 1;
    };

    var getNbDays = function(year){
        var nbDays = 365;
        if(isLeapYear(year)){
            nbDays = 366;
        }
        return nbDays;
    };

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

    var getDaysArray = function (year, granularity){
        var daysArray = [];
        var i;
        var nbDays;
        switch (granularity) {
            case "day":
               nbDays = getNbDays(year);
               for(i = 0;i<nbDays;i++){
                   daysArray.push(1);
               }
            break;
            case "week":
                var weeksInYear = 52;
                for(i = 0;i<weeksInYear;i++){
                    daysArray.push(7);
                }
                nbDays = getNbDays(year);
                daysArray.push(nbDays-(weeksInYear*7));
            break;
            case "month":
                for(i = 0;i<12;i++){
                    daysArray.push(numberOfDays(year,i));
                }
            break;
            case "year":
                daysArray.push(getNbDays(year));
            break;
        }
        return daysArray;
    };

    var applyGranularity = function(data,granularity){
        var daysArray = getDaysArray(hep.valuesbyyear.year,granularity);
        var sumArray = [];
        var k = 0;
        for(var i = 0;i<daysArray.length;i++){
            data.labels.push(i+1);

            var valuesSum = 0;
            for(var j = 0;j<daysArray[i];j++, k++){
                valuesSum += data.datasets[0].data[k];
            }
            sumArray.push(valuesSum);

        }
        data.datasets[0].data = sumArray;
        return data;
    };

    var getChartData = function(serverData, granularity){
        var data = {
            labels: [],
            datasets: [
                {
                    label: "Values by "+granularity,
                    backgroundColor: "rgba(255,255,255,0.5)",
                    borderColor: "rgba(255,255,255,0.8)",
                    hoverBackgroundColor: "rgba(255,255,255,0.75)",
                    hoverBorderColor: "rgba(255,255,255,1)",
                    data: serverData
                }
            ]
        };

        return applyGranularity(data,granularity);
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

    var changeGraphValues = function(data){
        var canvas = $("#valuesbyyeargraph").get(0);
        var ctx = canvas.getContext("2d");
        var granularity = $("#granularity").val();

        if (hep.valuesbyyear.graphByYear){
            hep.valuesbyyear.graphByYear.destroy();
        }
        hep.valuesbyyear.graphByYear = Chart.Bar(ctx, {
            data: getChartData(data, granularity)
        });
    };

    var displayGraph = function(){
        var year = $("#years").val();

        enableControls(false);

        if (hep.valuesbyyear.year === year){
            enableControls(true);
            changeGraphValues(hep.valuesbyyear.data);
            return;
        }

        $.get("/valuesbyyear/"+year, function(data) {
            hep.valuesbyyear.year = year;
            hep.valuesbyyear.data = data;

            changeGraphValues(data);
            enableControls(true);
        });
    };

    $("#RefreshYearsValues").click(refreshValues);
    $("#go").click(displayGraph);

    //Call once at the page loading
    refreshValues();
});
