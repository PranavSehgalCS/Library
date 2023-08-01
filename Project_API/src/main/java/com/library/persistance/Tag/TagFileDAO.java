package com.library.persistance.Tag;

import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import com.library.persistance.Books.BookFileDAO;
import com.library.model.Tag;
import com.library.persistance.Commonfuncs;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Component
public class TagFileDAO implements TagDAO{
    Commonfuncs comm;
    int nextId = -1;
    Map<Integer,Tag> tagHolder = new HashMap<Integer, Tag>();

    public  TagFileDAO( @Value("${spring.datasource.url}") String database ,
                        @Value("${spring.datasource.username}") String datauser,
                        @Value("${spring.datasource.password}") String datapass
                        ){
        this.comm = new Commonfuncs(database, datauser, datapass);
        setNextId();
    }

    void setNextId(){
        try {
            tagHolder.clear();
            ResultSet load = comm.getQuery("SELECT * FROM allTags ORDER BY tagId;");
            Tag mapObj = new Tag(-5, null);
            while(load.next()){
                mapObj = new Tag(   load.getInt("tagId"),
                                    load.getString("tagName"));
                tagHolder.put(mapObj.getTagId(), mapObj);
            }
            nextId = mapObj.getTagId() + 1;
            load.getStatement().getConnection().close();
        } catch (Exception e) {
            System.out.println("ERROR While setting nextID at (TagFileDAO)--> " + e);
        }
    }

    @Override
    public Tag[] getAllTags(int j) {
        try {
            Tag[] retVal = null;
            if(j==-1){
                retVal = new Tag[tagHolder.size()];
                int index = 0;
                for(int i: tagHolder.keySet()){
                    retVal[index] = tagHolder.get(i);
                    index++;
                }
            }else if(tagHolder.containsKey(j)){
                    retVal = new Tag[1];
                    retVal[0]=tagHolder.get(j);
            }else{
                retVal = new Tag[1];
                retVal[0] = new Tag(-1, "");
            }
            return retVal;
        }catch (Exception e) {
            System.out.println("ERROR While Getting tags at (TagFileDAO) --> " + e);
            return null;
        }
   }

    @Override
    public boolean deleteTag(int tagId) {
        try {
            ResultSet load = this.comm.getQuery("SELECT * FROM allTags WHERE tagId = "+tagId+";");
            load.next();
            String tagName = load.getString("tagName");
            load.getStatement().getConnection().close();

            this.comm.setQuery("DELETE FROM allTags WHERE tagId = " + tagId + ";");
            this.comm.setQuery("DELETE FROM tags WHERE tag = '"+tagName+"';");
            this.tagHolder.remove(tagId);
            BookFileDAO.loadBooks();
            return true;
        } catch (Exception e) {
            System.out.println("ERROR While deleting tag at (TagFileDAO) --> " + e);
            return false;
        }
    }

    @Override
    public boolean createTag(String tagName) {
        try {
            if(nextId <= 0){
                setNextId();
                if(nextId<=-1){
                    throw new RuntimeException("Couldn't Initalize NextId");
                }
            }
            for(Tag i:tagHolder.values()){
                if(i.getTagName().toLowerCase().equals(tagName.toLowerCase())){
                    return false;
                }
            }
            nextId++;
            if(this.comm.setQuery("INSERT INTO allTags VALUES("+(nextId-1)+", '"+tagName+"')")){
                this.tagHolder.put(nextId-1, new Tag(nextId-1, tagName));
                return true;
            } 
            return false;
        } catch (Exception e) {
            System.out.println("ERROR While Creating new Tag at (TagFileDAO) --> " + e);
            return false;
        }
    }

    @Override
    public boolean updateTag(int tagId, String tagName) {
        try {
            ResultSet load = this.comm.getQuery("SELECT * FROM allTags WHERE tagId = "+tagId+";");
            load.next();
            String prevName = load.getString("tagName");
            load.getStatement().getConnection().close();
            
            boolean b1 = this.comm.setQuery("UPDATE allTags SET tagName = '"+tagName+"' WHERE tagId = "+tagId+";");
            b1 = b1 && this.comm.setQuery("UPDATE Tags SET tag = '"+tagName+"' WHERE tag = '"+prevName+"';");
            if(b1){
                this.tagHolder.put(tagId, new Tag(tagId,tagName));
                BookFileDAO.loadBooks();
            }
            return b1;
        } catch (Exception e) {
            System.out.println("ERROR While Updating Tag at (TafFileDAO) --> " + e);
            return false;
        }
    }
    
}
