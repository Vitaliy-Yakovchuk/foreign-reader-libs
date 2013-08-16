package com.reader.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = { TextSourceTest.class, DatabaseSpeedTest.class })
public class ReaderTests {

}
