hep.valuesbyyear={};

document.addEventListener("DOMContentLoaded", function (){

    //Create an inner object that creates the datastore of the graph
    (function (){
        var getDaysArray = function (year, granularity){
            var daysArray = [];
            var i;
            var nbDays;
            switch (granularity) {
                case "day":
                   nbDays = hep.dateutils.nbDaysInYear(year);
                   for(i = 0;i<nbDays;i++){
                       daysArray.push(1);
                   }
                break;
                case "week":
                    var weeksInYear = 52;
                    for(i = 0;i<weeksInYear;i++){
                        daysArray.push(7);
                    }
                    nbDays = hep.dateutils.nbDaysInYear(year);
                    daysArray.push(nbDays-(weeksInYear*7));
                break;
                case "month":
                    for(i = 0;i<12;i++){
                        daysArray.push(hep.dateutils.nbDaysInMonth(year,i));
                    }
                break;
                case "year":
                    daysArray.push(hep.dateutils.nbDaysInYear(year));
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

        hep.valuesbyyear.chartsJsDataCreator = {
            getChartData : function(serverData, granularity){
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
            }
        };
    })();

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

    var changeGraphValues = function(data){
        var canvas = $("#valuesbyyeargraph").get(0);
        var ctx = canvas.getContext("2d");
        var granularity = $("#granularity").val();

        if (hep.valuesbyyear.graphByYear){
            hep.valuesbyyear.graphByYear.destroy();
        }
        hep.valuesbyyear.graphByYear = Chart.Bar(ctx, {
            data: hep.valuesbyyear.chartsJsDataCreator.getChartData(data, granularity)
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

    var loadIfEmpty = function(){
        var nbItems = $('#years')[0].options.length;
        if (nbItems === 0){
            refreshValues();
        }
    };

    $("#years").click(loadIfEmpty);
    $("#RefreshYearsValues").click(refreshValues);
    $("#go").click(displayGraph);

    //Call once at the page loading
    refreshValues();

});
