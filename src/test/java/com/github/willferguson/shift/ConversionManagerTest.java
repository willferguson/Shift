package com.github.willferguson.shift;

import com.github.willferguson.shift.conversion.ConversionManager;
import com.github.willferguson.shift.conversion.ConversionManagerImpl;
import com.github.willferguson.shift.path.SimplePathFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by will on 16/03/16.
 */
public class ConversionManagerTest {

    ConversionManager<Integer, String> conversionManager;

    @Before
    public void setup() {
        conversionManager = new ConversionManagerImpl<>(new SimplePathFinder<>());
    }

    @Test
    public void testConversion() {
        Converter<String> converter1 = str -> "Version 2";
        Converter<String> converter2 = str -> "Version 3";
        conversionManager.addConverter(1, 2, converter1);
        conversionManager.addConverter(2, 3, converter2);

        List<Converter<String>> converters = conversionManager.findConversionChain(1, 3);
        Assert.assertTrue(converters.size() == 2);
        Assert.assertEquals(converter1, converters.get(0));
        Assert.assertEquals(converter2, converters.get(1));
    }
}
