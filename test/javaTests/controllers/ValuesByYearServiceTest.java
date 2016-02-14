package javaTests.controllers;

import controllers.value.dao.ValueByYearDAO;
import controllers.year.ValuesByYearService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

/**
 * Created by dralu on 2/11/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ValuesByYearServiceTest {

    private ValuesByYearService valuesByYearService;

    @Mock
    ValueByYearDAO valueByYearDAO;

    @Before
    public void before(){

        valuesByYearService = new ValuesByYearService();
        Whitebox.setInternalState(valuesByYearService, "valueByYearDAO", valueByYearDAO);
    }

    @Test
    public void testGetYearsNoData(){
        final List<Integer> years = valuesByYearService.getYears();

        Assert.assertTrue(years.isEmpty());
    }

    @Test
    public void testGetYears(){
        Mockito.doReturn(new Long[]{1229212211641L,1355347540911L}).when(valueByYearDAO).getMinMaxTimestamps();
        final List<Integer> years = valuesByYearService.getYears();

        Assert.assertArrayEquals(new Integer[]{2008,2009,2010,2011,2012}, years.toArray());
    }
}
