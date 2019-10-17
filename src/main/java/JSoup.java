import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qzs
 * @email 631706099@qq.com
 * @date 2019/10/15
 */
public class JSoup {

    public static void main (String [] args) throws IOException {
        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/index.html";
//        System.out.println(getPath(url));
        Document doc = getDocument(url);


        Map<String,Integer> provinceMap = new HashMap<String, Integer>();
        provinceMap.put("北京市",1);
        provinceMap.put("天津市",2);
        provinceMap.put("河北省",3);
        provinceMap.put("山西省",4);
        provinceMap.put("内蒙古自治区",5);
        provinceMap.put("辽宁省",6);
        provinceMap.put("吉林省",7);
        provinceMap.put("黑龙江省",8);
        provinceMap.put("上海市",9);
        provinceMap.put("江苏省",10);
        provinceMap.put("浙江省",11);
        provinceMap.put("安徽省",12);
        provinceMap.put("福建省",13);
        provinceMap.put("江西省",14);
        provinceMap.put("山东省",15);
        provinceMap.put("河南省",16);
        provinceMap.put("湖北省",17);
        provinceMap.put("湖南省",18);
        provinceMap.put("广东省",19);
        provinceMap.put("广西壮族自治区",20);
        provinceMap.put("海南省",21);
        provinceMap.put("重庆市",22);
        provinceMap.put("四川省",23);
        provinceMap.put("贵州省",24);
        provinceMap.put("云南省",25);
        provinceMap.put("西藏自治区",26);
        provinceMap.put("陕西省",27);
        provinceMap.put("甘肃省",28);
        provinceMap.put("青海省",29);
        provinceMap.put("宁夏回族自治区",30);
        provinceMap.put("新疆维吾尔自治区",31);
        provinceMap.put("台湾省",32);
        provinceMap.put("香港特别行政区",33);
        provinceMap.put("澳门特别行政区",34);

        Integer cityId = 1;
        Integer areaId = 1;

        for (Element e : doc.select("tr.provincetr")) {
            for (Element provinceE : e.select("td a")) {
                String provinceUrl = getPath(url) + provinceE.attr("href");
                String provinceName = provinceE.text();
                Integer provinceId = provinceMap.get(provinceName);
//                System.out.println( provinceName + "-------------" + provinceId);

                System.out.println("-- 开始导入:" + provinceName + "," + provinceId);
                for (Element cityE : getDocument(provinceUrl).select("tr.citytr")) {
                    Long cityCode = Long.parseLong( cityE.child(0).child(0).text());
                    String cityName = cityE.child(1).child(0).text();
                    String cityUrl = getPath(provinceUrl) + cityE.child(1).child(0).attr("href");
//                    System.out.println( "\t" + cityCode + "\t" + cityName);

                    System.out.println( "INSERT INTO `t_dist_city` (`id`, `unique_code`, `city`, `province_id`) VALUES " +
                            "('" + cityId +
                            "', '" + cityCode +
                            "', '" + cityName +
                            "', '" + provinceId +
                            "');");

                    for (Element areaE : getDocument(cityUrl).select("tr.countytr")) {
                        Long areaCode = Long.parseLong( areaE.child(0).text());
                        String areaName = areaE.child(1).text();
//                        System.out.println( "\t\t" + areaCode + "\t" + areaName);
                        System.out.println( "INSERT INTO `t_dist_area` (`id`, `unique_code`, `area`, `city_id`) VALUES " +
                                "('" + areaId +
                                "', '" + areaCode +
                                "', '" + areaName +
                                "', '" + cityId +
                                "');");

                        areaId++;
                    }
                    cityId ++;
                }

//
            }
        }

    }


    public static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).timeout(500000).get();
    }
    public static String getPath (String url) {
        return url.substring(0,url.lastIndexOf("/") + 1);
    }

    public static void sleep() {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void parseArea () {
        String html = "<ul class=\"area-selectable-list\"><li value=\"820101\" class=\"area-select-option selected\">\n" +
                "    离岛\n" +
                "</li><li value=\"820102\" class=\"area-select-option\">\n" +
                "    澳门半岛\n" +
                "</li><li value=\"820103\" class=\"area-select-option\">\n" +
                "    凼仔\n" +
                "</li><li value=\"820104\" class=\"area-select-option\">\n" +
                "    路凼城\n" +
                "</li><li value=\"820105\" class=\"area-select-option\">\n" +
                "    路环\n" +
                "</li></ul>";

        Document document = Jsoup.parse(html);
        for (Element element : document.select("li")) {
            System.out.println( "INSERT INTO `t_dist_area` (`unique_code`, `area`, `city_id`) VALUES " +
                    "(" +
                    "'" + element.attr("value") +
                    "', '" + element.text() +
                    "', '" + 346 +
                    "');");

        }

    }
}
