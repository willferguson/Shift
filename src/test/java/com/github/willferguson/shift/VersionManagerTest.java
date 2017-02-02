package com.github.willferguson.shift;

import com.github.willferguson.shift.conversion.ConversionManager;
import com.github.willferguson.shift.conversion.ConversionManagerImpl;
import com.github.willferguson.shift.exceptions.NoValidConversionPath;
import com.github.willferguson.shift.path.SimplePathFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by will on 16/03/16.
 */
public class VersionManagerTest {

    VersionManager<Integer, String> versionManager;
    @Before
    public void setup() {
        ConversionManager<Integer, String> conversionManager = new ConversionManagerImpl<>(new SimplePathFinder<>());
        versionManager = new VersionManagerImpl<>(conversionManager);

        versionManager.addConverter(1, 2, str -> "Version 2");
        versionManager.addConverter(2, 3, str -> "Version 3");
        versionManager.addConverter(3, 4, str -> "Version 4", 4);

        versionManager.addVersionDetectionFunction(str -> {
            switch (str) {
                case "Version 1":
                    return 1;
                case "Version 2":
                    return 2;
                case "Version 3":
                    return 3;
                case "Version 4":
                    return 4;
                default:
                    throw new UnsupportedOperationException();
            }
        });

    }

    @Test
    public void testValidConversion() {
        String version1 = "Version1";
        String version2 = versionManager.convert(1, 3, version1);
        Assert.assertTrue(version2.equals("Version 3"));
    }

    @Test(expected = NoValidConversionPath.class)
    public void testInvalidConversion() {
        String version1 = "Version1";
        versionManager.convert(1, 7, version1);
    }

    @Test
    public void testVersionDetection() {
        String version4 = versionManager.convert(4, "Version 2");
        Assert.assertEquals("Version 4", version4);
    }

    @Test
    public void testConvertToLatest() {
        String version1 = "Version 1";
        String latest = versionManager.convertToLatest(version1);
        Assert.assertEquals("Version 4", latest);
    }



}
