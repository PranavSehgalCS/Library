import { map } from 'rxjs';
import { Tag } from 'src/app/model/Tag';
import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { Book } from 'src/app/model/Book';
import { TagService } from 'src/app/services/tag.service';
import { BookService } from 'src/app/services/book.service';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-create-book',
  templateUrl: './create-book.component.html',
  styleUrls: ['../bookStyles.css']
})
export class CreateBookComponent {

  public tagArr:Tag[] = [];
  public chosenArr:Tag[] = [];
  public reload:boolean = true;
  private chosen:Map<Number,Tag> = new Map<Number,Tag>();
  private tagMap:Map<Number,Tag> = new Map<Number,Tag>();

  constructor(private router:Router,
              private tagService:TagService,
              private bookService:BookService,
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
        for(let tag of this.tagArr){
          this.tagMap.set(tag.tagId,tag);
        }
      }
    });
  }
  toNum(inVal:string):number{
    var retVal:number = Number(inVal);
    if(retVal<0){
      retVal=0;
    }else if(retVal>100){
      retVal = 100;
    }
    return retVal;
  }
  addTag(Id:number){
    var mapVal = this.tagMap.get(Id);
    if(mapVal !=undefined){
      this.chosen.set(Id,mapVal);
      this.chosenArr = Array.from(this.chosen.values());
    }
  }
  removeTag(tagId:number){
    this.chosen.delete(tagId);
    this.chosenArr = Array.from(this.chosen.values());
  }
  
  createBook(bookName:string, bookAuth:string, minAge:number, maxAge:number){
    bookName = bookName.trim();
    bookAuth = bookAuth.trim();
    if(bookName.length<2){
      alert("Book name must be at least 2 Characters");
    }else if(bookAuth.length<2){
      alert("Author name must be at least 2 Characters");
    }else if(minAge>maxAge){
      alert("Minimum Age must be less than Maximum Age");
    }else{
        var stringTags:string[] = [];
        for(let tag of this.chosenArr){
          stringTags.push(tag.tagName);
        }
        var newBook:Book = new Book(-1, [minAge,maxAge], bookName, bookAuth,stringTags );
        this.bookService.createBook(newBook).subscribe( res => {
          if(res ==true ){
            alert("Book Create Successfully!");
          }else{
            alert("ERROR While creating book!");
          }
        });
    }
  }
}
