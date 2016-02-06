hep.import={};

document.addEventListener("DOMContentLoaded", function (){
    var  importStartPoint = 0;

    var startImport = function(e){
      $.get( "import/"+importStartPoint, function( data ) {

      });
    };

    $("#StartImport").click(startImport);
});