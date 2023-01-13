import org.json.simple.parser.ParseException;

import java.util.List;


public class Main {
    public static void main(String[] args) throws ParseException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCVS = "data.csv";
        String fileNameXML = "data.xml";
        List<Employee> list = Parsers.parseCSV(columnMapping, fileNameCVS);
        String json = Parsers.listToJson(list);
        Parsers.writeString(json, "data.json");
        List<Employee> listXML = Parsers.parseXML(fileNameXML);
        String jsonXML = Parsers.listToJson(listXML);
        Parsers.writeString(jsonXML, "data2.json");
        String newJson = Parsers.readString("data.json");
        List<Employee> newList = Parsers.jsonToList(newJson);
        System.out.println(newList);
    }
}
