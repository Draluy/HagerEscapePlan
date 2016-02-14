hep.dateutils={};

(function(){
   //private -------------------------------------------
   var isLeapYear = function(year){
       return new Date(year, 1, 29).getMonth() == 1;
   };

   //public  -------------------------------------------
   var numberOfDays = function (year, month) {
        var d = new Date(year, month, 0);
        return d.getDate();
   };

   var getNbDays = function(year){
       var nbDays = 365;
       if(isLeapYear(year)){
           nbDays = 366;
       }
       return nbDays;
   };

    hep.dateutils.nbDaysInMonth = numberOfDays;
    hep.dateutils.nbDaysInYear = getNbDays;
})();
