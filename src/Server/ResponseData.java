package Server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.*;

public class ResponseData
{
    private String Response;
    private CookieData Cookies;
    public ResponseData(String response, CookieData cookies)
    {
        Response = response;
        Cookies = cookies;
    }

    public String GetResponse()
    {
        return Response;
    }

    public CookieData GetCookies()
    {
        return Cookies;
    }

}
