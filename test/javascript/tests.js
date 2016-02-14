QUnit.module( "Date Utils" );
QUnit.test( "numberOfDays", function( assert ) {
  assert.equal(29 , hep.dateutils.nbDaysInMonth(2008, 2));
  assert.equal(29 , hep.dateutils.nbDaysInMonth(2012, 2));
  assert.equal(28 , hep.dateutils.nbDaysInMonth(2009, 2));
  assert.equal(28 , hep.dateutils.nbDaysInMonth(2015, 2));
});

QUnit.test( "nbDaysInYear", function( assert ) {
  assert.equal(366 , hep.dateutils.nbDaysInYear(2008));
  assert.equal(366 , hep.dateutils.nbDaysInYear(2012));
  assert.equal(365 , hep.dateutils.nbDaysInYear(2009));
  assert.equal(365 , hep.dateutils.nbDaysInYear(2015));
});

QUnit.module( "chartsJsDataCreator" );
QUnit.test( "getChartData with no data", function( assert ) {
    var data = hep.valuesbyyear.chartsJsDataCreator.getChartData();

    assert.deepEqual([],data.labels);
    assert.deepEqual([],data.datasets[0].data);
});

QUnit.test( "getChartData with no data", function( assert ) {
    var data = hep.valuesbyyear.chartsJsDataCreator.getChartData();

    assert.deepEqual([],data.labels);
    assert.deepEqual([],data.datasets[0].data);
});

var testData = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,14774,2150010,2138082,2128987,2146474,2134652,2132464,2144688,2130300,2148432];

QUnit.test( "getChartDataYear", function( assert ) {
    hep.valuesbyyear.year = "2008";
    var data = hep.valuesbyyear.chartsJsDataCreator.getChartData(
    testData,"year"
    );

    assert.deepEqual([1],data.labels);
    assert.deepEqual([19268863],data.datasets[0].data);
});

QUnit.test( "getChartDataMonth", function( assert ) {
    hep.valuesbyyear.year = "2008";
    var data = hep.valuesbyyear.chartsJsDataCreator.getChartData(
    testData,"month"
    );

    assert.deepEqual([1,2,3,4,5,6,7,8,9,10,11,12],data.labels);
    assert.deepEqual([0,0,0,0,0,0,0,0,0,0,0,19268863],data.datasets[0].data);
});

