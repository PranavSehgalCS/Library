package com.library.controller;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : TagController.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//
//  DESCRIPTION: Is a tag Controller file with preconfigurations all set.
//               USE this to take requests [GET, POST, PUT, PATCH & DELETE] from specific URLS
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.logging.Level;
import java.util.logging.Logger;
///////////////////////////////////////////////////////////////////////////////////////////////////////
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
///////////////////////////////////////////////////////////////////////////////////////////////////////
import com.library.model.Tag;
import com.library.persistance.Tag.TagDAO;
///////////////////////////////////////////////////////////////////////////////////////////////////////

@RestController
@RequestMapping("tags")
public class TagController {
    private TagDAO tagDAO;
    private static final Logger LOG = Logger.getLogger(TagController.class.getName());
    public TagController(TagDAO tagDao) {
        this.tagDAO = tagDao;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/{tagId}")
    public ResponseEntity<Tag[]>getTags( @PathVariable Integer tagId){
        LOG.info("\nGET /tag");
        try {
            Tag[] retVal = this.tagDAO.getAllTags(tagId);
            return new ResponseEntity<Tag[]>(retVal,HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE,"ERROR At Controller While Getting --> "+e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Boolean> createTag(  @RequestParam(name = "tagName", required = true) String tagName ){
        LOG.info("\nPOST /tag/" + tagName );
        try{
            Boolean responseVal = tagDAO.createTag(tagName);
            if(responseVal){
                return new ResponseEntity<Boolean>(responseVal, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
            }
        }
        catch(Exception e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("")
    public ResponseEntity<Boolean> updateTag(  @RequestParam(name = "tagId", required = true) Integer tagId,
                                                    @RequestParam(name = "tagName", required = true) String  tagName 
                                                ){
        LOG.info("\nPUT /tag/" + tagId + ":" + tagName);
        try{
            Boolean reponseVal =  tagDAO.updateTag(tagId, tagName);
            if(reponseVal){
                return new ResponseEntity<Boolean>(reponseVal, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteTag(  @RequestParam(name = "tagId", required = true) Integer tagId ){
        LOG.info("\nDELETE /tag/" + tagId);
        try {
            Boolean reponseVal =  tagDAO.deleteTag(tagId);
            if(reponseVal){
                return new ResponseEntity<Boolean>(reponseVal, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
