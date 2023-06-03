package Server;

import fi.iki.elonen.NanoHTTPD;

public interface IResponseProvider
{
    public ResponseData GetResponse(NanoHTTPD.IHTTPSession session);
}
