package Test;

import Server.ResponseData;
import fi.iki.elonen.NanoHTTPD;

public interface IPageProvider
{
     public ResponseData GetPage(NanoHTTPD.IHTTPSession request);
}
