package com.capstone.giftWeb.Service;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@Service
public class SurveyService {

    public String parseJson() throws IOException {

        ClassPathResource resource = new ClassPathResource("/static/survey.json");
        Reader reader = new FileReader(resource.getFile());
        StringBuffer stringBuffer=new StringBuffer();
        int cur;
        while((cur = reader.read()) != -1){
            stringBuffer.append((char)cur);
        }
        reader.close();
        return stringBuffer.toString();
    }
}
