package com.nihao001.sso.common.filter;

import java.lang.reflect.Method;

import javax.servlet.FilterConfig;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class SsoFilterTest {

    public void setUp() throws Exception {

    }
    
    private SsoFilter getFilterWithConfig1() throws Exception{
        FilterConfig config = EasyMock.mock(FilterConfig.class);
        EasyMock.expect(config.getInitParameter("NOT_CHECK_URL_LIST")).andReturn("/path1/*/path2");
        EasyMock.replay(config);
        SsoFilter ssoFileter = new SsoFilter();
        ssoFileter.init(config);
        return ssoFileter;
    }
    
    private SsoFilter getFilterWithConfig2() throws Exception{
        FilterConfig config = EasyMock.mock(FilterConfig.class);
        EasyMock.expect(config.getInitParameter("NOT_CHECK_URL_LIST")).andReturn("/path1/*");
        EasyMock.replay(config);
        SsoFilter ssoFileter = new SsoFilter();
        ssoFileter.init(config);
        return ssoFileter;
    }
    
    private SsoFilter getFilterWithConfig3() throws Exception{
        FilterConfig config = EasyMock.mock(FilterConfig.class);
        EasyMock.expect(config.getInitParameter("NOT_CHECK_URL_LIST")).andReturn("/*");
        EasyMock.replay(config);
        SsoFilter ssoFileter = new SsoFilter();
        ssoFileter.init(config);
        return ssoFileter;
    }
    
    private SsoFilter getFilterWithConfig4() throws Exception{
        FilterConfig config = EasyMock.mock(FilterConfig.class);
        EasyMock.expect(config.getInitParameter("NOT_CHECK_URL_LIST")).andReturn("/**");
        EasyMock.replay(config);
        SsoFilter ssoFileter = new SsoFilter();
        ssoFileter.init(config);
        return ssoFileter;
    }
    
    
    private Boolean invokeCheck(SsoFilter ssoFileter , String url) throws Exception{
        Method testNoParamMethod = ssoFileter.getClass().getDeclaredMethod("checkUri", String.class);
        testNoParamMethod.setAccessible(true);
        return  (Boolean)testNoParamMethod.invoke(ssoFileter, url);
    }

    @Test
    public void testConfig1_1() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig1();
        Boolean ret = invokeCheck(ssoFileter, "/path1/asdf/path2");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig1_2() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig1();
        Boolean ret = invokeCheck(ssoFileter, "/path1/123123/path2");
        Assert.assertTrue((Boolean)ret);
    }

    
    @Test
    public void testConfig1_3() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig1();
        Boolean ret = invokeCheck(ssoFileter, "/path1/asdf32324/path2");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig1_4() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig1();
        Boolean ret = invokeCheck(ssoFileter, "/path1/asdf32324/path2.js");
        Assert.assertFalse((Boolean)ret);
    }
    
    @Test
    public void testConfig1_5() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig1();
        Boolean ret = invokeCheck(ssoFileter, "/pat/asdf32324/path2.js");
        Assert.assertFalse((Boolean)ret);
    }
    
    @Test
    public void testConfig2_1() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig2();
        Boolean ret = invokeCheck(ssoFileter, "/path1/asfef");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig2_2() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig2();
        Boolean ret = invokeCheck(ssoFileter, "/path1/23444");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig2_3() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig2();
        Boolean ret = invokeCheck(ssoFileter, "/path1/asdfee23444");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig2_4() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig2();
        Boolean ret = invokeCheck(ssoFileter, "/path1/asdfee23444.js");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig2_5() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig2();
        Boolean ret = invokeCheck(ssoFileter, "/path1/asdfee23444.css");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig2_6() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig2();
        Boolean ret = invokeCheck(ssoFileter, "/path2/asdfee23444.css");
        Assert.assertFalse((Boolean)ret);
    }
    
    @Test
    public void testConfig2_7() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig2();
        Boolean ret = invokeCheck(ssoFileter, "/path1");
        Assert.assertFalse((Boolean)ret);
    }
    
    
    @Test
    public void testConfig3_1() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig3();
        Boolean ret = invokeCheck(ssoFileter, "/path1");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig3_2() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig3();
        Boolean ret = invokeCheck(ssoFileter, "/abcee");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig3_3() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig3();
        Boolean ret = invokeCheck(ssoFileter, "/123123");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig3_4() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig3();
        Boolean ret = invokeCheck(ssoFileter, "/123123.js");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig3_5() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig3();
        Boolean ret = invokeCheck(ssoFileter, "/123123.css");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig3_6() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig3();
        Boolean ret = invokeCheck(ssoFileter, "/1231/asdf/234.html");
        Assert.assertFalse((Boolean)ret);
    }
    
    @Test
    public void testConfig4_1() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig4();
        Boolean ret = invokeCheck(ssoFileter, "/1231/asdf/234.html");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig4_2() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig4();
        Boolean ret = invokeCheck(ssoFileter, "/234.html");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig4_5() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig4();
        Boolean ret = invokeCheck(ssoFileter, "/test");
        Assert.assertTrue((Boolean)ret);
    }
    
    @Test
    public void testConfig4_6() throws Exception {
        SsoFilter ssoFileter = getFilterWithConfig4();
        Boolean ret = invokeCheck(ssoFileter, "/");
        Assert.assertFalse((Boolean)ret);
    }
}
