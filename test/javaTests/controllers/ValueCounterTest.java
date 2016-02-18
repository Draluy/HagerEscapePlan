package javaTests.controllers;

import com.avaje.ebean.Model;
import controllers.sum.ValueCounter;
import models.Sum;
import models.Value;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import play.test.WithApplication;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

/**
 * Created by dralu on 2/18/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ValueCounterTest {

    ValueCounter valueCounter;

    @Mock
    Model.Finder<String, Sum> finder;

    @Mock
    ValueCounter.SumFactory sumFactory;

    @Before
    public void before() {
        valueCounter = new ValueCounter();
        setInternalState(valueCounter, "finder", finder);
        setInternalState(valueCounter, "sumFactory", sumFactory);
    }

    @Test
    public void testSaveValue() {
        assertTrue(valueCounter.getSumValuesMap().isEmpty());

        final Value val = new Value(1L, 2L, "country");
        valueCounter.saveValue(val);

        assertEquals(1, valueCounter.getSumValuesMap().size());
        assertEquals("country", valueCounter.getSumValuesMap().entrySet().iterator().next().getKey());
    }


    @Test
    public void testgetSumValues() {
        valueCounter.getSumValues();

        verify(finder).all();
    }

    @Test
    public void testdoPeriodically() {
        final String country = "country";
        final Value val = new Value(1L, 2L, country);

        Sum sum = mock(Sum.class);
        Mockito.doReturn(sum).when(sumFactory).getSum(country, 2L);

        valueCounter.saveValue(val);
        valueCounter.doPeriodically();

        verify(sum).insert();
    }

    @Test
    public void testdoPeriodicallySeveralValues() {
        final String country = "country";
        final Value val = new Value(1L, 2L, country);
        final Value val2 = new Value(6L, 3L, country);

        Sum sum = mock(Sum.class);
        Sum sumFound = mock(Sum.class);
        Mockito.doReturn(country).when(sum).getCountry();
        Mockito.when(finder.byId(Mockito.anyString())).thenReturn(sumFound);
        Mockito.doReturn(sum).when(sumFactory).getSum(eq(country), eq(5L));

        valueCounter.saveValue(val);
        valueCounter.saveValue(val2);
        valueCounter.doPeriodically();

        verify(sumFound).update();
    }

}
