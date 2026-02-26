package com.securin.recipe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.securin.recipe.model.Recipe;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final ObjectMapper mapper=new ObjectMapper();

    private static final int BATCH_SIZE=1000;
    private static final String FILE_PATH="C:/Users/harsh/Documents/GitHub/Securin-Assessment/recipe/src/main/resources/US_recipes_null.json";
    private static final String INSERT_SQL="Insert ignore into recipes"+
                                            "(title,cuisine,rating,prep_time,cook_time,"+
                                            "total_time,description,nutrients,serves)"+
                                            "VALUES(?,?,?,?,?,?,?,?,?)";
    @Override
    public void run(String... args){
        try{
            Long count=jdbcTemplate.queryForObject("Select count(*) from recipes",Long.class);
            if(count!=null && count>0){
                System.out.println("Database already loaded");
                return;
            }
        }
        catch(Exception e){
            System.out.println("Loading error data");
            e.printStackTrace();
        }
        File file=new File(FILE_PATH);
        if(!file.exists()){
            System.out.println("File not found");
            return;
        }
        System.out.println("Starting Data Load");
        Long startTime=System.currentTimeMillis();
        int totalLoaded=0;
        int totalSkipped=0;
        List<Recipe> batch=new ArrayList<>();

        try{
            JsonFactory factory=new JsonFactory();
            JsonParser parser=factory.createParser(file);
            parser.nextToken();
            while(parser.nextToken()!=JsonToken.END_OBJECT){
                parser.nextToken();
                try{
                    ObjectNode node=(ObjectNode) mapper.readTree(parser);
                    Recipe r=mapToRecipe(node);
                    if(isValid(r)){
                        batch.add(r);
                    }
                    else{
                        totalSkipped++;
                    }
                }
                catch(Exception e){
                    totalSkipped++;
                }
                if(batch.size()>=BATCH_SIZE){
                    insertBatch(batch);
                    totalLoaded+=batch.size();
                    System.out.println("Inserted "+totalLoaded+" records");
                    batch.clear();
                }
            }
            if(!batch.isEmpty()){
                insertBatch(batch);
                totalLoaded+=batch.size();
                batch.clear();
            }
            parser.close();
        }
        catch(Exception e){
            System.out.println("Error in Loading Data");
        }
        Long time_taken=(System.currentTimeMillis()-startTime)/1000;
        System.out.println("Inserted "+totalLoaded+" records");
        System.out.println("Total Loaded: "+totalLoaded);
        System.out.println("Total Skipped: "+totalSkipped);
        System.out.println("Time: "+time_taken);
    }
    private void insertBatch(List<Recipe> batch){
        jdbcTemplate.batchUpdate(INSERT_SQL,batch,batch.size(),(ps,r)->{
            ps.setString(1,r.getTitle());
            ps.setString(2,r.getCuisine());
            if(r.getRating()!=null){
                ps.setDouble(3,r.getRating());
            }
            else{
                ps.setNull(3,java.sql.Types.DOUBLE);
            }
            if(r.getPrepTime()!=null){
                ps.setInt(4,r.getPrepTime());
            }
            else{
                ps.setNull(4,java.sql.Types.INTEGER);
            }
            if(r.getCookTime()!=null){
                ps.setInt(5,r.getCookTime());
            }
            else{
                ps.setNull(5,java.sql.Types.INTEGER);
            }
            if(r.getTotalTime()!=null){
                ps.setInt(6,r.getTotalTime());
            }
            else{
                ps.setNull(6,java.sql.Types.INTEGER);
            }
            ps.setString(7,r.getDescription());
            ps.setString(8,r.getNutrients());
            ps.setString(9,r.getServes());
        });
    }
    private boolean isValid(Recipe r){
        if(r==null)return false;
        if(r.getTitle()==null || r.getTitle().isBlank())return false;
        return true;
    }
    private Recipe mapToRecipe(ObjectNode node){
        Recipe r=new Recipe();
        r.setTitle(getText(node,"title"));
        r.setCuisine(getText(node,"cuisine"));
        r.setRating(getDouble(node,"rating"));
        r.setPrepTime(getInt(node,"prep_time"));
        r.setCookTime(getInt(node,"cook_time"));
        r.setTotalTime(getInt(node,"total_time"));
        r.setDescription(getText(node,"description"));
        r.setNutrients(nodeToString(node,"nutrients"));
        r.setServes(getText(node,"serves"));
        return r;
    }
    private String getText(ObjectNode node,String field){
        JsonNode n=node.get(field);
        if(n==null || n.isNull())return null;
        return n.asText();
    }
    private Double getDouble(ObjectNode node,String field){
        JsonNode n=node.get(field);
        if(n==null || n.isNull())return null;
        try{
            Double val=n.asDouble();
            if(Double.isNaN(val) || Double.isInfinite(val))return null;
            return val;
        }
        catch(Exception e){
            return null;
        }
    }
    private Integer getInt(ObjectNode node,String field){
        JsonNode n=node.get(field);
        if(n==null || n.isNull())return null;
        try{
            String text=n.asText();
            if(text.equalsIgnoreCase("NaN"))return null;
            return n.asInt();
        }
        catch(Exception e){
            return null;
        }
    }
    private String nodeToString(ObjectNode node, String field){
        JsonNode n=node.get(field);
        try{
            return mapper.writeValueAsString(n);
        }
        catch(Exception e){
            return null;
        }
    }

}
