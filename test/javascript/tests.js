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
    var data = hep.cdf.chartsJsDataCreator.getChartData();

    assert.deepEqual([],data.labels);
    assert.deepEqual([],data.datasets[0].data);
});


var testData = [1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 10, 1, 1, 1, 1, 0, 0, 0, 10, 10, 10];

QUnit.test( "getChartDataYear", function( assert ) {
    var lower = 1222120800000;
    var upper = lower + 86400000 * 40;
    hep.valuesbyyear.year = "2008";
    var data = hep.cdf.chartsJsDataCreator.getChartData(
    testData,"year", new Date(lower), new Date(upper)
    );

    assert.deepEqual([1],data.labels);
    assert.deepEqual([77],data.datasets[0].data);
});

QUnit.test( "getChartDataMonth", function( assert ) {
    hep.valuesbyyear.year = "2008";
    var lower = 1222120800000;
    var upper = lower + 86400000 * 45;
    var data = hep.cdf.chartsJsDataCreator.getChartData(
    testData,"month", new Date(lower), new Date(upper)
    );

    assert.deepEqual([1,2,3],data.labels);
    assert.deepEqual([12,65,30],data.datasets[0].data);
});

QUnit.test( "getChartDataWeek", function( assert ) {
    hep.valuesbyyear.year = "2008";
    var lower = 1222120800000;
    var upper = lower + 86400000 * 45;
    var data = hep.cdf.chartsJsDataCreator.getChartData(
    testData,"week", new Date(lower), new Date(upper)
    );

    assert.deepEqual([1,2,3,4,5,6,7],data.labels);
    assert.deepEqual([8,15,21,14,16,13,20],data.datasets[0].data);
});
