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

    var getChartData = function(serverData){
        var labels = [];

        for (i = 0; i < serverData.length; i++) {
            labels.push(i+1);
        }

        var data = {
            labels: labels,
            datasets: [
                {
                    label: "Values by day",
                    backgroundColor: "rgba(0,0,0,0.5)",
                    borderColor: "rgba(0,0,0,0.8)",
                    hoverBackgroundColor: "rgba(0,0,0,0.75)",
                    hoverBorderColor: "rgba(0,0,0,1)",
                    data: serverData
                }
            ]
        };
        return data;
    };

    var displayGraph = function(){
        var year = $("#years").val();

        $("#three .spinner").show();
        $("#years").prop("disabled", true);

        $.get("/valuesbyyear/"+year, function(data) {
            var canvas = $("#valuesbyyeargraph").get(0);
            var ctx = canvas.getContext("2d");
            if (hep.valuesbyyear.graphByYear){
                hep.valuesbyyear.graphByYear.destroy();
            }
            hep.valuesbyyear.graphByYear = Chart.Bar(ctx, {
                data: getChartData(data)
            });

            $("#three .spinner").hide();
            $("#years").prop("disabled", false);
        });
    };

    $("#RefreshYearsValues").click(refreshValues);
    $("#years").change(displayGraph);

    //Call once at the page loading
    refreshValues();
});
