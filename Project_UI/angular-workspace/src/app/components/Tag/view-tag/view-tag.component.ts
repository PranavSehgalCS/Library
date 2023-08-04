import { Tag } from 'src/app/model/Tag';
import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { TagService } from 'src/app/services/tag.service';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-view-tag',
  templateUrl: './view-tag.component.html',
  styleUrls: ['../tags.css']
})

export class ViewTagComponent {
  public tagArr:Tag[] = [];
  public reload:boolean = true;
  constructor(  private title:Title,
                public router:Router,
                private tagService:TagService,
                private accService:AccountService){
    if(!accService.isLoggedIn()){
      router.navigate(['/login']);
    }else if(!accService.isAdmin()){
      router.navigate(['/dashboard']);
    }else{
      title.setTitle('Tags');
    }
  }

  ngOnInit(){
    this.tagService.getTags(-1).subscribe( res => {
      if(res==null || res == undefined){
        alert("ERROR While Getting Tags!");
      }else{
        this.tagArr = res;
      }
    });
  }
  
  
  createTag(){
    var resVal =  prompt("Enter name of the new Tag : ");
    if(resVal != null && resVal != undefined){
      resVal = resVal.trim();
      if(resVal.length<3){
        alert("Tag name must be at least 3 characters");
      }else{
        this.tagService.createTag(resVal).subscribe( res => {
          if(res == true){
              alert("Tag created successfully!");
              location.reload();
          }else{
            alert("ERROR while creating tag!");
          }
        });
      }
    }
  }
  editTag(tag:Tag){
    var resVal =  prompt("Enter New Name For '"+tag.tagName+"'  :");
    if(resVal != null && resVal != undefined){
      resVal = resVal.trim();
      if(resVal.length<3){
        alert("Tag name must be at least 3 characters");
      }else{
        this.tagService.updateTag(tag.tagId, String(resVal)).subscribe( res => {
          if(res==true){
            this.tagArr[this.tagArr.indexOf(tag)] = new Tag(tag.tagId, String(resVal));
            alert('Tag updated successfully!');
            this.reload = true;
          }
        });
      }
    }
  }
  deleteTag(tag:Tag){
    if(confirm("Are you sure you want to delete this tag?")){
      this.tagService.deleteTag(tag.tagId).subscribe( res => {
        if(res == true){
          alert("Tag Deleted Successfully!");
          delete this.tagArr[this.tagArr.indexOf(tag)];
          location.reload()
        }else{
          alert("ERROR While Deleting tag");
        }
      });
    } 
  }
}
