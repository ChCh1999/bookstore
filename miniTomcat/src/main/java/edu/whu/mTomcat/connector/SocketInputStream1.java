package edu.whu.mTomcat.connector;

import java.io.*;

public class SocketInputStream1 extends InputStream {
    private static final byte CR = (byte) '\r';
    private static final byte LF = (byte) '\n';
    private static final byte SP = (byte) ' ';
    private static final byte HT = (byte) '\t';
    private static final byte COLON = (byte) ':';
    private static final int LC_OFFSET = 'A' - 'a';
    protected byte buf[];
    protected int count;
    protected int pos;
    protected InputStream is;
    private int bufferSize;

    public SocketInputStream1(InputStream is, int bufferSize) {
        this.is = is;
        buf = new byte[bufferSize];
        this.bufferSize = bufferSize;
    }

    public String readLine() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        int ch = 0;
        int status = 0;
        StringBuilder sb = new StringBuilder();
        while(true){
            ch = is.read();
            if(ch == -1) break;
            sb.append((char)ch);
            if(ch== CR && status == 0){
                status = 1;
            } else if(ch == LF && status == 1){
                break;
            } else status = 0;
        }
        String result = sb.toString();
        if(result.length() < 2) return "";
        return result.substring(0,result.length()-2);
    }


    public int read()
        throws IOException {
        if (pos >= count) {
            fill();
            if (pos >= count)
                return -1;
        }
        return buf[pos++]&0xff;
    }

    public int available()
        throws IOException {
        return (count - pos) + is.available();
    }

    public void close()
        throws IOException {
        if (is == null)
            return;
        is.close();
        buf = null;
    }

    protected void fill()
        throws IOException {
        pos = 0;
        count = 0;
        int nRead = is.read(buf, 0, buf.length);
        if (nRead > 0) {
            count = nRead;
        }
    }


}
