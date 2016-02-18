hep.cdf={};

document.addEventListener("DOMContentLoaded", function (){


    var getDaysArrayByGranularity = function (data, functionName, lowerDate, upperDate){

        //This contains a list of how many days to consider for each bar of the graph
        var dayArray = [];

        //Get the lowest date/month/year we are interested in
        var current = lowerDate[functionName]();

        //k is the index in the dayArray
        var k = 0;

        //j is the index of the data we are parsing.  Same function as i but different stepping
        var j = 0;
        var curDate = lowerDate;
        for (var i = lowerDate.getTime(); i < upperDate.getTime(); ) {

          //This is the date of the current day we are seeing
          curDate = new Date(i);

          //if we changed day/month/year then switch to another cell in the array
          if (current != curDate[functionName]() && i > lowerDate.getTime()) {
            k++;
          }

          //Add or sum the value
          dayArray[k] = (dayArray[k] ? dayArray[k] +1 : 1);

          current = curDate[functionName]();
          j++;
          curDate.setDate(curDate.getDate()+1);
          i = curDate.getTime();
        }

        return dayArray;
    };

       var getDaysArrayByWeek = function (data, lowerDate, upperDate){

            //This contains a list of how many days to consider for each bar of the graph
            var dayArray = [];

            //Weeks start on mondays
            var weekDelimiter = 1;

            //k is the index in the dayArray
            var k = 0;

            //j is the index of the data we are parsing.  Same function as i but different stepping
            var j = 0;
            var curDate = lowerDate;
            for (var i = lowerDate.getTime(); i < upperDate.getTime(); ) {

              //This is the date of the current day we are seeing
              curDate = new Date(i);

              //if we changed day/month/year then switch to another cell in the array
              if (weekDelimiter == curDate.getUTCDay() && i > lowerDate.getTime()) {
                k++;
              }

              //Add or sum the value
              dayArray[k] = (dayArray[k] ? dayArray[k] +1 : 1);

              j++;
              curDate.setDate(curDate.getDate()+1);
              i = curDate.getTime();
            }

            return dayArray;
      };

    //Returns list of how many days to consider for each bar of the graph
    var getDaysArray = function (data, granularity, lowerDate, upperDate){
        var dayArray = [];

        switch (granularity) {
          case "year":
            dayArray = getDaysArrayByGranularity(data,"getUTCFullYear", lowerDate, upperDate );
            break;
          case "month":
            dayArray = getDaysArrayByGranularity(data,"getUTCMonth", lowerDate, upperDate );
            break;
          case "day":
            dayArray = getDaysArrayByGranularity(data,"getUTCDate", lowerDate, upperDate );
            break;
          case "week":
            dayArray = getDaysArrayByWeek(data, lowerDate, upperDate );
            break;
        }

        return dayArray;
    };

    //Given how many days to sum for each bar of the graph, thus function will sum the value of each graph bar
    var applyGranularity = function(data,granularity, lowerDate, upperDate){
        var daysArray = getDaysArray(data.datasets[0].data,granularity, lowerDate, upperDate);
        var sumArray = [];
        var k = 0;
        for(var i = 0;i<daysArray.length;i++){
            data.labels.push(i+1);

            var valuesSum = 0;
            for(var j = 0;j<daysArray[i];j++, k++){
                var valueToSum = data.datasets[0].data[k];
                if(valueToSum){
                    valuesSum += valueToSum;
                }
            }
            sumArray.push(valuesSum);

        }
        data.datasets[0].data = sumArray;
        return data;
    };

    //Returns a chartjs bar graph datastore
    hep.cdf.chartsJsDataCreator = {
        getChartData : function(serverData, granularity, lowerDate, upperDate){
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

          return applyGranularity(data,granularity, lowerDate, upperDate);
        }
    };

});