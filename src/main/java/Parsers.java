import com.google.gson.*;
import com.google.gson.reflect.*;
import com.opencsv.*;
import com.opencsv.bean.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Parsers {

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> list = new ArrayList<>();
        Document document = null;
        Employee employee;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        NodeList employees = document.getElementsByTagName("employee");
        for (int i = 0; i < employees.getLength(); i++) {
            Node node = employees.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                employee = new Employee();
                employee.id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                employee.firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                employee.lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                employee.country = element.getElementsByTagName("country").item(0).getTextContent();
                employee.age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                list.add(employee);
            }
        }
        return list;
    }


    public static <T> String listToJson(List<T> list) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String readString(String fileName) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder json = new StringBuilder();
            while (bufferedReader.ready())
                json.append(bufferedReader.readLine());
            return json.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "ничего не вышло";
    }

    public static List<Employee> jsonToList(String json) throws ParseException {
        List<Employee> list = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        JSONArray jsonArray = (JSONArray) obj;
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            Employee employee = gson.fromJson(String.valueOf(jsonObject), Employee.class);
            list.add(employee);
        }

        return list;
    }
}
