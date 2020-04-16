package edu.whu.mTomcat.connector;

import com.alibaba.fastjson.JSON;
import edu.whu.mTomcat.util.RequestUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpRequest implements HttpServletRequest {
    private String contentType;
    private int contentLength;
    private InetAddress inetAddress;
    private InputStream input;
    private String method;
    private String protocol;
    private String queryString;
    private String requestURI;
    private String serverName;
    private int serverPort;
    private Socket socket;
    private boolean requestedSessionCookie;
    private String requestedSessionId;
    private boolean requestedSessionURL;
    private Map body;

    public void finishRequest() throws IOException {
        input.close();
    }

    protected HashMap attributes = new HashMap();
    /**
     * The authorization credentials sent with this Request.
     */
    protected String authorization = null;
    /**
     * The context path for this request.
     */
    protected String contextPath = "";
    /**
     * The set of cookies associated with this Request.
     */
    protected ArrayList cookies = new ArrayList();
    /**
     * An empty collection to use for returning empty Enumerations.  Do not
     * add any elements to this collection!
     */
    protected static ArrayList empty = new ArrayList();
    /**
     * The set of SimpleDateFormat formats to use in getDateHeader().
     */
    protected SimpleDateFormat formats[] = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)
    };

    /**
     * The HTTP headers associated with this Request, keyed by name.  The
     * values are ArrayLists of the corresponding header values.
     */
    protected HashMap headers = new HashMap();
    /**
     * The parsed parameters for this request.  This is populated only if
     * parameter information is requested via one of the
     * <code>getParameter()</code> family of method calls.  The key is the
     * parameter name, while the value is a String array of values for this
     * parameter.
     * <p>
     * <strong>IMPLEMENTATION NOTE</strong> - Once the parameters for a
     * particular request are parsed and stored here, they are not modified.
     * Therefore, application level access to the parameters need not be
     * synchronized.
     */
    protected ParameterMap parameters = null;

    /**
     * Have the parameters for this request been parsed yet?
     */
    protected boolean parsed = false;
    protected String pathInfo = null;

    /**
     * The reader that has been returned by <code>getReader</code>, if any.
     */
    protected BufferedReader reader = null;

    /**
     * The ServletInputStream that has been returned by
     * <code>getInputStream()</code>, if any.
     */
    protected ServletInputStream stream = null;

    public HttpRequest(InputStream input) {
        this.input = input;
    }

    public void addHeader(String name, String value) {
        name = name.toLowerCase();
        synchronized (headers) {
            ArrayList values = (ArrayList) headers.get(name);
            if (values == null) {
                values = new ArrayList();
                headers.put(name, values);
            }
            values.add(value);
        }
    }

    /**
     * Parse the parameters of this request, if it has not already occurred.
     * If parameters are present in both the query string and the request
     * content, they are merged.
     */
    protected void parseParameters() {
        if (parsed)
            return;
        ParameterMap results = parameters;
        if (results == null)
            results = new ParameterMap();
        results.setLocked(false);
        String encoding = getCharacterEncoding();
        if (encoding == null)
            encoding = "ISO-8859-1";

        // Parse any parameters specified in the query string
        String queryString = getQueryString();
        try {
            RequestUtil.parseParameters(results, queryString, encoding);
        }
        catch (UnsupportedEncodingException e) {
            ;
        }

        // Parse any parameters specified in the input stream
        String contentType = getContentType();
        if (contentType == null)
            contentType = "";
        if ("POST".equals(getMethod()) && (getContentLength() > 0)){
            try {
                if(false){
//                if(contentType.startsWith("multipart/form-data")){  // 处理文件上传
                    String boundary = contentType.substring(contentType.indexOf("boundary") +
                            "boundary=".length());
                    BufferedReader br = new BufferedReader(new InputStreamReader(input));
                    String str = null;
                    while((str = br.readLine()) != null){
                        //解析结束的标记
                        do {
                            //读取boundary中的内容
                            //读取Content-Disposition
                            str = br.readLine();
                            //说明是文件上传
                            if (str.indexOf("Content-Disposition:") >= 0 && str.indexOf("filename") > 0) {
                                str = str.substring("Content-Disposition:".length());
                                String[] strs = str.split(";");
                                String fileName = strs[strs.length - 1].replace("\"", "").split("=")[1];
                                System.out.println("fileName = " + fileName);
                                //这一行是Content-Type
                                br.readLine();
                                //这一行是换行
                                br.readLine();
                                //正式去读文件的内容
                                StringBuilder sb = new StringBuilder();
                                BufferedWriter bw = null;
                                try {
                                    bw = new BufferedWriter(new OutputStreamWriter(new
                                            FileOutputStream("/media/hbj/G/OtherCodes" +
                                            File.separator + fileName), "UTF-8"));
                                    while (true) {
                                        str = br.readLine();
                                        if (str.startsWith("--" + boundary)) {
                                            break;
                                        }
                                        sb.append(str).append("\n");
//                                        bw.write(str);
//                                        bw.newLine();
                                    }
                                    BufferedReader br2 = new BufferedReader(new InputStreamReader(
                                            new ByteArrayInputStream(sb.toString().getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
                                    String tmp = null;
                                    while((tmp = br2.readLine()) != null){
                                        bw.write(tmp);
                                        bw.newLine();
                                    }
                                    bw.flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (bw != null) {
                                        bw.close();
                                    }
                                }
                            }
                            if (str.indexOf("Content-Disposition:") >= 0) {
                                str = str.substring("Content-Disposition:".length());
                                String[] strs = str.split(";");
                                String name = strs[strs.length - 1].replace("\"", "").split("=")[1];
                                br.readLine();
                                StringBuilder stringBuilder = new StringBuilder();
                                while (true) {
                                    str = br.readLine();
                                    if (str.startsWith("--" + boundary)) {
                                        break;
                                    }
                                    stringBuilder.append(str);
                                }
                                parameters.put(name, stringBuilder.toString());
                            }
                        } while (("--" + boundary).equals(str));
                        //解析结束
                        if (str.equals("--" + boundary + "--")) {
                            break;
                        }
                    }
                } else {
                    int max = getContentLength();
                    int len = 0;
                    byte buf[] = new byte[getContentLength()];
                    ServletInputStream is = getInputStream();
                    while (len < max) {
                        int next = is.read(buf, len, max - len);
                        if (next < 0) {
                            break;
                        }
                        len += next;
                    }
                    is.close();
                    if (len < max) {
                        throw new RuntimeException("Content length mismatch");
                    }

                    Map body = new HashMap();
                    if ("application/x-www-form-urlencoded".equals(contentType)) {
                        RequestUtil.parseParameters(body, buf, encoding);
                    } else if (contentType.contains("application/json")) {
                        String s = new String(buf);
                        body = (Map) JSON.parse(s);
                    } else if(contentType.startsWith("multipart/form-data")){
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                new ByteArrayInputStream(buf), Charset.forName("utf8")));
                        String boundary = contentType.substring(contentType.indexOf("boundary") +
                                "boundary=".length());
//                        BufferedReader br = new BufferedReader(new InputStreamReader(input));
                        String str = null;
                        while((str = br.readLine()) != null){
                            //解析结束的标记
                            do {
                                //读取boundary中的内容
                                //读取Content-Disposition
                                str = br.readLine();
                                //说明是文件上传
                                if (str.indexOf("Content-Disposition:") >= 0 && str.indexOf("filename") > 0) {
                                    str = str.substring("Content-Disposition:".length());
                                    String[] strs = str.split(";");
                                    String fileName = strs[strs.length - 1].replace("\"", "").split("=")[1];
                                    System.out.println("fileName = " + fileName);
                                    //这一行是Content-Type
                                    str = br.readLine();
                                    System.out.println(str);
                                    //这一行是换行
                                    str = br.readLine();
                                    System.out.println(str);
                                    //正式去读文件的内容
                                    StringBuilder sb = new StringBuilder();
                                    BufferedWriter bw = null;
                                    try {
                                        bw = new BufferedWriter(new OutputStreamWriter(new
                                                FileOutputStream("/media/hbj/G/OtherCodes" +
                                                File.separator + fileName), "UTF-8"));
                                        while (true) {
                                            str = br.readLine();
                                            if (str.startsWith("--" + boundary)) {
                                                break;
                                            }
                                            sb.append(str).append("\n");
                                        bw.write(str);
                                        bw.newLine();
                                        }
                                        BufferedReader br2 = new BufferedReader(new InputStreamReader(
                                                new ByteArrayInputStream(sb.toString().getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
                                        String tmp = null;
                                        while((tmp = br2.readLine()) != null){
//                                            bw.write(tmp);
//                                            bw.newLine();
                                        }
                                        bw.flush();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (bw != null) {
                                            bw.close();
                                        }
                                    }
                                }
                                if (str.indexOf("Content-Disposition:") >= 0) {
                                    str = str.substring("Content-Disposition:".length());
                                    String[] strs = str.split(";");
                                    String name = strs[strs.length - 1].replace("\"", "").split("=")[1];
                                    br.readLine();
                                    StringBuilder stringBuilder = new StringBuilder();
                                    while (true) {
                                        str = br.readLine();
                                        if (str.startsWith("--" + boundary)) {
                                            break;
                                        }
                                        stringBuilder.append(str);
                                    }
                                    parameters.put(name, stringBuilder.toString());
                                }
                            } while (("--" + boundary).equals(str));
                            //解析结束
                            if (str.equals("--" + boundary + "--")) {
                                break;
                            }
                        }
                    }
                    setBody(body);
                }
            } catch (IOException e) {
                throw new RuntimeException("Content read fail");
            }

        }

        // Store the final results
        results.setLocked(true);
        parsed = true;
        parameters = results;
    }

    public void addCookie(Cookie cookie) {
        synchronized (cookies) {
            cookies.add(cookie);
        }
    }

    /**
     * Create and return a ServletInputStream to read the content
     * associated with this Request.  The default implementation creates an
     * instance of RequestStream associated with this request, but this can
     * be overridden if necessary.
     *
     * @exception IOException if an input/output error occurs
     */
    public ServletInputStream createInputStream() throws IOException {
        return (new RequestStream(this));
    }

    public InputStream getStream() {
        return input;
    }
    public void setContentLength(int length) {
        this.contentLength = length;
    }

    public void setContentType(String type) {
        this.contentType = type;
    }

    public void setInet(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public void setContextPath(String path) {
        if (path == null)
            this.contextPath = "";
        else
            this.contextPath = path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPathInfo(String path) {
        this.pathInfo = path;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }
    /**
     * Set the name of the server (virtual host) to process this request.
     *
     * @param name The server name
     */
    public void setServerName(String name) {
        this.serverName = name;
    }
    /**
     * Set the port number of the server to process this request.
     *
     * @param port The server port
     */
    public void setServerPort(int port) {
        this.serverPort = port;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Set a flag indicating whether or not the requested session ID for this
     * request came in through a cookie.  This is normally called by the
     * HTTP Connector, when it parses the request headers.
     *
     * @param flag The new flag
     */
    public void setRequestedSessionCookie(boolean flag) {
        this.requestedSessionCookie = flag;
    }

    public void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    public void setRequestedSessionURL(boolean flag) {
        requestedSessionURL = flag;
    }

    /* implementation of the HttpServletRequest*/
    public Object getAttribute(String name) {
        synchronized (attributes) {
            return (attributes.get(name));
        }
    }

    public Enumeration getAttributeNames() {
//        synchronized (attributes) {
//            return (new Enumerator(attributes.keySet()));
//        }
        return null;
    }

    public String getAuthType() {
        return null;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public int getContentLength() {
        return contentLength ;
    }

    public long getContentLengthLong() {
        return 0;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContextPath() {
        return contextPath;
    }

    public Cookie[] getCookies() {
        synchronized (cookies) {
            if (cookies.size() < 1)
                return (null);
            Cookie results[] = new Cookie[cookies.size()];
            return ((Cookie[]) cookies.toArray(results));
        }
    }

    public long getDateHeader(String name) {
        String value = getHeader(name);
        if (value == null)
            return (-1L);

        // Work around a bug in SimpleDateFormat in pre-JDK1.2b4
        // (Bug Parade bug #4106807)
        value += " ";

        // Attempt to convert the date header in a variety of formats
        for (int i = 0; i < formats.length; i++) {
            try {
                Date date = formats[i].parse(value);
                return (date.getTime());
            }
            catch (ParseException e) {
                ;
            }
        }
        throw new IllegalArgumentException(value);
    }

    public String getHeader(String name) {
        name = name.toLowerCase();
        synchronized (headers) {
            ArrayList values = (ArrayList) headers.get(name);
            if (values != null)
                return ((String) values.get(0));
            else
                return null;
        }
    }

    public Enumeration getHeaderNames() {
//        synchronized (headers) {
//            return (new Enumerator(headers.keySet()));
//        }
        return null;
    }

    public Enumeration getHeaders(String name) {
//        name = name.toLowerCase();
//        synchronized (headers) {
//            ArrayList values = (ArrayList) headers.get(name);
//            if (values != null)
//                return (new Enumerator(values));
//            else
//                return (new Enumerator(empty));
//        }
        return null;
    }

    public ServletInputStream getInputStream() throws IOException {
        if (reader != null)
            throw new IllegalStateException("getInputStream has been called");

        if (stream == null)
            stream = createInputStream();
        return (stream);
    }

    public int getIntHeader(String name) {
        String value = getHeader(name);
        if (value == null)
            return (-1);
        else
            return (Integer.parseInt(value));
    }

    public Locale getLocale() {
        return null;
    }

    public Enumeration getLocales() {
        return null;
    }

    public String getMethod() {
        return method;
    }

    public String getParameter(String name) {
        parseParameters();
        String values = (String)parameters.get(name);
        return values;
    }

    public Map getParameterMap() {
        parseParameters();
        return (this.parameters);
    }

    public Enumeration getParameterNames() {
        return null;
    }

    public String[] getParameterValues(String name) {
        return null;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getQueryString() {
        return queryString;
    }

    public BufferedReader getReader() throws IOException {
        if (stream != null)
            throw new IllegalStateException("getInputStream has been called.");
        if (reader == null) {
            String encoding = getCharacterEncoding();
            if (encoding == null)
                encoding = "ISO-8859-1";
            InputStreamReader isr =
                    new InputStreamReader(createInputStream(), encoding);
            reader = new BufferedReader(isr);
        }
        return (reader);
    }

    public String getRealPath(String path) {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public String getLocalName() {
        return null;
    }

    public String getLocalAddr() {
        return null;
    }

    public int getLocalPort() {
        return 0;
    }

    public ServletContext getServletContext() {
        return null;
    }

    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    public boolean isAsyncStarted() {
        return false;
    }

    public boolean isAsyncSupported() {
        return false;
    }

    public AsyncContext getAsyncContext() {
        return null;
    }

    public DispatcherType getDispatcherType() {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public String getRemoteUser() {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    public String getScheme() {
        return null;
    }

    public String getServerName() {
        return null;
    }

    public int getServerPort() {
        return 0;
    }

    public String getRequestedSessionId() {
        return requestedSessionId;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public HttpSession getSession() {
        return null;
    }

    public String changeSessionId() {
        return null;
    }

    public HttpSession getSession(boolean create) {
        return null;
    }

    public String getServletPath() {
        return null;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }

    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    public void login(String s, String s1) throws ServletException {

    }

    public void logout() throws ServletException {

    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isSecure() {
        return false;
    }

    public boolean isUserInRole(String role) {
        return false;
    }

    public void removeAttribute(String attribute) {
    }

    public void setAttribute(String key, Object value) {
    }

    /**
     * Set the authorization credentials sent with this request.
     *
     * @param authorization The new authorization credentials
     */
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException {
    }

    public Map getBody() {
        return body;
    }

    public void setBody(Map body) {
        this.body = body;
    }
}
