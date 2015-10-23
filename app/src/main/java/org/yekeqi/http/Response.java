package org.yekeqi.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class Response {

    private int statusCode;
    private Document responseAsDocument = null;
    private String responseAsString = null;
    private InputStream is;
    private HttpURLConnection con;
    private boolean streamConsumed = false;
    
    private HttpResponse response;
    private StatusLine statusLine;
    private Header[] responseHeader;
    
    public Response()  {
    	
    }
    public Response(HttpResponse response, String url) throws IOException {
    	this.response = response;
    	this.statusLine = response.getStatusLine();
        this.statusCode = statusLine.getStatusCode();
        this.responseHeader = response.getAllHeaders();
        
        HttpEntity entity = response.getEntity();
        this.is = entity.getContent();
        Header contentEncoding = entity.getContentEncoding();
        if (null != is && contentEncoding != null
                && "gzip".equals(contentEncoding.getValue())) {
            // the response is gzipped
            is = new GZIPInputStream(is);
        }
    }

    // for test purpose
    /*package*/ Response(String content) {
        this.responseAsString = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseHeader(String name) {
    	if (response != null) {
    		Header[] header = response.getHeaders(name);
    		if (header.length > 0) {
    			return header[0].getValue();
    		}
    	}
   		return null;
    }

    /**
     * Returns the response stream.<br>
     * This method cannot be called after calling asString() or asDcoument()<br>
     * It is suggested to call disconnect() after consuming the stream.
     *
     * Disconnects the internal HttpURLConnection silently.
     * @return response body stream
     * @throws HttpException
     * @see #disconnect()
     */
    public InputStream asStream() {
        if(streamConsumed){
            throw new IllegalStateException("Stream has already been consumed.");
        }
        return is;
    }

    /**
     * Returns the response body as string.<br>
     * Disconnects the internal HttpURLConnection silently.
     * @return response body
     * @throws HttpException
     */
    public String asString() throws ResponseException {
        if(null == responseAsString){
            BufferedReader br;
            try {
                InputStream stream = asStream();
                if (null == stream) {
                    return null;
                }
                br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuffer buf = new StringBuffer();
                String line;
                while (null != (line = br.readLine())) {
                    buf.append(line).append("\n");
                }
                this.responseAsString = buf.toString();
                    this.responseAsString = unescape(responseAsString);
//              log(responseAsString);
                stream.close();
                streamConsumed = true;
            } catch (NullPointerException npe) {
                // don't remember in which case npe can be thrown
                throw new ResponseException(npe.getMessage(), npe);
            } catch (IOException ioe) {
                throw new ResponseException(ioe.getMessage(), ioe);
            }
        }
        return responseAsString;
    }
  
    /**
     * Returns the response body as sinat4j.org.json.JSONObject.<br>
     * Disconnects the internal HttpURLConnection silently.
     * @return response body as sinat4j.org.json.JSONObject
     * @throws HttpException
     */
    public JSONObject asJSONObject() throws ResponseException {
        try {
            return new JSONObject(asString());
        } catch (JSONException jsone) {
            throw new ResponseException(jsone.getMessage() + ":" + this.responseAsString, jsone);
        }
    }
    
    /**
     * Returns the response body as sinat4j.org.json.JSONArray.<br>
     * Disconnects the internal HttpURLConnection silently.
     * @return response body as sinat4j.org.json.JSONArray
     * @throws HttpException
     */
    public JSONArray asJSONArray() throws ResponseException {
        try {
        	return  new JSONArray(asString());  
        } catch (Exception jsone) {
            throw new ResponseException(jsone.getMessage() + ":" + this.responseAsString, jsone);
        }
    }

    public InputStreamReader asReader() {
        try {
            return new InputStreamReader(is, "UTF-8");
        } catch (java.io.UnsupportedEncodingException uee) {
            return new InputStreamReader(is);
        }
    }

    public void disconnect(){
        con.disconnect();
    }

    private static Pattern escaped = Pattern.compile("&#([0-9]{3,5});");

    /**
     * Unescape UTF-8 escaped characters to string.
     * @author pengjianq...@gmail.com
     *
     * @param original The string to be unescaped.
     * @return The unescaped string
     */
    public static String unescape(String original) {
        Matcher mm = escaped.matcher(original);
        StringBuffer unescaped = new StringBuffer();
        while (mm.find()) {
            mm.appendReplacement(unescaped, Character.toString(
                    (char) Integer.parseInt(mm.group(1), 10)));
        }
        mm.appendTail(unescaped);
        return unescaped.toString();
    }

    @Override
	public String toString() {
		return "Response [statusCode=" + statusCode + ", responseAsDocument="
				+ responseAsDocument + ", responseAsString=" + responseAsString
				+ ", is=" + is + ", con=" + con + ", streamConsumed="
				+ streamConsumed + ", response=" + response + ", statusLine="
				+ statusLine + ", responseHeader="
				+ Arrays.toString(responseHeader) + "]";
	}

	public String getResponseAsString() {
		return responseAsString;
	}

	public void setResponseAsString(String responseAsString) {
		this.responseAsString = responseAsString;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
    
}
