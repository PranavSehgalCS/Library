import { Tag } from 'src/app/model/Tag';
import { Router } from '@angular/router';
import { Component } from '@angular/core';
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
  constructor(  private router:Router,
                private tagService:TagService,
                private accService:AccountService){
    if(!accService.isLoggedIn()){
      router.navigate(['/login']);
    }else if(!accService.isAdmin()){
      router.navigate(['/dashboard']);
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
}
