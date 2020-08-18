package com.wpp.devtools.util;

import com.thoughtworks.xstream.XStream;
import com.wpp.devtools.domain.pojo.WXTextMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-17
 **/
public class XmlUtil {
    /*
     * xml转map
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        HashMap<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();

        InputStream ins = request.getInputStream();
        Document doc = reader.read(ins);

        Element root = doc.getRootElement();
        @SuppressWarnings("unchecked")
        List<Element> list = root.elements();

        for(Element e:list){
            map.put(e.getName(), e.getText());
        }
        ins.close();
        return map;
    }
    /*
     * 文本消息对象转xml
     */
    public static String textMsgToxml(WXTextMessage textMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }
}
